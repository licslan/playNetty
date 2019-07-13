package com.licslan.ch02.bio2test;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;

/**
 * @author LICSLAN
 */
@Slf4j
public class TimeClient {
    public static void main(String[] args){
        int size =10000;
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        //发送10次请求
        for (int i=0;i<size;i++){
            try{
                    socket = new Socket("127.0.0.1",8080);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
                    out.println("LICSLAN");
                    System.out.println(" send order to server succeed");
                    String resp = in.readLine();
                    System.out.println("服务器处理的请求次数是第 "+i+1+" 次， The message from server is "+resp);
            }catch (Exception e){
                System.err.println("============系统负载已达上限=============");
                //退出系统
                System.exit(0);
            }finally {
                if(socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(out != null){
                    out.close();
                }
                if(in!=null){
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
