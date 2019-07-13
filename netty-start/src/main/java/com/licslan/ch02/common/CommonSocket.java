package com.licslan.ch02.common;

import java.io.*;
import java.net.Socket;
import java.util.Date;
/**
 * @author LICSLAN
 * 重构代码 抽出通用部分
 * */
public class CommonSocket {
    public void useSocket(Socket socket){
        BufferedReader in = null;
        PrintWriter out = null;
        try{
            //读取输入流
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //获取输出流
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
            String currentTime;
            String body;
            while(true){
                //循环读取客户端发送过来的输入流  客户端其实就是输出流内容
                body = in.readLine();
                if(body == null){
                    break;
                }
                System.out.println("The time server receive order :"+body);
                currentTime = "Query time order".equalsIgnoreCase(body)?new Date(System.currentTimeMillis()).toString():"bad order";
                //输出流输出内容
                //服务端输出流输出消息到客户端（就是一个socket通道其实  到了客户端端其实就被视作输入流）
                out.println(currentTime);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭流资源
            try {
                if(in!=null){in.close();}
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(out != null){
                out.close();
            }
            if(socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
