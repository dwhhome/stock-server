package com.shop.stock.service.impl;

import com.shop.stock.entity.ProductStock;
import com.shop.stock.mapper.ProductStockMapper;
import com.shop.stock.redis.RedisUtils;
import com.shop.stock.service.ProductStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class ProductStockServiceImpl implements ProductStockService {

    @Autowired
    private RedisUtils redisUtils;
    @Resource
    private ProductStockMapper productStockMapper;

    @Override
    public void updateProductStock(ProductStock productStock) {
        productStockMapper.updateProductStock(productStock);
    }

    @Override
    public void removeProductStockCache(ProductStock productStock) {
        redisUtils.del("product:stock:"+productStock.getProductId());
    }

    @Override
    public ProductStock findProductStock(Integer productId) {
        return productStockMapper.findProductStock(productId);
    }

    @Override
    public void setProductStockCache(ProductStock productStock) {
        redisUtils.set("product:stock:"+productStock.getProductId(),productStock.getProductCnt());
    }

    @Override
    public ProductStock getProductStockCache(Integer productId) {
        Integer cnt = (Integer) redisUtils.get("product:stock:"+productId);
        if(null == cnt){
            return null;
        }
        return new ProductStock(productId,Long.valueOf(cnt));
    }
}
