package com.licslan.ch02.bio2;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author LICSLAN
 * 时间服务端
 * */
@Slf4j
public class TimeServer {
    public static void main(String[] args) {
        int port = 8080;
        if(args != null && args.length >0 ){
            try{
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException ignored){

            }
        }
        ServerSocket serverSocket = null;
        Socket socket;
        try {

            ServerHandlerExcuterPool serverHandlerExcuterPool = new ServerHandlerExcuterPool(10,10);
            serverSocket = new ServerSocket(port);
            log.info("the time server is start in port :"+port);
            //不断循环
            while (true){
                socket = serverSocket.accept();
                /*之前是一个请求开启一个线程
                new Thread(new ServerHandler()).start();*/
                /*这里改造成线程池的方式*/
                serverHandlerExcuterPool.excutorTask(new ServerHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                assert serverSocket != null;
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
