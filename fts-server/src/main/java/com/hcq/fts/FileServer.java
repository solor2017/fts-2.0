package com.hcq.fts;

import com.hcq.fts.utils.Config;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: solor
 * @Since: 2.0
 * @Description:
 */
@Slf4j
public class FileServer {

    private Config config;
    public FileServer() {
        this.config = Config.getInstance();
    }

    public void bind(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).
                    option(ChannelOption.SO_BACKLOG, 1024).
                    childHandler(new ChannelInitializer<Channel>() {

                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            log.info("有新客户端连接:"+ch.remoteAddress().toString());
                            ch.pipeline().addLast(new ObjectEncoder());
                            ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE,
                                    ClassResolvers.weakCachingConcurrentResolver(null))); // 最大长度
                            ch.pipeline().addLast(new FileOperateServerHandler());
                            ch.pipeline().addLast(new PolicyHandler());
                        }
            });
            ChannelFuture f = b.bind(port).sync();
            log.info("\n");
            log.info("=====================================================================");
            log.info("                      欢迎使用FTS文件服务器，正在监听连接...");
            log.info("=====================================================================");
            f.channel().closeFuture().sync();
            log.info("file end");
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        int port =  FILE_PORT;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        try {
            new FileServer().bind(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static final int FILE_PORT = 8888;
}
