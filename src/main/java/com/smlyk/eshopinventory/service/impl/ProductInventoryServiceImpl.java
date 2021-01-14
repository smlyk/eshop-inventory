package com.smlyk.eshopinventory.service.impl;

import com.alibaba.fastjson.JSON;
import com.smlyk.eshopinventory.model.ProductInventory;
import com.smlyk.eshopinventory.mapper.ProductInventoryMapper;
import com.smlyk.eshopinventory.service.IProductInventoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.Serializable;

import static java.util.Objects.nonNull;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author always
 * @since 2021-01-02
 */
@Service
public class ProductInventoryServiceImpl extends ServiceImpl<ProductInventoryMapper, ProductInventory>
        implements IProductInventoryService {

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @Override
    public void updateProductInventory(ProductInventory productInventory) {
        baseMapper.updateById(productInventory);
    }

    @Override
    public void removeProductInventoryCache(ProductInventory productInventory) {
        String key = "product:inventory:" + productInventory.getId();
        redisTemplate.delete(key);
    }

    @Override
    public ProductInventory findProductInventory(Integer id) {
        return baseMapper.selectById(id);
    }

    @Override
    public void setProductInventoryCache(ProductInventory productInventory) {
        String key = "product:inventory:" + productInventory.getId();
        redisTemplate.opsForValue().set(key, productInventory);
    }

    @Override
    public ProductInventory getProductInventoryCache(Integer id) {
        String key = "product:inventory:" + id;
        if (nonNull(redisTemplate.opsForValue().get(key))){
            ProductInventory productInventory = (ProductInventory) redisTemplate.opsForValue().get(key);
            return productInventory;
        }
       return null;
    }
}
