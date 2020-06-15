package org.clxmm;

import redis.clients.jedis.Jedis;

/**
 * @author clx
 * @date 2020-06-13 20:09
 */
public interface CallWithJedis {

    void call(Jedis jedis);


}
