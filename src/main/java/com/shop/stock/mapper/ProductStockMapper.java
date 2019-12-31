package com.shop.stock.mapper;

import com.shop.stock.entity.ProductStock;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface ProductStockMapper {

    /**
     * 更新库存数量
     * @param productStock 商品库存
     */
    @Update(" update product_stock set product_cnt = #{productCnt} where product_id = #{productId} ")
    void updateProductStock(ProductStock productStock);

    /**
     * 根据商品id查询商品库存信息
     * @param productId 商品id
     * @return 商品库存信息
     */
    @Select(" select product_id as productId,product_cnt as productCnt from product_stock where product_id = #{productId}")
    ProductStock findProductStock(@Param("productId") Integer productId);

}
