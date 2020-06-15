import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

/**
 * @author clx
 * @date 2020-06-13 20:37
 */
public class LettuceTest {


    public static void main(String[] args) {


        RedisClient client = RedisClient.create("redis://123456@127.0.0.1");
        StatefulRedisConnection<String, String> connect = client.connect();

        RedisCommands<String, String> sync = connect.sync();

        String set = sync.set("test1", "clx");
        String test1 = sync.get("test1");
        System.out.println(test1);

    }


}
