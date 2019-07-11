package com.hcq.fts.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: solor
 * @Since: 2.0
 * @Description:就叫文件操作策略工厂吧
 */
public class FileOperateStrategyFactory {

    private static Map<Integer,FileOperateStrategy> FILE_OPERATE_STRATEGY_MAP = new HashMap<Integer, FileOperateStrategy>(16);
    static {
        FILE_OPERATE_STRATEGY_MAP.put(FileOperateKey.OP_UPLOAD,new UpLoadStrategy());
        FILE_OPERATE_STRATEGY_MAP.put(FileOperateKey.OP_COPY,new CopyStrategy());
        FILE_OPERATE_STRATEGY_MAP.put(FileOperateKey.OP_DELETE,new DeleteStrategy());
        FILE_OPERATE_STRATEGY_MAP.put(FileOperateKey.OP_DOWNLOAD,new DownLoadStrategy());
        FILE_OPERATE_STRATEGY_MAP.put(FileOperateKey.OP_CHECK,new CheckStrategy());
        FILE_OPERATE_STRATEGY_MAP.put(FileOperateKey.OP_RENAME,new RenameStrategy());
        FILE_OPERATE_STRATEGY_MAP.put(FileOperateKey.OP_MOVE,new MoveStrategy());
        FILE_OPERATE_STRATEGY_MAP.put(FileOperateKey.OP_SWF,new SwfStrategy());
        FILE_OPERATE_STRATEGY_MAP.put(FileOperateKey.OP_THUMB,new ThumbStrategy());
    }

    private static final FileOperateStrategy NON_OPERATE = new EmptyStrategy();

    private FileOperateStrategyFactory(){}

    public static FileOperateStrategy getFileOperateStrategy(int fileOperateKey){
        FileOperateStrategy fileOperateStrategy = FILE_OPERATE_STRATEGY_MAP.get(fileOperateKey);
        return fileOperateStrategy == null ? NON_OPERATE : fileOperateStrategy;
    }

    private interface FileOperateKey{
        public static final int OP_UPLOAD = 0x000F01;
        public static final int OP_DOWNLOAD = 0x000F02;
        public static final int OP_DELETE = 0x000F03;
        public static final int OP_CHECK = 0x000F04;
        public static final int OP_COPY = 0x000F05;
        public static final int OP_RENAME = 0x000F06;
        public static final int OP_MOVE = 0x000F07;
        public static final int OP_SWF = 0x000F08;
        public static final int OP_THUMB= 0x000F09;
    }

    public static void main(String[] args) {
        int fileOperateKey = 0x000F01;
        FileOperateActivity fileOperateActivity = new FileOperateActivity(FileOperateStrategyFactory.getFileOperateStrategy(fileOperateKey));
//        fileOperateActivity.execute();
    }
}
