package com.shop.stock.controller;

import com.shop.stock.entity.ProductStock;
import com.shop.stock.request.ProductStockCacheRefreshRequest;
import com.shop.stock.request.ProductStockDBUpdateRequest;
import com.shop.stock.request.Request;
import com.shop.stock.response.Response;
import com.shop.stock.service.ProductStockService;
import com.shop.stock.service.RequestAsyncProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductStockController {

    @Autowired
    private ProductStockService productStockService;
    @Autowired
    private RequestAsyncProcessService requestAsyncProcessService;

    /**
     * 更新商品库存
     */
    @PutMapping("/product/stock")
    public Response updateProductInventory(ProductStock productStock) {
        Response response = null;
        try {
            Request request = new ProductStockDBUpdateRequest(productStockService,productStock);
            requestAsyncProcessService.process(request);
            response = new Response(Response.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response(Response.FAIL);
        }

        return response;
    }

    @GetMapping("/product/{id}")
    public Response getProductStock(@PathVariable("id")Integer id){
        try {
            Request request = new ProductStockCacheRefreshRequest(productStockService,id,false);
            requestAsyncProcessService.process(request);

            long startTime = System.currentTimeMillis();
            long endTime = 0L;
            long waitTime = 0L;

            while (waitTime < 200){
                //尝试从缓存获取数据
                ProductStock productStock = productStockService.getProductStockCache(id);
                if(null != productStock){
                    return new Response(Response.SUCCESS,productStock);
                }
                try {
                    Thread.sleep(20);
                }catch (Exception e){
                    e.printStackTrace();
                }
                endTime = System.currentTimeMillis();
                waitTime = endTime - startTime;
            }
            //200毫秒内未获取到数据，从DB中获取并返回
            ProductStock productStock = productStockService.findProductStock(id);
            if(null != productStock){
                // 将缓存刷新一下，并放入队列串行处理
                // 为防止redis的LRU算法清理掉数据，导致上次的读请求的标志位在false上被过滤，这里需要强制刷新缓存
                request = new ProductStockCacheRefreshRequest(productStockService,id,true);
                requestAsyncProcessService.process(request);
                return new Response(Response.SUCCESS,productStock);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new Response(Response.SUCCESS,-1L);
    }



}
