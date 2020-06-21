package org.clxmm;

import io.rebloom.client.Client;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.JedisPool;

/**
 * @author clx
 * @date 2020-06-21 15:58
 * redisbloom  的使用
 */
public class BloomFilter {


    public static void main(String[] args) {


        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxWaitMillis(30_000L);
        config.setTestOnBorrow(true);
        config.setMaxTotal(300);
        config.setMaxIdle(10_000);


        JedisPool pool = new JedisPool(config, "139.196.160.31", 6379, 30_000);
        Client client = new Client(pool);

        //存入数据
        for (int i = 0; i < 10_000; i++) {
            client.add("name","clx-"+i);
        }

        //判断是否存在
        boolean name = client.exists("name", "clx-99999");
        System.out.println(name);


    }


}
