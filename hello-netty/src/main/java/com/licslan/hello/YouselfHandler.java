package com.licslan.hello;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;

/**
 * 添加自定义的助手类
 * @author LICSLAN
 * */
@Slf4j
public class YouselfHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject o){
        //获取channel
        Channel channel = channelHandlerContext.channel();

        //如果是http请求
        if(o instanceof HttpRequest){
            SocketAddress socketAddress = channel.localAddress();
            log.info("The socketAddress is ======>"+socketAddress);
            String uri = ((HttpRequest) o).uri();
            log.info("The URL is ======>"+uri);
            DecoderResult decoderResult = o.decoderResult();
            log.info("The decoderResult is ======>"+decoderResult);
            HttpHeaders headers = ((HttpRequest) o).headers();
            log.info("The headers is ======>"+headers);
            HttpMethod method = ((HttpRequest) o).method();
            log.info("The http method is =========>"+method.name());
            //显示客户端IP地址
            log.info("远程地址是=====>  "+channel.remoteAddress());
            //定义发送的数据消息
            person person = new person();
            person.setAge(28);
            person.setName("LICSLAN");

            //TODO 这里可以涉及  http 请求第三方应用  比如
            //TODO springcloud的Feign 或者查询数据之类  也可以在外面再加入自己的yourselfhandler 来处理相关逻辑

            ByteBuf byteBuf = Unpooled.copiedBuffer(JSON.toJSONString(person), CharsetUtil.UTF_8);
            // 构建一个http response
            FullHttpResponse response =
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                            HttpResponseStatus.OK,
                            byteBuf);
            // 为响应增加数据类型和长度

            /* response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");*/

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
            // 把响应刷到客户端
            channelHandlerContext.writeAndFlush(response);

        }
    }

    /**
     * HttpServletRequest httpRequest=(HttpServletRequest)request;
     *
     * String strBackUrl = "http://" + request.getServerName() //服务器地址
     *                     + ":"
     *                     + request.getServerPort()           //端口号
     *                     + httpRequest.getContextPath()      //项目名称
     *                     + httpRequest.getServletPath()      //请求页面或其他地址
     *                 + "?" + (httpRequest.getQueryString()); //参数
     *
     *
     * InetAddress address = InetAddress.getLocalHost();//获取的是本地的IP地址
     * String hostAddress = address.getHostAddress();
     *
     *
     * request.getSession().getServletContext().getRealPath("/");获取项目所在服务器的全路径，如：D:\Program Files\apache-tomcat-7.0.25\webapps\TestSytem\
     * request.getServletPath()    获取客户端请求的路径名，如：/object/delObject
     * request.getServerName()     获取服务器地址，如：localhost
     * request.getServerPort()     获取服务器端口，如8080
     * request.getContextPath()    获取项目名称，如：TestSytem
     * request.getLocalAddr()      获取本地地址，如：127.0.0.1
     * request.getLocalName()      获取本地IP映射名，如：localhost
     * request.getLocalPort()      获取本地端口，如：8090
     * request.getRealPath("/")    获取项目所在服务器的全路径，如：D:\Program Files\apache-tomcat-7.0.25\webapps\TestSytem\
     * request.getRemoteAddr()     获取远程主机地址，如：127.0.0.1
     * request.getRemoteHost()     获取远程主机，如：127.0.0.1
     * request.getRemotePort()     获取远程客户端端口，如：3623
     * request.getRequestedSessionId()      获取会话session的ID，如：823A6BACAC64FB114235CBFE85A46CAA
     * request.getRequestURI()     获取包含项目名称的请求路径，如：/TestSytem/object/delObject
     * request.getRequestURL().toString()   获取请求的全路径，如：http://localhost:8090/TestSytem/object/delObject
     *
     * */

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
class person{
    private String name;
    private int age;
}

