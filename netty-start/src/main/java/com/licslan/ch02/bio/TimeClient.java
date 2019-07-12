package com.licslan.ch02.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author LICSLAN
 */
public class TimeClient {
    public static void main(String[] args){
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try{
            socket = new Socket("127.0.0.1",8080);
            //读取服务端发送过来的输入流内容
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //获取输出流对象
            out = new PrintWriter(socket.getOutputStream(),true);

            //客户端输出流输出消息到服务端（就是一个socket通道其实  到了服务端其实就被视作输入流）
            out.println("Query time order");
            System.out.println(" send order to server succeed");
            String resp = in.readLine();
            System.out.println(resp);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(socket != null){
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if(out != null){
                out.close();
            }
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
}
