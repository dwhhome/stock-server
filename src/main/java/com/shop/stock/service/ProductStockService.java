package com.shop.stock.service;

import com.shop.stock.entity.ProductStock;

public interface ProductStockService {

    /**
     * 更新商品库存
     * @param productStock 商品库存
     */
    void updateProductStock(ProductStock productStock);

    /**
     * 删除Redis中的商品库存的缓存
     * @param productStock 商品库存
     */
    void removeProductStockCache(ProductStock productStock);

    /**
     * 根据商品id查询商品库存
     * @param productId 商品id
     * @return 商品库存
     */
    ProductStock findProductStock(Integer productId);

    /**
     * 设置商品库存的缓存
     * @param productStock 商品库存
     */
    void setProductStockCache(ProductStock productStock);

    /**
     * 获取商品库存的缓存
     * @param productId
     * @return
     */
    ProductStock getProductStockCache(Integer productId);

}
