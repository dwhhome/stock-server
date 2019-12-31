package com.shop.stock.request;

/**
 * 请求对象
 */
public interface Request {

    void process();

    Integer getProductId();

    Boolean getFlag();

}
