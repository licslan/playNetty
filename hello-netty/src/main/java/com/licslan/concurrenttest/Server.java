package com.licslan.concurrenttest;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author LICSLAN
 * */
public class Server {

    //该程序本地启动后  使用Jmeter 测试  吞吐量可以达到1000/sec

    //如果我们将这样的服务器器部署个上100台  甚至更多  并使用dockers K8S 落地devops 提高效率和生产力

    //根据业务情况如果加上MQ  负载均衡  合理设计和配置 将可以达到10万/秒的并发

    //想起来自己刚入行时  什么都不知道   通过不断积累和学习  现在知道的要多一些   需要一直学习

    //MySQL单库并发大概2000/秒  如何保证分布式事务 接口幂等都需要考虑  如今微服务大行其道  这些设计时都需要考虑

    //架构不断演技的同时  还是需要结合自己的实际业务场景  用户量来设计  而非技术跟风  为了微服务而微服务

    public static void main(String[] args) {
        //定义线程主从线程组

        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        //下面的线程池来处理handler里面的业务逻辑  生产环境需要根据业务不断来测试和调整
        //NioEventLoopGroup xorkGroup = new NioEventLoopGroup(160);


        // 启动辅助类

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //获取pipeline 管道
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        //pipeline 添加相关的handler
                        pipeline.addLast(new HttpServerCodec());
                        /*pipeline.addLast(xorkGroup,new ServerPlayHandler());*/
                        pipeline.addLast(new ServerPlayHandler());
                    }
                });



        try {
            //绑定端口
            /*ChannelFuture sync = bootstrap.bind(8088).sync();*/
            ChannelFuture sync = bootstrap.bind(8088);
            System.err.println("bind success in port: 8088");
            //监听关闭channel  同步
            sync.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }


    }



}
