package com.licslan.hello;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
/**
 * @author LICSLAN
 * 初始化子处理类
 * */
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
        //添加普通响应服务端handler
        //pipeline.addLast("helloServer2ClinetHander",new HelloServer2ClinetHander());

        //FIXME 注意：YouselfHandler  &  HelloServer2ClinetHander都有返回   只能一个生效  可以注释一个


        //TODO and  also you can still add some other YourselfHandlers to pipeline to deal with your logic business
    }
}
