package com.shop.stock.entity;

import lombok.Data;

/**
 * 库存数量
 */
@Data
public class ProductStock {

    /**
     * 商品id
     */
    private Integer productId;
    /**
     * 库存数量
     */
    private Long productCnt;

    public ProductStock(){

    }

    public ProductStock(Integer productId,Long productCnt){
        this.productCnt= productCnt;
        this.productId = productId;
    }

}
