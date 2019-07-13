package com.licslan.ch02.nio;
/**
 * @author LICSLAN
 * */
public class TimeClient {
    public static void main(String[] args) {
        int port =8080;
        if(args!=null&&args.length>0){
            try {
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        new Thread(new TimeClientHandle("127.0.0.1",port),"TimeClient-licslan").start();
    }
}
