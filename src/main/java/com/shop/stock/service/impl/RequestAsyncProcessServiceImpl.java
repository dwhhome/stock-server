package com.shop.stock.service.impl;

import com.shop.stock.request.Request;
import com.shop.stock.request.RequestQueue;
import com.shop.stock.service.RequestAsyncProcessService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;

@Service
public class RequestAsyncProcessServiceImpl implements RequestAsyncProcessService {

    @Override
    public void process(Request request) {
        try {
            //获取到商品指定的路由队列
            ArrayBlockingQueue<Request> queue = getRoutingQueue(request.getProductId());
            //将商品添加到该队列等待处理
            queue.put(request);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    /**
     * 获取路由到的内存队列
     * @param productId 商品id
     * @return 内存队列
     */
    private ArrayBlockingQueue<Request> getRoutingQueue(Integer productId) {
        RequestQueue requestQueue = RequestQueue.getInstance();

        // 先获取productId的hash值
        String key = String.valueOf(productId);
        int h;
        int hash = (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);

        // 对hash值取模，将hash值路由到指定的内存队列中，比如内存队列大小8
        // 用内存队列的数量对hash值取模之后，结果一定是在0~7之间
        // 所以任何一个商品id都会被固定路由到同样的一个内存队列中去的
        int index = (requestQueue.queuesSize() - 1) & hash;
        //log.info("路由内存队列，商品id：{}，索引：{}",productId,index);
        return requestQueue.getQueue(index);
    }


}
