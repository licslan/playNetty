package com.licslan.hello;

public class HelloNettyServer {

    public static void main(String[] args) {

        //使用netty 官方推荐的Recto模型第三种方式  主从线程组模型

        /*1.1. 单线程模型
        Reactor单线程模型，指的是所有的IO操作都在同一个NIO线程上面完成，NIO线程的职责如下：
        1）作为NIO服务端，接收客户端的TCP连接；
        2）作为NIO客户端，向服务端发起TCP连接；
        3）读取通信对端的请求或者应答消息；
        4）向通信对端发送消息请求或者应答消息。
        由于Reactor模式使用的是异步非阻塞IO，所有的IO操作都不会导致阻塞，理论上一个线程可以独立处理所有IO相关的操作。从架构层面看，一个NIO线程确实可以完成其承担的职责。例如，通过Acceptor类接收客户端的TCP连接请求消息，链路建立成功之后，通过Dispatch将对应的ByteBuffer派发到指定的Handler上进行消息解码。用户线程可以通过消息编码通过NIO线程将消息发送给客户端。
        对于一些小容量应用场景，可以使用单线程模型。但是对于高负载、大并发的应用场景却不合适，主要原因如下：
        1）一个NIO线程同时处理成百上千的链路，性能上无法支撑，即便NIO线程的CPU负荷达到100%，也无法满足海量消息的编码、解码、读取和发送；
        2）当NIO线程负载过重之后，处理速度将变慢，这会导致大量客户端连接超时，超时之后往往会进行重发，这更加重了NIO线程的负载，最终会导致大量消息积压和处理超时，成为系统的性能瓶颈；
        3）可靠性问题：一旦NIO线程意外跑飞，或者进入死循环，会导致整个系统通信模块不可用，不能接收和处理外部消息，造成节点故障。
        为了解决这些问题，演进出了Reactor多线程模型，如下。
        */














    }




}
