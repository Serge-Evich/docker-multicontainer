import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import services.FibonacciService;


@SpringBootApplication
public class Worker {

    public static void main(String[] arg) {

        String redisHost = System.getProperty("REDIS_HOST");
        String redisPort = System.getProperty("REDIS_PORT");

        final Jedis jRedisClient = new Jedis(redisHost, Integer.parseInt(redisPort));
        Jedis jRedisSubscriber = new Jedis(redisHost, Integer.parseInt(redisPort));

        ConfigurableApplicationContext context = SpringApplication.run(Worker.class, arg);

        final FibonacciService fibonacciService = context.getBean("fibonacciService", FibonacciService.class);

        jRedisSubscriber.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                jRedisClient.hset("values", message, convert(fibonacciService.fib(convert(message))));
            }
        }, "insert");
    }


    private static String convert(int number) {
        return String.valueOf(number);
    }
    private static int convert(String number) {
        return Integer.parseInt(number);
    }
}
