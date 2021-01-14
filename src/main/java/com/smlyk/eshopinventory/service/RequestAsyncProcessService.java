package com.smlyk.eshopinventory.service;

import com.smlyk.eshopinventory.request.Request;

/**
 * 请求异步执行的Service
 * @Author: always
 * @Date: 2021/1/2 4:58 下午
 */
public interface RequestAsyncProcessService {

    void process(Request request);

}
