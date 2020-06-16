package org.clxmm;

/**
 * @author clx
 * @date 2020-06-16 21:15
 */
public class DealMessageTest {


    public static void main(String[] args) {
        Redis redis = new Redis();

        redis.execute(jedis -> {

            //构造一个消息队列
            DelayMsgQueue msgQueue = new DelayMsgQueue(jedis, "clx-queue");

            Thread producer = new Thread(() -> {
                for (int i = 0; i < 5; i++) {
                    msgQueue.queue("test:" + i);
                }
            });

            //消费之
            Thread consumer = new Thread(() -> msgQueue.loop());

            //启动
            producer.start();
            consumer.start();


            try {
                Thread.sleep(12_000);
                consumer.interrupt();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        });


    }


}
