package org.clxmm.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author clx
 * @date 2020-06-13 15:40
 */
public class JedisPoolTest {


/*    public static void main(String[] args) {

        //1. 构造一个 Jedis 连接池
        JedisPool pool = new JedisPool("127.0.0.1", 6379);
        //2. 从连接池中获取一个 Jedis 连接
        Jedis jedis = pool.getResource();
        jedis.auth("123456");

        try {
            //3 .jedis 操作
            String ping = jedis.ping();
            System.out.println(ping);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                //4. 归还连接
                jedis.close();
            }
        }

    }*/


    public static void main(String[] args) {
        Redis redis = new Redis();
        redis.execute(jedis -> {
            jedis.auth("123456");
            String ping = jedis.ping();
            System.out.println(ping);
        });

    }

    public static void main1(String[] args) {

        //1. 构造一个 Jedis 连接池
        JedisPool pool = new JedisPool("127.0.0.1", 6379);
        //2. 从连接池中获取一个 Jedis 连接

        try (Jedis jedis = pool.getResource()) {
            jedis.auth("123456");
            String ping = jedis.ping();
            System.out.println(ping);
        }

    }


}
