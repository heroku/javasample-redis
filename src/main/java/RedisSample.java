import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.pool.impl.GenericObjectPool.Config;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;

public class RedisSample {
    
    static JedisPool jedisPool;
    
    static {
        Pattern REDIS_URL_PATTERN = Pattern.compile("^redis://([^:]*):([^@]*)@([^:]*):([^/]*)(/)?");
        Matcher matcher = REDIS_URL_PATTERN.matcher(System.getenv("REDISTOGO_URL"));
        matcher.matches();
        Config config = new Config();
        config.testOnBorrow = true;
        jedisPool = new JedisPool(config, matcher.group(3), Integer.parseInt(matcher.group(4)), Protocol.DEFAULT_TIMEOUT, matcher.group(2));
    }

    public static void main(String[] args) {
        Jedis jedis = jedisPool.getResource();
        jedis.set("key", "value");
        String value = jedis.get("key");
        System.out.println("value = " + value);
        jedisPool.returnResource(jedis);
    }
}

