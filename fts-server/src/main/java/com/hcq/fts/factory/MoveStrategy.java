package com.hcq.fts.factory;

import com.hcq.fts.pojo.FileBox;
import com.hcq.fts.utils.Config;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

/**
 * @Author: solor
 * @Since: 2.0
 * @Description:
 */
@Slf4j
public class MoveStrategy implements FileOperateStrategy {

    public  void doOperate(ChannelHandlerContext ctx, FileBox ef) throws IOException {

        String oldpath = Config.getInstance().getWorkdir()+ File.separator+
                ef.getOldRemoteFilePath();
        File oldFile = new File(oldpath);
        if(!oldFile.exists())
            oldFile.mkdirs();
        oldFile=new File(oldFile,ef.getOldRemoteFileName());
        if (!oldFile.exists()){oldFile.createNewFile();}
        RandomAccessFile srcFile = new RandomAccessFile(oldFile, "r");
        FileChannel srcFileChannel = srcFile.getChannel();

        String newpath = Config.getInstance().getWorkdir()+ File.separator+
                ef.getNewRemoteFilePath();
        File newFile = new File(newpath);
        if(!newFile.exists())
            newFile.mkdirs();
        newFile=new File(newFile,ef.getNewRemoteFileName());
        if (!newFile.exists()){newFile.createNewFile();}

        RandomAccessFile destFile = new RandomAccessFile(newFile, "rw");
        FileChannel destFileChannel = destFile.getChannel();

        long position = 0;
        Long srcFileSize = srcFileChannel.size();

//        if (ctx.pipeline().get(SslHandler.class) == null) {
//            // SSL not enabled - can use zero-copy file transfer.
//            ChannelFuture write = ctx.writeAndFlush(new DefaultFileRegion(destFileChannel, 0, count));//写完不管失败还是成功都release掉了
//            log.info("文件拷贝"+write.isSuccess());
//            if (write.isSuccess()){
//                ctx.channel().writeAndFlush(100);
//                log.info("文件拷贝完成");
//                srcFile.close();
//                destFile.close();
//            } else {
//                log.info("文件拷贝失败");
//                ctx.channel().writeAndFlush(ef);
//                srcFile.close();
//                destFile.close();
////                ctx.flush();
////                ctx.close();
//            }
//        } else {
//            log.info("开启了SSL");
//            // SSL enabled - cannot use zero-copy file transfer.
//            ctx.write(new ChunkedFile(destFile));
//        }


//        srcFileChannel.transferTo(position, count, destFileChannel);
        Long transferSize = srcFileChannel.transferTo(position, srcFileSize, destFileChannel);
        if (transferSize.longValue()==srcFileSize.longValue()) {
            ctx.channel().writeAndFlush(100);
            log.info("文件移动完成");
            destFile.close();
            srcFile.close();
            Files.deleteIfExists(oldFile.toPath());
        }else {
            log.info("文件移动失败");
            ctx.flush();
            ctx.close();
        }
    }
}

