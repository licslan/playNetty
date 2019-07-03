package com.licslan.sb2netty;

import com.alibaba.fastjson.JSON;
import com.licslan.dbdao.Hobby;
import com.licslan.dbdao.MessageDaoImpl;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.SocketAddress;
import java.util.List;

/**
 * @author LICSLAN
 * 处理消息的handler
 */
@Slf4j
@Component
public class ChatHandler extends SimpleChannelInboundHandler<HttpObject> {

	/**
	 *   首先目前我写的项目是springboot+netty，在接收客户端传输的数据时调用service一直报null指针异常
	 *   刚开始没有想到是service无法实例化的问题，一直在测试数据方面的问题，后来去群里讨论才知道问题所在
	 *   我这里讲的netty接收数据的handler类，但是基本都大同小异
	 *   如果我们直接在一个不是controller类的里面注入@Autowired的时候，而且还去调用就会报null指针
	 *	 首先需要了解两个注解 @Component  跟 @PostConstruct
	 *   spring注解中@component就是说把这个类交给Spring管理，因为不清楚这个类是属于哪个层面，所以就用@Component。
	 *   PostContruct是spring框架的注解，在方法上加该注解会在项目启动的时候执行该方法，也可以理解为在spring容器初始化的时候执行该方法。
	 * */

	private static ChatHandler chatHandler;

	//1.正常注入[记得主类也需要使用@Component注解]

	@Autowired
	private MessageDaoImpl messageDaoImpl;

	//2.初始化构造方法一定要有

	public ChatHandler() {

	}

	//3.容器初始化的时候进行执行-这里是重点

	@PostConstruct
	public void init() {
		chatHandler = this;
		chatHandler.messageDaoImpl = this.messageDaoImpl;
	}



	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpObject o)
			{
				Channel channel = ctx.channel();
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
					ByteBuf byteBuf;

					/*手动获取bean  后面待学习...*/
					/*MessageDao messageDaoImpl = (MessageDao)SpringUtil.getBean("messageDaoImpl");*/
					/*log.info("the messageDaoImpl is "+messageDaoImpl);*/

					List<Hobby> list = chatHandler.messageDaoImpl.getList();
					log.info(JSON.toJSONString(list));
					if(list!=null){
						 byteBuf = Unpooled.copiedBuffer(JSON.toJSONString(list), CharsetUtil.UTF_8);
					}else {
						Lerson person = new Lerson();
						person.setAge(28);
						person.setName("LICSLAN");
						 byteBuf = Unpooled.copiedBuffer(JSON.toJSONString(person), CharsetUtil.UTF_8);
					}

					// 构建一个http response
					FullHttpResponse response =
							new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
									HttpResponseStatus.OK,
									byteBuf);

					/* response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");*/
					response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
					response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");

					// 把响应刷到客户端
					ctx.writeAndFlush(response);

				}

	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// 当出现异常就关闭连接
		log.info("someting was wrong plz check it !!");
		cause.printStackTrace();
		ctx.close();
	}
	
}
@Data
@Component
class Lerson{
	private String name;
	private int age;
	//TextWebSocketFrame： 在netty中，是用于为websocket专门处理文本的对象，frame是消息的载体
}
