package org.clxmm;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

/**
 * @author clx
 * @date 2020-06-21 16:38
 */
public class RateLimiter {
    private Jedis jedis;


    public RateLimiter(Jedis jedis) {
        this.jedis = jedis;
    }

    /**
     * 限流方法
     *
     * @param user     操作的用户，相当于是限流的对象
     * @param action   具体的操作
     * @param period   时间窗，限流的周期
     * @param maxCount 限流的次数
     * @return
     */
    public  boolean isAllowed(String user, String action, int period, int
            maxCount) {

        //1.数据用 zset 保存，首先生成一个 key
        String key = user + "-" + action;

        //2.
        long millis = System.currentTimeMillis();


        //3.建立管道
        Pipeline pipelined = jedis.pipelined();
        pipelined.multi();


        //4.将当前的操作先存储下来
        pipelined.zadd(key, millis, String.valueOf(millis));
        //5.移除时间窗之外的数据
        pipelined.zremrangeByScore(key, 0, millis - period * 1_000);

        //6.统计剩下的 key
        Response<Long> response = pipelined.zcard(key);


        //7.将当前 key 设置一个过期时间，过期时间就是时间窗
        pipelined.expire(key, period + 1);


        //8.关闭
        pipelined.exec();
        pipelined.close();

//        8.比较时间窗内的操作数
        return response.get() <= maxCount;
    }


    public static void main(String[] args) {
        Redis redis = new Redis();


        redis.execute(j -> {
            RateLimiter rateLimiter = new RateLimiter(j);
            for (int i = 0; i < 30; i++) {

                System.out.println(rateLimiter.isAllowed("clx","public",5,3));
            }
        });


    }


}
