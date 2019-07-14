package com.licslan.ch02.nio;

/**
 * @author LICSLAN  nio编程模型
 * TODO  有些问题  没有接收到消息
 * */
public class TimeServer {
    public static void main(String[] args) {
        int port=8080;
        if (args!=null&&args.length>0){
            try {
                port= Integer.valueOf(args[0]);
            }catch (NumberFormatException ignored){
            }
    }
        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
        new Thread(timeServer,"nio-Thread").start();
    }

}
