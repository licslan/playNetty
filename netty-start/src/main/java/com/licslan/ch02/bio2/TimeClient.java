package com.licslan.ch02.bio2;

import java.io.*;
import java.net.Socket;

/**
 * @author LICSLAN
 * 伪异步IO 将
 * 一请求一连接模式改  请求到了服务端会被封装了task投递到线程池
 * 不用每一个请求都创建一个线程
 * */
public class TimeClient {

    public static void main(String[] args) {
        Socket socket = null;
        //输入流
        BufferedReader in = null;
        //输出流
        PrintWriter out = null;
        try {
            //绑定socket
            socket = new Socket("127.0.0.1",8080);
            //读取服务端发送过来的信息 （输入流）
            in =new  BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            //客户端发送消息到服务端
            out.println("hello licslan");
            //打印从服务端接收到的信息
            String s = in.readLine();
            System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                assert socket != null;
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                assert in != null;
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
                assert out != null;
                out.close();
        }
    }
}
