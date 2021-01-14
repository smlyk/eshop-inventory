package com.smlyk.eshopinventory.controller;


import com.smlyk.eshopinventory.dto.Result;
import com.smlyk.eshopinventory.model.ProductInventory;
import com.smlyk.eshopinventory.request.ProductInventoryCacheRefreshRequest;
import com.smlyk.eshopinventory.request.ProductInventoryDBUpdateRequest;
import com.smlyk.eshopinventory.request.Request;
import com.smlyk.eshopinventory.service.IProductInventoryService;
import com.smlyk.eshopinventory.service.RequestAsyncProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author always
 * @since 2021-01-02
 */
@RestController
@RequestMapping("/product-inventory")
@Slf4j
public class ProductInventoryController {

    @Autowired
    private RequestAsyncProcessService requestAsyncProcessService;

    @Autowired
    private IProductInventoryService productInventoryService;

    /**
     * 更新商品库存
     */
    @GetMapping("updateProductInventory")
    public Result updateProductInventory(ProductInventory productInventory){
       log.info("FC2594485C60: 接收到更新商品库存的请求，商品id=" + productInventory.getId() + ", 商品库存数量=" + productInventory.getInventoryCnt());

        try {
            Request request = new ProductInventoryDBUpdateRequest(productInventory, productInventoryService);
            requestAsyncProcessService.process(request);
        } catch (Exception e) {
            log.error("updateProductInventory error: {}", e);
            return new Result().fail();
        }
        return new Result().success(productInventory);
    }

    /**
     * 根据商品id获取商品库存信息
     * @param id
     * @return
     */
    @GetMapping("getProductInventory")
    public Result getProductInventory(Integer id) {
        log.info("FC2594485C60: 接收到一个商品库存的读请求，商品id=" + id);

        try {
            Request request = new ProductInventoryCacheRefreshRequest(
                    id, productInventoryService, false);
            requestAsyncProcessService.process(request);

            // 将请求扔给service异步去处理以后，就需要while(true)一会儿，在这里hang住
            // 去尝试等待前面有商品库存更新的操作，同时缓存刷新的操作，将最新的数据刷新到缓存中
            long startTime = System.currentTimeMillis();
            long endTime = 0L;
            long waitTime = 0L;

            while (true){
                // 等待超过200ms没有从缓存中获取到结果,就跳出循环，直接尝试从数据库中读取数据
//                if (waitTime > 200){
//                    break;
//                }
                //为方便测试时间写长一点
                if (waitTime > 25000){
                    break;
                }

                //先尝试从redis缓存读取商品库存信息
                ProductInventory productInventoryCache = productInventoryService.getProductInventoryCache(id);
                // 如果读取到了结果，那么就返回
                if(productInventoryCache != null) {
                    log.info("FC2594485C60: 在200ms内读取到了redis中的库存缓存，商品id=" + productInventoryCache.getId() + ", 商品库存数量=" + productInventoryCache.getInventoryCnt());
                    return new Result().success(productInventoryCache);
                }else {
                    //如果从缓存中没有获取到，等待20ms，然后继续再次从缓存中获取
                    Thread.sleep(20);
                    endTime = System.currentTimeMillis();
                    waitTime = endTime - startTime;;
                }
            }

            //200ms内没有从缓存中获取到，直接从数据库中获取
            // 代码会运行到这里，只有三种情况：
            // 1、就是说，上一次也是读请求，数据刷入了redis，但是redis LRU算法给清理掉了，标志位还是false
            // 所以此时下一个读请求是从缓存中拿不到数据的，再放一个读Request进队列，让数据去刷新一下
            // 2、可能在200ms内，就是读请求在队列中一直积压着，没有等待到它执行（在实际生产环境中，基本是比较坑了）
            // 所以就直接查一次库，然后给队列里塞进去一个刷新缓存的请求
            // 3、数据库里本身就没有，缓存穿透，穿透redis，请求到达mysql库
            ProductInventory productInventory = productInventoryService.findProductInventory(id);
            if(productInventory != null) {
                //为了解决第一种问题，需要强制刷新一下缓存
                request = new ProductInventoryCacheRefreshRequest(
                        id, productInventoryService, true);
                requestAsyncProcessService.process(request);

                return new Result().success(productInventory);
            }

        } catch (Exception e) {
           log.error("getProductInventory error: {}", e);
            return new Result().fail();
        }
        return new Result().success(null);
    }

}
