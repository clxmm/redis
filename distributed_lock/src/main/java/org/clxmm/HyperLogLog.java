package org.clxmm;

/**
 * @author clx
 * @date 2020-06-18 21:16
 */
public class HyperLogLog {


    public static void main(String[] args) {


        Redis redis = new Redis();

        redis.execute(jedis -> {
            for (int i = 0; i < 1_000; i++) {
                jedis.pfadd("uv", "u" + i, "u" + (i + 1));
            }
            long uv = jedis.pfcount("uv");
            System.out.println(uv);  //理论1001   实际993

        });

    }


}
