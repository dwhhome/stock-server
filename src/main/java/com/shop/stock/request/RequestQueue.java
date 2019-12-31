package com.shop.stock.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 请求内存队列
 */
public class RequestQueue {

    private List<ArrayBlockingQueue<Request>> queues = new ArrayList<>();

    //维护标识过滤请求优化
    private Map<Integer,Boolean> flagMap = new ConcurrentHashMap<>();


    private static class Singleton{

        private static RequestQueue queues;

        static {
            queues = new RequestQueue();
        }

        public static RequestQueue getInstance(){
            return queues;
        }

    }

    /**
     * 初始化
     * @return
     */
    public static RequestQueue getInstance(){
        return Singleton.getInstance();
    }

    /**
     * 添加队列
     * @param queue
     */
    public void addQueues(ArrayBlockingQueue<Request> queue){
        this.queues.add(queue);
    }

    /**
     * 获取内存队列大小
     * @return
     */
    public int queuesSize(){
        return this.queues.size();
    }

    /**
     * 获取内存队列
     * @param index
     * @return
     */
    public ArrayBlockingQueue<Request> getQueue(int index){
        return this.queues.get(index);
    }

    /**
     * 获取标识
     * @return
     */
    public Map<Integer, Boolean> getFlagMap() {
        return flagMap;
    }

}
