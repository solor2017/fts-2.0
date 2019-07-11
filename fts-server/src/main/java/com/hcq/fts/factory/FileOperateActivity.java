package com.hcq.fts.factory;

import com.hcq.fts.pojo.FileBox;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

/**
 * @Author: solor
 * @Since: 2.0
 * @Description:
 */
public class FileOperateActivity {

    private ChannelHandlerContext ctx;
    private FileBox f;
    private FileOperateStrategy fileOperateStrategy;

    public FileOperateActivity(ChannelHandlerContext ctx, FileBox f, FileOperateStrategy fileOperateStrategy) {

        this.ctx = ctx;
        this.f = f;
        this.fileOperateStrategy = fileOperateStrategy;
    }
    public FileOperateActivity(FileOperateStrategy fileOperateStrategy) {

        this.ctx = ctx;
        this.f = f;
        this.fileOperateStrategy = fileOperateStrategy;
    }
    public void execute(ChannelHandlerContext ctx, FileBox f) throws IOException {

        fileOperateStrategy.doOperate( ctx,  f);
    }

}
