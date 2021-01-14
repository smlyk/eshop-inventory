package com.smlyk.eshopinventory.request;

/**
 * 请求的接口类(具体实现有更新请求，读取请求等)
 * @Author: always
 * @Date: 2021/1/2 10:31 上午
 */
public interface Request {

    void process();

    Integer getProductId();

    boolean isForceRefresh();
}
