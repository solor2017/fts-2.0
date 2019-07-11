package com.hcq.fts.factory;

import com.hcq.fts.pojo.FileBox;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

/**
 * @Author: solor
 * @Since: 2.0
 * @Description:
 */
public class EmptyStrategy implements FileOperateStrategy {

    public void doOperate(ChannelHandlerContext ctx, FileBox f) throws IOException {

    }
}
