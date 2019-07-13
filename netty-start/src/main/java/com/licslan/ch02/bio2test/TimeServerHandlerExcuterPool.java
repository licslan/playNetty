package com.licslan.ch02.bio2test;

import lombok.Data;

import java.util.concurrent.*;

/**
 * @author LICSLAN
 */
@Data
class TimeServerHandlerExcuterPool {

    private int maxPoolSize;
    private int queueSize;
    private ExecutorService executorService;

    TimeServerHandlerExcuterPool(int maxPoolSize, int queueSize) {
        this.maxPoolSize = maxPoolSize;
        this.queueSize = queueSize;
        executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),maxPoolSize,120L, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(queueSize));
    }

    void executeTask(Runnable task){
        executorService.execute(task);
    }
}
