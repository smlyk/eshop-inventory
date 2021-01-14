package com.smlyk.eshopinventory.service;

import com.smlyk.eshopinventory.model.ProductInventory;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author always
 * @since 2021-01-02
 */
public interface IProductInventoryService extends IService<ProductInventory> {

    /**
     * 更新商品库存
     * @param productInventory 商品库存
     */
    void updateProductInventory(ProductInventory productInventory);

    /**
     * 删除Redis中的商品库存的缓存
     * @param productInventory 商品库存
     */
    void removeProductInventoryCache(ProductInventory productInventory);

    /**
     * 根据商品id查询商品库存
     * @param id 商品id
     * @return 商品库存
     */
    ProductInventory findProductInventory(Integer id);

    /**
     * 设置商品库存的缓存
     * @param productInventory 商品库存
     */
    void setProductInventoryCache(ProductInventory productInventory);

    /**
     * 从redis缓存中获取库存信息
     * @param id
     * @return
     */
    ProductInventory getProductInventoryCache(Integer id);

}
