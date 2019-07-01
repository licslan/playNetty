package com.licslan.hello;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class HelloNettyInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 通过SocketChannel去获得对应的管道
        ChannelPipeline pipeline = socketChannel.pipeline();
        //通过管道添加handler
        //添加httpServerCodec   当请求到服务端我们需要解码  并响应到客户端
        pipeline.addLast("HttpServerCodec",new HttpServerCodec());
        //添加自定义返回类  如何  text or json
        pipeline.addLast("youselfhanler",new YouselfHandler());
    }
}
