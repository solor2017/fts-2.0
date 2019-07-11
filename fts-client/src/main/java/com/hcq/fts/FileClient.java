package com.hcq.fts;

import com.hcq.fts.config.Config;
import com.hcq.fts.handler.FileCopyClientHandler;
import com.hcq.fts.handler.FileMoveClientHandler;
import com.hcq.fts.handler.FileUploadClientHandler;
import com.hcq.fts.pojo.FileBox;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Paths;

/**
 * @Author: solor
 * @Since: 2.0
 * @Description:
 */
@Slf4j
public class FileClient {

	private Config config;

	public static final int OP_UPLOAD = 0x000F01;
	public static final int OP_DOWNLOAD = 0x000F02;
	public static final int OP_DELETE = 0x000F03;
	public static final int OP_CHECK = 0x000F04;
	public static final int OP_COPY = 0x000F05;
	public static final int OP_RENAME = 0x000F06;
	public static final int OP_MOVE = 0x000F07;
	public static final int OP_SWF = 0x000F08;
	public static final int OP_THUMB= 0x000F09;

	public static final int PAKER_SIZE=1024;
	public static final String LINE="\r\n";
	public static final String SPLITER="!)($#%)#(%&#)##%)_#%_#%_#((@(@)#@$$%%^^!@@+==!!!";

	public FileClient() {
		this.config = Config.getInstance();
	}

	/**
	 * 上传文件
	 * @param localFileFullPath 本文件路径
	 * @param remoteFileName  指定的远程文件名
	 * @param remoteFilePath 指定的远程文件路径
	 */
	public Object uploadFile(String localFileFullPath, String remoteFileName,String remoteFilePath){

		File file = Paths.get(localFileFullPath).toFile();
		String fileMd5 = file.getName();// 文件名
		final FileBox fileBox = FileBox.builder()
				.file(file)
				.file_md5(fileMd5)
				.starPos(0)
				.remoteFileName(remoteFileName)
				.remoteFilePath(remoteFilePath).operate(OP_UPLOAD).build();
		FileUploadClientHandler fileUploadClientHandler = new FileUploadClientHandler(fileBox);
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<Channel>() {
						@Override
						protected void initChannel(Channel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(new ObjectEncoder());
							pipeline.addLast(new ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(null)));
							pipeline.addLast(fileUploadClientHandler);
						}
					});
			ChannelFuture f = b.connect(Config.getInstance().getServerHost(), Config.getInstance().getServerPort()).sync();
			f.channel().closeFuture().sync();
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			group.shutdownGracefully();
		}
		return  fileUploadClientHandler.getResponse();
	}

	/**
	 *
	 * @param oldRemoteFileName 原文件名
	 * @param newRemoteFileName 原文件路径
	 * @param oldRemoteFilePath 新文件名
	 * @param newRemoteFilePath 新文件路径
	 * @return
	 */
	public Object copyFile(String oldRemoteFileName,String newRemoteFileName,
							String oldRemoteFilePath,String newRemoteFilePath){
		boolean result=false;
		final FileBox fileBox = FileBox.builder()
				.starPos(0)
				.oldRemoteFilePath(oldRemoteFilePath)
				.oldRemoteFileName(oldRemoteFileName)
				.newRemoteFilePath(newRemoteFilePath)
				.newRemoteFileName(newRemoteFileName).operate(OP_COPY).build();
		FileCopyClientHandler fileCopyClientHandler = new FileCopyClientHandler(fileBox);
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<Channel>() {

						@Override
						protected void initChannel(Channel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(new ObjectEncoder());
							pipeline.addLast(new ObjectDecoder(
									ClassResolvers.weakCachingConcurrentResolver(null)));
							pipeline.addLast(fileCopyClientHandler);
						}
					});
			ChannelFuture f = b.connect(Config.getInstance().getServerHost(),Config.getInstance().getServerPort()).sync();
			f.channel().closeFuture().sync();
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			group.shutdownGracefully();
		}
		return fileCopyClientHandler.getResponse();
	}
	/**
	 *
	 * @param oldRemoteFileName 原文件名
	 * @param newRemoteFileName 原文件路径
	 * @param oldRemoteFilePath 新文件名
	 * @param newRemoteFilePath 新文件路径
	 * @return
	 */
	public Object moveFile(String oldRemoteFileName,String newRemoteFileName,
						 String oldRemoteFilePath,String newRemoteFilePath){
		boolean result=false;
		final FileBox fileBox = FileBox.builder()
				.starPos(0)
				.oldRemoteFilePath(oldRemoteFilePath)
				.oldRemoteFileName(oldRemoteFileName)
				.newRemoteFilePath(newRemoteFilePath)
				.newRemoteFileName(newRemoteFileName).operate(OP_MOVE).build();
		FileMoveClientHandler fileMoveClientHandler = new FileMoveClientHandler(fileBox);
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<Channel>() {

						@Override
						protected void initChannel(Channel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(new ObjectEncoder());
							pipeline.addLast(new ObjectDecoder(
									ClassResolvers.weakCachingConcurrentResolver(null)));
							pipeline.addLast(fileMoveClientHandler);
						}
					});
			ChannelFuture f = b.connect(Config.getInstance().getServerHost(),Config.getInstance().getServerPort()).sync();
			f.channel().closeFuture().sync();
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			group.shutdownGracefully();
		}
		return fileMoveClientHandler.getResponse();
	}
	public static void main(String[] args) {

		try {
//			ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
//			for (int i = 0; i < 10; i++) {
//				final int index = i;
//				fixedThreadPool.execute(new Runnable() {
//
//
////					@Override
//					public void run() {
//						try {
//							new FileUploadClient().uploadFile("d:/jfts.log","aa.log","fts"+String.valueOf(index));
//							System.out.println(index);
//							Thread.sleep(2000);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//				});
//			}

			new FileClient().uploadFile("d:/jfts.log","aa.log","22");
//			new FileClient().copyFile("av.zip","bv.zip","22","33");
//			new FileClient().moveFile("aa.log","bb.log","22","33");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
