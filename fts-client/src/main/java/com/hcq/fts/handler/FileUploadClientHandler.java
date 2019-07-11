package com.hcq.fts.handler;

import com.hcq.fts.handler.template.FileClientHandler;
import com.hcq.fts.pojo.FileBox;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @Author: solor
 * @Since: 2.0
 * @Description:
 */
@Slf4j
public class FileUploadClientHandler extends ChannelInboundHandlerAdapter {
	private int byteRead;
	private volatile int start = 0;
	private volatile int lastLength = 0;
	public RandomAccessFile randomAccessFile;
	private FileBox fileUploadFile;
	private Object response;

	public Object getResponse() {
		return response;
	}

	public FileUploadClientHandler(FileBox ef) {
		super();
		if (ef.getFile().exists()) {
			if (!ef.getFile().isFile()) {
				System.out.println("Not a file :" + ef.getFile());
				return;
			}
		}
		this.fileUploadFile = ef;
	}

	/**
	 * 建立连接成功后调用
	 * @param ctx
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		try {
			log.info("开始传输");
			randomAccessFile = new RandomAccessFile(fileUploadFile.getFile(),"r");
			randomAccessFile.seek(fileUploadFile.getStarPos());
			byte[] bytes = new byte[(int)randomAccessFile.length()];
			if ((byteRead = randomAccessFile.read(bytes)) != -1) {
				fileUploadFile.setEndPos(byteRead);
				fileUploadFile.setBytes(bytes);
				ctx.channel().writeAndFlush(fileUploadFile);
			} else {
			}
			log.info("客户端开始连接 "+ctx.channel().remoteAddress());
			log.info("channelActive()文件已经读完 "+byteRead);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	/**
	 * 收到消息后调用,续传
	 * @param ctx
	 * @param msg
	 * @throws Exception
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)throws Exception {
		response = msg;
		if (msg instanceof Integer) {
			start = (Integer) msg;
			if (start != -1) {
				log.info(msg.toString());
				log.info("续传");
				randomAccessFile = new RandomAccessFile(fileUploadFile.getFile(), "r");
				randomAccessFile.seek(start);
				log.info("长度："  + (randomAccessFile.length() - start));
				int a = (int) (randomAccessFile.length() - start);
				int b = (int) (randomAccessFile.length() );
				if (a < lastLength) {
					lastLength = a;
				}
				log.info("文件长度" + (randomAccessFile.length())+",start:"+start+",a:"+a+",b:"+b+",lastLength:"+lastLength);
				byte[] bytes = new byte[lastLength];
				if ((byteRead = randomAccessFile.read(bytes)) != -1
						&& (randomAccessFile.length() - start) > 0) {
					fileUploadFile.setEndPos(byteRead);
					fileUploadFile.setBytes(bytes);
					try {
						ctx.channel().writeAndFlush(fileUploadFile);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					randomAccessFile.close();
					ctx.channel().close();
				}
			}
		}else {
			log.info(msg.toString());
		}
	}
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelInactive(ctx);
		log.info("连接断开");
	}


	// @Override
	// public void channelReadComplete(ChannelHandlerContext ctx) throws
	// Exception {
	// ctx.flush();
	// }

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}


}
