package com.shop.stock.thread;

import com.shop.stock.request.Request;
import com.shop.stock.request.RequestQueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 请求处理线程池
 */
public class RequestProcessorThreadPool {

    /**
     * 线程池
     */
    private ExecutorService threadPool = Executors.newFixedThreadPool(10);

    /**
     * 构造方法绑定线程与队列
     */
    public RequestProcessorThreadPool(){
        for(int i = 0; i < 10; i++){
            ArrayBlockingQueue<Request> queue = new ArrayBlockingQueue<Request>(100);
            RequestQueue requestQueue = RequestQueue.getInstance();
            requestQueue.addQueues(queue);
            threadPool.submit(new RequestProcessorThread(queue));
        }
    }


    private static class Singleton{

        private static RequestProcessorThreadPool instance;

        static {
            instance = new RequestProcessorThreadPool();
        }

        public static RequestProcessorThreadPool getInstance(){
            return instance;
        }

    }

    public static RequestProcessorThreadPool getInstance(){
        return Singleton.getInstance();
    }

    public static RequestProcessorThreadPool init(){
        return getInstance();
    }



}
