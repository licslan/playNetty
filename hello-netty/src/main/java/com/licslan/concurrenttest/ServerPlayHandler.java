package com.licslan.concurrenttest;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ServerPlayHandler extends SimpleChannelInboundHandler<HttpObject> {

    private static ExecutorService threadPool = Executors.newFixedThreadPool(50);
    //定义请求次数

    private static AtomicInteger totalRequest = new AtomicInteger(0);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject o){

        /*Channel channel = channelHandlerContext.channel();*/
        int i = totalRequest.incrementAndGet();

        log.info("客户端连接数为======>  "+i+ "次");
        if(o instanceof HttpRequest){
            //构建响应体
            Xerson person = new Xerson();
            person.setAge(28);
            person.setName("LICSLAN");
            person.setCount(i);

            ByteBuf byteBuf = Unpooled.copiedBuffer(JSON.toJSONString(person), CharsetUtil.UTF_8);
            // 构建一个http response
            FullHttpResponse response =
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                            HttpResponseStatus.OK,
                            byteBuf);

            /* response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");*/
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");

            log.info("===============>"+JSON.toJSONString(person));
            // 把响应刷到客户端
            //channelHandlerContext.writeAndFlush(response);
					threadPool.submit(() -> {
                        channelHandlerContext.channel().writeAndFlush(response);
					});

        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channel。。。注册");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channel。。。移除");
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("channel。。。活跃");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("channel。。。不活跃");
        super.channelInactive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("channeld读取完毕。。。");
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("用户事件触发。。。");
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        log.info("channel可写更改");
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("补货到异常");
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("助手类添加");
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info("助手类移除");
        super.handlerRemoved(ctx);
    }
}
@Data
class Xerson{
    private int age;
    private String name;
    private int count;
}