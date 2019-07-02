package com.licslan.hello;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
/**
 * @author LICSLAN
 * */
public class YouSelfClientHandler extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel){
        //获取pipline
        ChannelPipeline pipeline = socketChannel.pipeline();

        //TODO
        pipeline.addLast("licslanHandler",new LicslanResponseHandler());
    }
}
