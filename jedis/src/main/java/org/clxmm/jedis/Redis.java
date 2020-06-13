package org.clxmm.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author clx
 * @date 2020-06-13 20:10
 */
public class Redis {
    private JedisPool pool;

    public Redis() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        //连接池最大空闲数
        config.setMaxIdle(300);
        //最大连接数·
        config.setMaxTotal(1_000);
        //最大等待时间 -1 表示没有限制
        config.setMaxWaitMillis(30_000);
        //空闲时间检查有效性
        config.setTestOnBorrow(true);

        pool = new JedisPool(config, "127.0.0.1", 6379, 30_000, "123456");
    }

    public void execute(CallWithJedis withJedis) {
        try (Jedis jedis = pool.getResource()) {
            withJedis.call(jedis);
        }
    }


}
