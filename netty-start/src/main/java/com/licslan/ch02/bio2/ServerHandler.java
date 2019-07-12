package com.licslan.ch02.bio2;

import com.licslan.ch02.common.CommonSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * @author LICSLAN
 * 创建线程池  投递任务到线程池处理
 * 不必一个请求就创建一个线程
 * */
public class ServerHandler implements Runnable{

    private Socket socket;

   //构造方法

   ServerHandler(Socket socket){
       this.socket= socket;
   }

    @Override
    public void run() {
        CommonSocket commonSocket = new CommonSocket();
        commonSocket.useSocket(socket);

        /*BufferedReader in = null;
        PrintWriter out = null;
        try{
            //读取输入流
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            //获取输出流
            out = new PrintWriter(this.socket.getOutputStream(),true);
            String currentTime = null;
            String body = null;
            while(true){
                //循环读取客户端发送过来的输入流  客户端其实就是输出流内容

                body = in.readLine();
                if(body == null){
                    break;
                }
                System.out.print("The time server receive order :"+body);
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
                out=null;
            }
            if(this.socket != null){
                try {
                    this.socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.socket = null;
            }
        }*/
    }
}
