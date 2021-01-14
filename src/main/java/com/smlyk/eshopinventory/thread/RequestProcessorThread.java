package com.smlyk.eshopinventory.thread;

import com.smlyk.eshopinventory.request.ProductInventoryCacheRefreshRequest;
import com.smlyk.eshopinventory.request.ProductInventoryDBUpdateRequest;
import com.smlyk.eshopinventory.request.Request;
import com.smlyk.eshopinventory.request.RequestQueue;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;

/**
 * 执行请求的工作线程
 * @Author: always
 * @Date: 2021/1/2 10:51 上午
 */
public class RequestProcessorThread implements Runnable {

    /**
     * 自己监控的内存队列
     */
    private ArrayBlockingQueue<Request> queue;

    public RequestProcessorThread(ArrayBlockingQueue<Request> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true){
                //阻塞式的从队列中获取请求
                Request request = queue.take();

                //不是强制刷新缓存的读操作情况下
                if (!request.isForceRefresh()){
                    // 先做读请求的去重
                    RequestQueue requestQueue = RequestQueue.getInstance();
                    Map<Integer, Boolean> flagMap = requestQueue.getFlagMap();

                    if (request instanceof ProductInventoryDBUpdateRequest){
                        // 如果是一个更新数据库的请求，那么就将那个productId对应的标识设置为true
                        flagMap.put(request.getProductId(), true);
                    }else if (request instanceof ProductInventoryCacheRefreshRequest){
                        //判断flag的值，来决定是否执行请求操作
                        Boolean flag = flagMap.get(request.getProductId());

                        if (flag == null){
                            //第一次读请求进来，将读请求放入内存队列，继续执行读请求
                            flagMap.put(request.getProductId(), false);
                        }

                        // 如果是缓存刷新的请求，那么就判断，如果标识不为空，而且是true，就说明之前有一个这个商品的数据库更新请求
                        if(flag != null && flag) {
                            //有过更新操作，将读请求放入内存队列，继续执行读请求
                            flagMap.put(request.getProductId(), false);
                        }

                        // 如果是缓存刷新的请求，而且发现标识不为空，但是标识是false
                        // 说明前面已经有一个数据库更新请求+一个缓存刷新请求了，直接过滤掉这个请求
                        if(flag != null && !flag) {
                            // 对于这种读请求，直接就过滤掉，不要放到后面的内存队列里面去了
                            continue;
                        }
                    }
                }

                //执行请求操作
                System.out.println("FC2594485C60: 工作线程处理请求，商品id=" + request.getProductId());
                request.process();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
