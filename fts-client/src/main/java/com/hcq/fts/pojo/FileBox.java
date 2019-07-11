package com.hcq.fts.pojo;

import lombok.Builder;
import lombok.Data;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: solor
 * @Since: 2.0
 * @Description:
 */
@Builder
@Data
public class FileBox implements Serializable {

    private static final long serialVersionUID = 1L;
    private String addr;//客户端标识
    private LocalDateTime time;
    private File file;
    private String file_md5;//原文件名
    private int starPos;
    private byte[] bytes;
    private int endPos;

    private String localFileFullPath;
    private String remoteFileName;
    private String remoteFilePath;
    //以下属性作为copy或move文件时使用
    private String oldRemoteFileName;
    private String newRemoteFileName;
    private String oldRemoteFilePath;
    private String newRemoteFilePath;
    private int operate;
    private int sign;

//    public FileBox
//    public FileBox(String login, long currentTimeMillis, String customer) {
//        this.login
//    }

//    static enum Operate {
//        OP_UPLOAD,//上传
//        OP_DOWNLOAD,//下载
//        OP_DELETE,//删除
//        OP_CHECK, //检查文件是否存在
//        OP_COPY,//远程拷贝
//        OP_RENAME,//rename
//        OP_MOVE,//移动
//        OP_SWF,//检测swf文件个数
//        OP_THUMB;//缩略图
//    }
//
//    static enum Status {
//        TODO,//待处理
//        DELETED,//已删除
//        DONE;//已处理
//    }


}
