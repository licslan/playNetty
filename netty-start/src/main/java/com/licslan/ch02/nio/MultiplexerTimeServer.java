package com.licslan.ch02.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * @author LICSLAN
 * JDK nio 编程
 * */
@Slf4j
public class MultiplexerTimeServer implements Runnable {
    //多路复用选择器

    private Selector selector;

    private volatile  boolean stop;

    //初始化多路复用选择器 绑定监听端口

    MultiplexerTimeServer(int port){

        try {
            selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port),1024);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            log.info("The time server is start in port "+port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop(){
        this.stop=true;
    }



    @Override
    public void run() {
        //开启多路复用
        while (!stop){
            try {
                //1秒钟轮询一次  监测key  interestOps
                selector.select(1000);
                Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeySet.iterator();
                SelectionKey key;
                while (it.hasNext()){
                    key=it.next();
                    it.remove();
                    try {
                        handleInput(key);
                    }catch (Exception e){
                        if(key!=null){
                            key.cancel();
                            if(key.channel()!=null){
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //多路复用器关闭后  所有注册上面的channel和pipe等资源都会被
        //自动去注册并关闭  所以不需要重复释放资源
        if(selector!=null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) {

        if(key.isValid()){
            //处理新接入的请求消息
            if(key.isAcceptable()){
                //acceptable new connection
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
                try {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    //add new connection to selector
                    socketChannel.register(selector,SelectionKey.OP_READ);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 可读
            if(key.isReadable()){
                //read the data  读客户端发送过来的数据
                SocketChannel socketChannel = (SocketChannel)key.channel();
                //设置存放数据缓冲区大小
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                try {
                    //将channel的数据读到缓冲区中
                    int readBytes = socketChannel.read(readBuffer);
                    if(readBytes>0){
                        readBuffer.flip();
                        byte[] bytes = new byte[readBuffer.remaining()];
                        readBuffer.get(bytes);
                        String body = new String(bytes,"UTF-8");
                        log.info("The time server receive order: "+body);
                        String currentTime = "Query time order".equalsIgnoreCase(body)
                                ?new Date(System.currentTimeMillis()).toString()
                                :"bad order";
                        //写数据到客户端
                        doWrite(socketChannel,currentTime);
                    }else if(readBytes<0){
                        key.cancel();
                        //对端链路关闭 客户端socketChannel关闭
                        socketChannel.close();
                    }else {
                        //读到0字节 忽略不做任何操作
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }


    }

    //写数据

    private void doWrite(SocketChannel socketChannel, String response) {
        if(response!=null&&response.trim().length()>0){
            try {
                byte[] bytes = response.getBytes();
                ByteBuffer wirteBuffer = ByteBuffer.allocate(bytes.length);
                wirteBuffer.put(bytes);
                wirteBuffer.flip();
                socketChannel.write(wirteBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
