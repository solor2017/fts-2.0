package com.hcq.fts.factory;

import com.hcq.fts.pojo.FileBox;
import io.netty.channel.ChannelHandlerContext;

import java.io.FileNotFoundException;

/**
 * @Author: solor
 * @Since: 2.0
 * @Description:
 */
public class SwfStrategy implements FileOperateStrategy {

    public void doOperate() {
        System.out.println("OP_SWF");
    }

    public void doOperate(ChannelHandlerContext ctx, FileBox f) throws FileNotFoundException {

    }
}
