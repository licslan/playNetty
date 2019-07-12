package com.licslan.ch02.bio2;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author LICSLAN
 * 创建一个线程池
 * */
@Data
class ServerHandlerExcuterPool {

    //最大线程数

    private int maxPoolSize;
    //队列大小

    private int queueSize;
    //线程池service

    private ExecutorService executorService;

    //构造方法

    ServerHandlerExcuterPool(int maxPoolSize, int queueSize){
        this.maxPoolSize=maxPoolSize;
        this.queueSize = queueSize;

        executorService = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(),
                maxPoolSize,
                120L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(queueSize));
    }
    void excutorTask(Runnable task){
        executorService.submit(task);
    }


}
