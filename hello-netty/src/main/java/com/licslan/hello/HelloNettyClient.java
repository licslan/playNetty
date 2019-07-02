package com.licslan.hello;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author LICSLAN
 * netty 客户端请求处理类
 * */
public class HelloNettyClient {

    private void connectServer(String host, int port){

        //客户端线程组发送服务端连接
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        //客户端启动类
        Bootstrap bootstrap = new Bootstrap();

        //客户端设置双向通道channel
        bootstrap.group(workGroup)
                .channel(NioSocketChannel.class)
                .handler(new YouSelfClientHandler());

        try {
            ChannelFuture sync = bootstrap.connect(host, port).sync();
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        HelloNettyClient helloNettyClient = new HelloNettyClient();
        helloNettyClient.connectServer("localhost",8888);
    }


}
