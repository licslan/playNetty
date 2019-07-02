package com.licslan.sb2netty;

import com.alibaba.fastjson.JSON;
import com.licslan.dbdao.MessageDao;
import com.licslan.utils.SpringUtil;
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
import org.springframework.stereotype.Component;

import java.net.SocketAddress;

/**
 * 
 * @Description: 处理消息的handler
 * TextWebSocketFrame： 在netty中，是用于为websocket专门处理文本的对象，frame是消息的载体
 */
@Slf4j
@Component
public class ChatHandler extends SimpleChannelInboundHandler<HttpObject> {

//	@Autowired
//	private MessageDaoImpl messageDaoImpl;
	/*上面的方式不行  需要手动获取bean*/


	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpObject o)
			{
//		// 获取客户端传输过来的消息
//		String response = msg.toString();
//		ByteBuf encoded = ctx.alloc().buffer(4 * response.length());
//		encoded.writeBytes(response.getBytes());
//		ctx.write(encoded);
//		ctx.flush();


				//获取channel
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
					//手动获取bean
					MessageDao messageDaoImpl = (MessageDao)SpringUtil.getBean("messageDaoImpl");
					log.info("the messageDaoImpl is "+messageDaoImpl);
					if(null!=messageDaoImpl.getList()){
						 byteBuf = Unpooled.copiedBuffer(JSON.toJSONString(messageDaoImpl.getList()), CharsetUtil.UTF_8);
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
}
