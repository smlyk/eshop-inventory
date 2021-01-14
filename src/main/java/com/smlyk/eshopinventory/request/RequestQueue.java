package com.smlyk.eshopinventory.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 请求的内存队列
 * @Author: always
 * @Date: 2021/1/2 10:30 上午
 */
public class RequestQueue {

    /**
     * 内存队列集合
     */
    private List<ArrayBlockingQueue<Request>> queues = new ArrayList<>();

    /**
     * 标识位map
     */
    private Map<Integer, Boolean> flagMap = new ConcurrentHashMap<>();

    public Map<Integer, Boolean> getFlagMap() {
        return flagMap;
    }

    private RequestQueue(){

    }

    public static RequestQueue getInstance(){
        return Singleton.getInstance();
    }

    /**
     * 向内存队列集合中添加内存队列
     * @param queue
     */
    public void addRequestQueue(ArrayBlockingQueue<Request> queue){
        queues.add(queue);
    }

    private static class Singleton{

        private static RequestQueue instance;

        static {
            instance = new RequestQueue();
        }

        public static RequestQueue getInstance(){
            return instance;
        }

    }

    /**
     * 获取内存队列集合的大小
     * @return
     */
    public Integer getQueueSize(){
        return queues.size();
    }

    /**
     * 获取内存队列
     * @param index
     * @return
     */
    public ArrayBlockingQueue<Request> getQueue(int index){
        return queues.get(index);
    }

}
