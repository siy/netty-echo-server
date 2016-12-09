package com.petehouston.netty.echo;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelFuture;

public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    /**
     * Run with NIO
     */
    public void runNio() throws Exception {
        
        EventLoopGroup masterGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(masterGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new EchoServerInitializer())
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.bind(this.port).sync();
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            masterGroup.shutdownGracefully();
        }
    }
    
    /**
     * Run with OIO (old IO)
     */
    public void runOio() throws Exception {
        
        EventLoopGroup masterGroup = new OioEventLoopGroup();
        EventLoopGroup workerGroup = new OioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(masterGroup, workerGroup)
                .channel(OioServerSocketChannel.class)
                .childHandler(new EchoServerInitializer())
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.bind(this.port).sync();
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            masterGroup.shutdownGracefully();
        }
    }
    
    /**
     * Run with Linux epoll, requires Linux
     */
    public void runEPoll () throws Exception {
        
        EventLoopGroup masterGroup = new EpollEventLoopGroup();
        EventLoopGroup workerGroup = new EpollEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(masterGroup, workerGroup)
                .channel(EpollServerSocketChannel.class)
                .childHandler(new EchoServerInitializer())
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.bind(this.port).sync();
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            masterGroup.shutdownGracefully();
        }
    }
}
