package com.smlyk.eshopinventory.thread;

import com.smlyk.eshopinventory.request.Request;
import com.smlyk.eshopinventory.request.RequestQueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 单例：请求处理线程池
 * @Author: always
 * @Date: 2021/1/2 10:10 上午
 */
public class RequestProcessorThreadPool {

    //创建一个线程池
    private ExecutorService threadPool = Executors.newFixedThreadPool(10);
    
    private RequestProcessorThreadPool(){
        //初始化时，创建好内存队列
        RequestQueue requestQueue = RequestQueue.getInstance();
        //创建10个内存队列(每个内存队列大小为100),放到内存队列集合中
        for (int i = 0;i<1; i++){
            ArrayBlockingQueue<Request> queue = new ArrayBlockingQueue<>(100);
            requestQueue.addRequestQueue(queue);

            //将内存队列绑定到线程，并提交线程
            threadPool.submit(new RequestProcessorThread(queue));
        }
    }

    /**
     * jvm的机制去保证多线程并发安全
     * 内部类的初始化，一定只会发生一次
     * @return
     */
    public static RequestProcessorThreadPool getInstance(){
        return Singleton.getInstance();
    }

    /**
     * 静态内部类实现单例
     */
    private static class Singleton{

        private static RequestProcessorThreadPool instance;

        static {
            instance = new RequestProcessorThreadPool();
        }

        public static RequestProcessorThreadPool getInstance(){
            return instance;
        }
    }

    public static void init(){
        RequestProcessorThreadPool.getInstance();
    }

}
