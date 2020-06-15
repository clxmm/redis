package org.clxmm;

import redis.clients.jedis.params.SetParams;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author clx
 * @date 2020-06-13 20:56
 */
public class LockTest {


    /**
     * 上面的代码存在一个小小问题：如果代码业务执行的过程中抛异常或者挂了，这样会导致 del 指令没有
     * 被调用，这样，k1 无法释放，后面来的请求全部堵塞在这里，锁也永远得不到释放。
     * 要解决这个问题，我们可以给锁添加一个过期时间，确保锁在一定的时间之后，能够得到释放。改进后
     * 的代码如下：
     *
     * @param args
     */
    public static void main11(String[] args) {

        Redis redis = new Redis();

        redis.execute(jedis -> {
            Long setnx = jedis.setnx("test2", "v1");

            if (setnx == 1) {
                //没人占位
                String s = jedis.set("name", "clx");

                String name = jedis.get("name");
                System.out.println(name);
                //释放资源
                jedis.del("test2");
            } else {
                //没人占位，停止/暂缓使用
            }
        });


    }


    public static void main2(String[] args) {

        Redis redis = new Redis();

        redis.execute(jedis -> {
            Long setnx = jedis.setnx("test2", "v1");


            if (setnx == 1) {
                //给锁添加一个过期时间，防止应用在运行过程中抛出异常导致锁无法及时得到释放
                jedis.expire("test2", 30);
                //没人占位
                String s = jedis.set("name", "clx");

                String name = jedis.get("name");
                System.out.println(name);
                //释放资源
                jedis.del("test2");
            } else {
                //没人占位，停止/暂缓使用
            }
        });
    }


    /**
     * 上面的
     * 这样改造之后，还有一个问题，就是在获取锁和设置过期时间之间如果如果服务器突然挂掉了，这个时
     * 候锁被占用，无法及时得到释放，也会造成死锁，因为获取锁和设置过期时间是两个操作，不具备原子
     * 性。
     * 为了解决这个问题，从 Redis2.8 开始，setnx 和 expire 可以通过一个命令一起来执行了，我们对上述
     * 代码再做改进
     *
     * @param args
     */
    public static void main3(String[] args) {
        Redis redis = new Redis();
        redis.execute(jedis -> {

            String set = jedis.set("test2", "v1", new SetParams().nx().ex(30));

            if (set != null && "OK".equals(set)) {
                //给锁添加一个过期时间，防止应用在运行过程中抛出异常导致锁无法及时得到释放
                jedis.expire("test2", 30);
                //没人占位
                String s = jedis.set("name", "clx");

                String name = jedis.get("name");
                System.out.println(name);
                //释放资源
                jedis.del("test2");
            } else {
                //没人占位，停止/暂缓使用
            }
        });
    }


    public static void main(String[] args) {
        Redis redis = new Redis();
        for (int i = 0; i < 2; i++) {

            redis.execute(jedis -> {

                String value = UUID.randomUUID().toString();

                //获取锁
                String set = jedis.set("test2", value, new SetParams().nx().ex(30));
                //3.判断是否成功拿到锁
                if (set != null && "OK".equals(set)) {
                    //给锁添加一个过期时间，防止应用在运行过程中抛出异常导致锁无法及时得到释放
                    jedis.expire("test2", 30);
                    //没人占位
                    String s = jedis.set("name", "clx");

                    String name = jedis.get("name");
                    System.out.println(name);
                    //释放资源
                     jedis.evalsha("b8059ba43af6ffe8bed3db65bac35d452f8115d8",
                            Arrays.asList("k1"), Arrays.asList(value));
                } else {
                    //没人占位，停止/暂缓使用
                }
            });
        }
    }


}
