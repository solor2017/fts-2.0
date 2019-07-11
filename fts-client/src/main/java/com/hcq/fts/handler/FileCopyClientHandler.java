package com.hcq.fts.handler;

import com.hcq.fts.pojo.FileBox;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.io.RandomAccessFile;

/**
 * @Author: solor
 * @Since: 2.0
 * @Description:
 */
@Slf4j
public class FileCopyClientHandler extends ChannelInboundHandlerAdapter {
    private int byteRead;
    private volatile int start = 0;
    private volatile int lastLength = 0;
    public RandomAccessFile randomAccessFile;
    private FileBox fileUploadFile;
    private Object response;

    public Object getResponse() {
        return response;
    }
    public FileCopyClientHandler(FileBox fileUploadFile) {
        this.fileUploadFile = fileUploadFile;
    }

    /**
     * 建立连接时调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().writeAndFlush(fileUploadFile);
    }

    /**
     * 收到消息跳跃
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        response = msg;
        log.info(msg.toString());
        if (msg instanceof Integer) {
            log.info(ctx.channel().remoteAddress().toString()+"远程拷贝完成"+msg.toString());
            ctx.close();
        } else {
            log.info("再次请求远程空白");
            ctx.channel().writeAndFlush(msg);
        }

    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        log.info("release");
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
