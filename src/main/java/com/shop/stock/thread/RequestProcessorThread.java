package com.shop.stock.thread;

import com.shop.stock.request.ProductStockDBUpdateRequest;
import com.shop.stock.request.Request;
import com.shop.stock.request.RequestQueue;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;

public class RequestProcessorThread implements Callable<Boolean> {

    private ArrayBlockingQueue<Request> queue;

    public RequestProcessorThread(ArrayBlockingQueue<Request> queue){
        this.queue = queue;
    }

    @Override
    public Boolean call(){
        try {
            while (true){
                Request request = queue.take();
                Map<Integer,Boolean> flagMap = RequestQueue.getInstance().getFlagMap();
                if(request instanceof ProductStockDBUpdateRequest){
                    //写请求
                    flagMap.put(request.getProductId(),true);
                    request.process();
                }
                else{
                    //读请求过滤
                    if(!request.getFlag()){ //不需强制刷新缓存的做读请求过滤
                        Boolean flag = flagMap.get(request.getProductId());
                        //读请求
                        if(null == flag){
                            //空请求算作读缓存
                            flagMap.put(request.getProductId(),false);
                            request.process();
                        }
                        if(null != flag && flag){
                            flagMap.put(request.getProductId(),false);
                            request.process();
                        }
                        System.out.println("当前读请求已过滤...");
                        //if(null != flag && !flag){//过滤不做处理}
                    }else{//强制刷新缓存
                        flagMap.put(request.getProductId(),false);
                        request.process();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }
}
