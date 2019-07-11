package com.hcq.fts.factory;

import com.hcq.fts.pojo.FileBox;
import com.hcq.fts.utils.Config;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @Author: solor
 * @Since: 2.0
 * @Description:
 */
@Slf4j
public class UpLoadStrategy implements FileOperateStrategy {
    private volatile int byteRead;
    private volatile int start = 0;
    private RandomAccessFile randomAccessFile;

    public void doOperate(ChannelHandlerContext ctx, FileBox ef) throws IOException {

        byte[] bytes = ef.getBytes();
        int end = ef.getEndPos();
        String md5 = ef.getFile_md5();//文件名
//        String path = Config.getInstance().getWorkdir()+ File.separator +
//                ef.getRemoteFilePath()+File.separator+ef.getRemoteFileName();

//        Files.walkFileTree(Paths.get(path),new SimpleFileVisitor<Path>(){//window不支持
//            @Override
//            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
//
//                randomAccessFile = new RandomAccessFile(file.toFile(), "rw");
//                System.out.println(file);
//                return FileVisitResult.CONTINUE;
//            }
//        });

        String path =  Config.getInstance().getWorkdir()+ File.separator + ef.getRemoteFilePath();
        File saveFile = new File(path);
        if(!saveFile.exists())
            saveFile.mkdirs();
        saveFile=new File(saveFile,ef.getRemoteFileName());
        if (!saveFile.exists()){saveFile.createNewFile();}

        randomAccessFile = new RandomAccessFile(saveFile, "rw");
        randomAccessFile.seek(start);
        randomAccessFile.write(bytes);

        if ((byteRead = randomAccessFile.read(bytes)) != -1) {
            start = start + byteRead;
            ctx.channel().writeAndFlush(start);
            randomAccessFile.close();
        } else {
            log.info("文件写入完成");
            ctx.channel().writeAndFlush("文件写入完成");
            randomAccessFile.close();
            ctx.flush();
            ctx.close();
        }

    }
}
