package com.shop.stock.request;

import com.shop.stock.entity.ProductStock;
import com.shop.stock.service.ProductStockService;

public class ProductStockDBUpdateRequest implements Request {

    //库存操作service
    private ProductStockService productStockService;
    //库存信息
    private ProductStock productStock;

    public ProductStockDBUpdateRequest(ProductStockService productStockService,ProductStock productStock){
        this.productStock = productStock;
        this.productStockService = productStockService;
    }

    @Override
    public void process() {
        //删除缓存
        productStockService.removeProductStockCache(productStock);
        //更新数据库库存
        productStockService.updateProductStock(productStock);
    }

    @Override
    public Integer getProductId() {
        return this.productStock.getProductId();
    }

    @Override
    public Boolean getFlag() {
        return false;
    }

}
