package com.licslan.hello;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class HelloNettyServer {

    public static void main(String[] args) {

        //使用netty 官方推荐的Recto模型第三种方式  主从线程组模型

        NioEventLoopGroup  bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup  workGroup = new NioEventLoopGroup();

        //netty 服务器创建一个bootstrap类
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        //塞入主从线程池
        serverBootstrap.group(bossGroup,workGroup)
                //设置双向通道channel
                        .channel(NioServerSocketChannel.class)
                //子处理器用于处理workGroup
                        .childHandler(new HelloNettyInitializer());

        try {
            ChannelFuture sync = serverBootstrap.bind(8888).sync();
            //监听关闭channel  设置为同步方式
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }


    }




}
