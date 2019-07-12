package com.licslan.sb2netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Component;
/**
 * @author LICSLAN
 * */
@Component
public class WSServer {

	/** 单例模式 */
	private static class SingletionWSServer {
		static final WSServer instance = new WSServer();
	}
	
	public static WSServer getInstance() {
		return SingletionWSServer.instance;
	}
	
	private EventLoopGroup mainGroup;
	private EventLoopGroup subGroup;
	private ServerBootstrap server;
	//private EventLoopGroup xorkGroup;
	private ChannelFuture future;
	
	public WSServer() {
		mainGroup = new NioEventLoopGroup();
		subGroup = new NioEventLoopGroup();
		server = new ServerBootstrap();
		//xorkGroup = new NioEventLoopGroup(100);
		server.group(mainGroup, subGroup)
			.channel(NioServerSocketChannel.class)
			.childHandler(new WSServerInitialzer())
			;
	}
	
	public void start() {
		try {
			this.future = server.bind(8088).sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.err.println("netty server 启动完毕... the port is 8088");
	}
}
