package org.clxmm.jedis;

import redis.clients.jedis.Jedis;

/**
 * @author clx
 * @date 2020-06-13 15:34
 */
public class MyJedis {


    public static void main(String[] args) {

        // 构造一个jedis 对象，默认端口不用配置
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        //2. 密码验证
        jedis.auth("123456");
        String ping = jedis.ping();
        //4.返回 pong 表示连接成功
        System.out.println(ping);
    }


}
