package com.smlyk.eshopinventory.request;

import com.smlyk.eshopinventory.model.ProductInventory;
import com.smlyk.eshopinventory.service.IProductInventoryService;

/**
 * @Author: always
 * @Date: 2021/1/2 11:00 上午
 */
public class ProductInventoryCacheRefreshRequest implements Request {

    //商品id
    private Integer id;

    //商品库存Service
    private IProductInventoryService productInventoryService;

    //是否强制刷新缓存
    private boolean isForceRefresh;

    public ProductInventoryCacheRefreshRequest(Integer id, IProductInventoryService productInventoryService, boolean isForceRefresh){
        this.id = id;
        this.productInventoryService = productInventoryService;
        this.isForceRefresh = isForceRefresh;
    }

    @Override
    public void process() {

        // 从数据库中查询最新的商品库存数量
        ProductInventory productInventory = productInventoryService.findProductInventory(id);
        System.out.println("FC2594485C60: 已查询到商品最新的库存数量，商品id=" + id + ", 商品库存数量=" + productInventory.getInventoryCnt());
        // 将最新的商品库存数量，刷新到redis缓存中去
        productInventoryService.setProductInventoryCache(productInventory);

    }

    @Override
    public Integer getProductId() {
        return id;
    }

    @Override
    public boolean isForceRefresh() {
        return isForceRefresh;
    }
}
