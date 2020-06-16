package org.clxmm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * @author clx
 * @date 2020-06-16 21:00
 */
public class DelayMsgQueue {


    private Jedis jedis;


    private String queue;


    public DelayMsgQueue(Jedis jedis, String queue) {
        this.jedis = jedis;
        this.queue = queue;
    }


    /**
     * 消息入队
     */

    public void queue(Object object) {

        JavaMessage javaMessage = new JavaMessage();
        javaMessage.setId(UUID.randomUUID().toString());
        javaMessage.setData(new Date());
        //序列化
        try {
            String s = new ObjectMapper().writeValueAsString(javaMessage);
            System.out.println("msg publish:" + new Date());
            //消息发送  score 延迟5s
            jedis.zadd(queue, System.currentTimeMillis() + 5_000, s);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }


    /**
     * 消息消费
     */

    public void loop() {

        while (!Thread.interrupted()) {
            //读取 score 在 0 到当前时间戳之间的消息
            Set<String> zrang = jedis.zrangeByScore(queue, 0, System.currentTimeMillis(), 0, 1);
            if (zrang.isEmpty()) {

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
                continue;
            }

            //如果读取到了消息，则直接读取消息出来
            String next = zrang.iterator().next();
            if (jedis.zrem(queue, next) > 0) {
                //抢到了，接下来处理业务
                try {
                    JavaMessage javaMessage = new ObjectMapper().readValue(next, JavaMessage.class);
                    System.out.println(javaMessage +"-->"+ new Date());

                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

            }


        }


    }


}
