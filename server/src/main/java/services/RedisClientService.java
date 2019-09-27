package services;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Map;

@Service
public class RedisClientService {
    private Jedis jedisClient;

    public RedisClientService() {
        String redisHost = System.getProperty("REDIS_HOST");
        String redisPort = System.getProperty("REDIS_PORT");

        jedisClient = new Jedis(redisHost, Integer.parseInt(redisPort));
    }

    public void publish(String channel, String message) {
        jedisClient.publish(channel, message);
    }

    public Map<String, String> getAll() {
        return jedisClient.hgetAll("values");
    }
}
