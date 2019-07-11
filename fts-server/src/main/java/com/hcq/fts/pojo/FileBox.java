package com.hcq.fts.pojo;

import lombok.Builder;
import lombok.Data;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * @Author: solor
 * @Since: 2.0
 * @Description:
 */
@Builder
@Data
public class FileBox implements Serializable {

    private static final long serialVersionUID = 1L;
    private String addr;
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



}
