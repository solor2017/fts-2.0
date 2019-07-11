package com.hcq.fts.handler.template;

import com.hcq.fts.config.Config;
import com.hcq.fts.pojo.FileBox;
import com.hcq.fts.utils.MacUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author: solor
 * @Since: 2.0
 * @Description:
 */
public class FileClientHandler extends ChannelInboundHandlerAdapter {
    protected Config config;
    protected int byteRead;
    protected volatile int start = 0;
    protected volatile int lastLength = 0;
    public RandomAccessFile randomAccessFile;
    protected FileBox fileUploadFile;
    protected String Customer;
    protected ChannelHandlerContext ctx;

    public FileClientHandler() {
        this.config = Config.getInstance();
    }

    /**
     * tcp链路建立成功后调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        FileBox message = FileBox.builder().time(LocalDateTime.now())
                .addr(InetAddress.getLocalHost().getHostAddress()+"||"+ MacUtil.GetMac()).build();
    }

}
