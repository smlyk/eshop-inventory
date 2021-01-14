package com.smlyk.eshopinventory.request;

import com.smlyk.eshopinventory.model.ProductInventory;
import com.smlyk.eshopinventory.service.IProductInventoryService;

/**
 * 数据更新请求
 *cache aside pattern
 * （1）删除缓存
 * （2）更新数据库
 * @Author: always
 * @Date: 2021/1/2 11:00 上午
 */
public class ProductInventoryDBUpdateRequest implements Request {

    //商品库存信息
    private ProductInventory productInventory;

    //商品库存Service
    private IProductInventoryService productInventoryService;

    public ProductInventoryDBUpdateRequest(ProductInventory productInventory, IProductInventoryService productInventoryService){
        this.productInventory = productInventory;
        this.productInventoryService = productInventoryService;
    }

    @Override
    public void process() {

        System.out.println("FC2594485C60: 数据库更新请求开始执行，商品id=" + productInventory.getId() + ", 商品库存数量=" + productInventory.getInventoryCnt());

        //删除redis中的缓存
        productInventoryService.removeProductInventoryCache(productInventory);

        //方便测试睡眠一段时间
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //更新数据库中的库存
        productInventoryService.updateProductInventory(productInventory);
        System.out.println("FC2594485C60: 数据库更新执行成功，商品id=" + productInventory.getId() + ", 商品库存数量=" + productInventory.getInventoryCnt());
    }

    @Override
    public Integer getProductId() {
        return productInventory.getId();
    }

    @Override
    public boolean isForceRefresh() {
        return false;
    }
}
