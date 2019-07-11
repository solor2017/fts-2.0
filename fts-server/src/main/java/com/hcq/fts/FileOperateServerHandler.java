package com.hcq.fts;


import com.hcq.fts.factory.FileOperateActivity;
import com.hcq.fts.factory.FileOperateStrategyFactory;
import com.hcq.fts.pojo.FileBox;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: solor
 * @Since: 2.0
 * @Description:
 */
@Slf4j
public class FileOperateServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 链路建立成功后调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    	// TODO Auto-generated method stub

        log.info("服务端接到连接请求："+ctx.channel().remoteAddress());
    }
    /**
     * 收到文件后调用
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof FileBox) {
            FileBox ef = (FileBox) msg;
            log.info("服务端：收到"+((FileBox) msg).getOperate());
            FileOperateActivity fileOperateActivity = new FileOperateActivity(
                    ctx,ef, FileOperateStrategyFactory.getFileOperateStrategy(ef.getOperate()));
            fileOperateActivity.execute(ctx,ef);

        }
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	// TODO Auto-generated method stub
        log.info("服务端：channelInactive()");
    	ctx.flush();
    	ctx.close();
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        cause.printStackTrace();
        ctx.close();
        log.info("FileOperateServerHandler--exceptionCaught()");
    }


}
