import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import services.FibonacciService;

import javax.annotation.Resource;


@SpringBootApplication
public class Worker {
    @Resource
    private FibonacciService fibonacciService;

    public void main(String[] arg) {

        String redisHost = System.getProperty("REDIS_HOST");
        String redisPort = System.getProperty("REDIS_PORT");

        final Jedis jRedisClient = new Jedis(redisHost, Integer.parseInt(redisPort));
        Jedis jRedisSubscriber = new Jedis(redisHost, Integer.parseInt(redisPort));

        jRedisSubscriber.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                jRedisClient.hset("values", message, convert(fibonacciService.fib(convert(message))));
            }
        }, "insert");
        SpringApplication.run(Worker.class, arg);
    }


    private String convert(int number) {
        return String.valueOf(number);
    }
    private int convert(String number) {
        return Integer.parseInt(number);
    }
}
