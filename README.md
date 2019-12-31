# stock-server
库存服务示例：数据库缓存一致性解决方案实现

# 实现技术
springboot+mybatis+redis

#数据库代码
  CREATE DATABASE /*!32312 IF NOT EXISTS*/`shop` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

  USE `shop`;

  /*Table structure for table `product_stock` */

  DROP TABLE IF EXISTS `product_stock`;

  CREATE TABLE `product_stock` (
    `product_id` int(11) NOT NULL,
    `product_cnt` int(11) DEFAULT NULL,
    PRIMARY KEY (`product_id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

  /*Data for the table `product_stock` */

  insert  into `product_stock`(`product_id`,`product_cnt`) values (1,100);
 
#测试方式
  更新商品库存：/product/stock   (put)
  查询商品库存：/product/{id}    (get)

# 基础实现原理

  更新数据的时候，根据数据的唯一标识，将操作路由之后，发送到一个jvm内部的队列中

  读取数据的时候，如果发现数据不在缓存中，那么将重新读取数据+更新缓存的操作，根据唯一标识路由之后，也发送同一个jvm内部的队列中

  一个队列对应一个工作线程，每个工作线程串行拿到对应的操作，一条一条的执行
  
  一个数据变更的操作，先执行，删除缓存，然后再去更新数据库，但是还没完成更新，此时如果一个读请求过来，读到了空的缓存，那么可以先将缓存更新的请求发送到队列中，此时会在队列中积压，然后同步等待缓存更新完成
  
  优化点：
    一个队列中，其实多个更新缓存请求串在一起是没意义的，因此可以做过滤，如果发现队列中已经有一个更新缓存的请求了，那么就不用再放个更新请求操作进去了，直接等待前面的更新操作请求完成即可，待那个队列对应的工作线程完成了上一个操作的数据库的修改之后，才会去执行下一个操作，也就是缓存更新的操作，此时会从数据库中读取最新的值，然后写入缓存中，如果请求还在等待时间范围内，不断轮询发现可以取到值了，那么就直接返回; 如果请求等待的时间超过一定时长，那么这一次直接从数据库中读取当前的旧值
