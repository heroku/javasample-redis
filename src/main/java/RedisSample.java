import org.apache.commons.pool.impl.GenericObjectPool.Config;
import redis.clients.jedis.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RedisSample {

    private static final JedisPool jedisPool;

    static {
        final Pattern redisUrlPattern = Pattern.compile("^redis://([^:]*):([^@]*)@([^:]*):([^/]*)(/)?");
        final Matcher redisUrlMatcher = redisUrlPattern.matcher(System.getenv("REDISTOGO_URL"));
        redisUrlMatcher.matches();

        final String host = redisUrlMatcher.group(3);
        final int port = Integer.parseInt(redisUrlMatcher.group(4));
        final String password = redisUrlMatcher.group(2);

        final Config config = new Config();
        config.testOnBorrow = true;

        jedisPool = new JedisPool(config, host, port, Protocol.DEFAULT_TIMEOUT, password);
    }

    public static void main(String[] args) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set("key", "value");
            String value = jedis.get("key");
            System.out.println("value = " + value);
        } finally {
            if (jedis != null) jedisPool.returnResource(jedis);
        }
    }
}

