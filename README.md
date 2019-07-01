## playNetty

### 本项目暂时分为4个模块吧  
###### 一.hello-netty 主要介绍netty一些简单的使用
###### 二.netty-im 主要是即时聊天工具的springboot项目
###### 三.netty-server 主要是介绍客户端连接的项目 主要有websocket http协议的请求 以及测试百万连接的测试
###### 四.netty-client 主要是客户端发送百万连接模拟请求项目





_介绍一下Netty Rector模型的三种形式_
_无论是C++还是Java编写的网络框架，大多数都是基于Reactor模式进行设计和开发，Reactor模式基于事件驱动，特别适合处理海量的I/O事件_<br>
#### 1. Reactor三种线程模型
##### 1.1. 单线程模型
##### Reactor单线程模型，指的是所有的IO操作都在同一个NIO线程上面完成，NIO线程的职责如下：
###### 1）作为NIO服务端，接收客户端的TCP连接；
###### 2）作为NIO客户端，向服务端发起TCP连接；
###### 3）读取通信对端的请求或者应答消息；
###### 4）向通信对端发送消息请求或者应答消息。
###### Reactor单线程模型示意图如下所示：
![单线程模式](https://github.com/licslan/playNetty/raw/master/images/netty1.png)



