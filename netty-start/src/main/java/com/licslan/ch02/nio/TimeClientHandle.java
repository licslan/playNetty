package com.licslan.ch02.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author LICSLAN
 * nio jdk 编程
 * */
@Slf4j
public class TimeClientHandle implements Runnable {

    private int port;
    private String host;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean stop;

    TimeClientHandle(String host,int port){
        this.host=host==null?"127.0.0.1":host;
        this.port=port;
        try {
            selector=Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    @Override
    public void run() {

        try {
            doConnect();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        while (!stop){
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeySet.iterator();
                SelectionKey key;
                while (iterator.hasNext()){
                    key= iterator.next();
                    iterator.remove();
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
            }catch (Exception e){
                e.printStackTrace();
                System.exit(1);
            }
        }
        //多路复用器关闭后所有注册在上面的
        // channel和pipe等资源都会被自动去注册并关闭 所以不需要重复释放资源

        if(selector!=null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }

    private void doConnect() throws IOException{
        //如果直连成功则注册到多路复用选择器 发送请求消息 读应答
        if(socketChannel.connect(new InetSocketAddress(host,port))){
            //可读
            socketChannel.register(selector,SelectionKey.OP_READ);
            //写入消息到服务端
            doWirte(socketChannel);
        }else {
            //可建立连接
            socketChannel.register(selector,SelectionKey.OP_CONNECT);
        }

    }

    private void doWirte(SocketChannel sc) throws IOException{

        byte[] req ="QUERY TIME ORDER".getBytes();
        ByteBuffer wirteBuffer = ByteBuffer.allocate(req.length);
        wirteBuffer.flip();
        sc.write(wirteBuffer);
        if(!wirteBuffer.hasRemaining()){
            log.info("Send order 2 Server succeed");
        }

    }

    private void handleInput(SelectionKey key) throws IOException{

        if(key.isValid()){
            //判断是否连接成功
            SocketChannel sc = (SocketChannel)key.channel();
            if(key.isConnectable()){
                if(sc.finishConnect()){
                    sc.register(selector,SelectionKey.OP_READ);
                    doWirte(sc);
                }else {
                    //连接失败 进程退出
                    System.exit(1);
                }
            }

            if(key.isReadable()){
                ByteBuffer readBuffer= ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                if(readBytes>0){
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes,"UTF-8");
                    log.info("NOW IS :"+body);
                    this.stop=true;
                }else if(readBytes<0){
                    //对端链路关闭
                    key.cancel();
                    sc.close();
                }else {
                    //读到0字节 忽略
                }
            }
        }

    }
}
