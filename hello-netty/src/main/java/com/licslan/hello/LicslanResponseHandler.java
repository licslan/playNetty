package com.licslan.hello;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
/**
 * netty 客户端连接成功后发送消息
 * @author LICSLAN
 * */
@Slf4j
public class LicslanResponseHandler extends ChannelInboundHandlerAdapter {
//    // 连接成功后，向server发送消息
//
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        //super.channelActive(ctx);
//        String msg = "hello Server!";
//        ByteBuf encoded = ctx.alloc().buffer(4 * msg.length());
//        ctx.writeAndFlush(encoded);
//    }
//
//    @Override
//    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        super.channelInactive(ctx);
//    }
//
//    //读取服务端发送的信息
//
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        //super.channelRead(ctx, msg);
//        log.info("SimpleClientHandler.channelRead");
//        ByteBuf result = (ByteBuf) msg;
//        byte[] resultByte = new byte[result.readableBytes()];
//        result.readBytes(resultByte);
//        log.info("Server said:" + new String(resultByte));
//        result.release();
//    }
//
//    // 当出现异常就关闭连接
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
//        ctx.close();
//    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("SimpleClientHandler.channelRead");
        ByteBuf result = (ByteBuf) msg;
        byte[] result1 = new byte[result.readableBytes()];
        result.readBytes(result1);
        System.out.println("Server said:" + new String(result1));
        result.release();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }


    // 连接成功后，向server发送消息
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String msg = "hello Server!";
        ByteBuf encoded = ctx.alloc().buffer(4 * msg.length());
        encoded.writeBytes(msg.getBytes());
        ctx.write(encoded);
        ctx.flush();
    }
}
