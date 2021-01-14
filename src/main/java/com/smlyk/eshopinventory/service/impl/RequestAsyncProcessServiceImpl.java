package com.smlyk.eshopinventory.service.impl;

import com.smlyk.eshopinventory.request.ProductInventoryCacheRefreshRequest;
import com.smlyk.eshopinventory.request.ProductInventoryDBUpdateRequest;
import com.smlyk.eshopinventory.request.Request;
import com.smlyk.eshopinventory.request.RequestQueue;
import com.smlyk.eshopinventory.service.RequestAsyncProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @Author: always
 * @Date: 2021/1/2 5:00 下午
 */
@Service
@Slf4j
public class RequestAsyncProcessServiceImpl implements RequestAsyncProcessService {

    @Override
    public void process(Request request) {

        try {
            //做请求的路由，根据每个请求的商品id，路由到对应的内存队列中
            ArrayBlockingQueue<Request> queue = getRountingQueue(request.getProductId());
            //将请求放入到内存队列中
            queue.put(request);
        } catch (InterruptedException e) {
            log.error("请求异步执行error：{}", e);
        }
    }

    /**
     * 根据商品id，获取路由的内存队列
     * @param productId
     * @return
     */
    private ArrayBlockingQueue<Request> getRountingQueue(Integer productId) {

        RequestQueue requestQueue = RequestQueue.getInstance();

        //现获取商品id的hash值
        String key = String.valueOf(productId);
        int h;
        int hash = (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);

        // 对hash值取模，将hash值路由到指定的内存队列中，比如内存队列大小8
        // 用内存队列的数量对hash值取模之后，结果一定是在0~7之间
        // 所以任何一个商品id都会被固定路由到同样的一个内存队列中去的
        int index = (requestQueue.getQueueSize() - 1) & hash;
        log.info("FC2594485C60: 路由内存队列，商品id=" + productId + ", 队列索引=" + index);

        return requestQueue. getQueue(index);
    }
}
