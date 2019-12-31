package com.shop.stock.request;

import com.shop.stock.entity.ProductStock;
import com.shop.stock.service.ProductStockService;

public class ProductStockCacheRefreshRequest implements Request {

    //库存操作service
    private ProductStockService productStockService;
    //商品id
    private Integer productId;
    //强制刷新缓存标识位
    private Boolean flag;

    public ProductStockCacheRefreshRequest(ProductStockService productStockService,Integer productId,Boolean flag){
        this.productId = productId;
        this.productStockService = productStockService;
        this.flag = flag;
    }

    @Override
    public void process() {
        //获取DB库存
        ProductStock productStock = productStockService.findProductStock(productId);
        //将DB库存刷新至缓存
        productStockService.setProductStockCache(productStock);
    }

    @Override
    public Integer getProductId() {
        return this.productId;
    }

    @Override
    public Boolean getFlag() {
        return this.flag;
    }
}
