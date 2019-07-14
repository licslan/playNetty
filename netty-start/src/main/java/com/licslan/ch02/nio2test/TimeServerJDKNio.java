package com.licslan.ch02.nio2test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author LICSLAN
 * 简化代码  nio编程  是不是有点复杂呀
 * */
@Slf4j
public class TimeServerJDKNio {

    private  boolean stop;
    private Selector selector = null;

    public static void main(String[] args){

        new TimeServerJDKNio().start();
    }

    private void start(){
        ServerSocketChannel acceptorSvr;
        try {
            int port = 8080;
            acceptorSvr = ServerSocketChannel.open();
            acceptorSvr.configureBlocking(false);
            acceptorSvr.bind(new InetSocketAddress("127.0.0.1",port));
            selector = Selector.open();
            acceptorSvr.register(selector,SelectionKey.OP_ACCEPT);
            log.info("Timeserver start!!");

            SelectionKey key;
            while(!stop){
                selector.select();
                Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                Iterator<SelectionKey>  iterator = selectionKeySet.iterator();
                while(iterator.hasNext()){
                    key = iterator.next();
                    iterator.remove();
                    try{
                        handleInput(key);
                    }catch (Exception e){
                        if(key != null){
                            key.cancel();
                            if(key.channel() !=null){
                                key.channel().close();
                            }
                        }
                    }
                }

            }
            if(selector !=null){
                try{
                    selector.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private  void handleInput(SelectionKey key) throws IOException{
        if(key.isValid()){
            if(key.isAcceptable()){
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                sc.register(selector,SelectionKey.OP_READ);
            }
            if(key.isReadable()){
                SocketChannel sc = (SocketChannel)key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                if(readBytes>0){
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes,"UTF-8");
                    log.info("the time server receive order:"+body);
                    doWrite(sc,"the time server receive order:"+body);
                }else if(readBytes <0){
                    key.cancel();
                    sc.close();
                }
            }
        }
    }

    private void doWrite(SocketChannel sc, String response) throws IOException{
        if(!StringUtils.isEmpty(response)){
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            byteBuffer.put(response.getBytes());
            byteBuffer.flip();
            sc.write(byteBuffer);
            log.info("send to client:"+response);
        }
    }

    public void stop(){
        this.stop = true;
    }


}
