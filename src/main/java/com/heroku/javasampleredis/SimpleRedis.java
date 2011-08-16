package com.heroku.javasampleredis;

import org.apache.commons.pool.impl.GenericObjectPool.Config;

import redis.clients.jedis.*;

import java.net.URI;
import java.net.URISyntaxException;

public class SimpleRedis {
    
    private static final JedisPool jedisPool;
    
    static {
        String host = null;
        int port = -1;
        String password = null;

        try {
            final URI dbUri = new URI(System.getenv("REDISTOGO_URL"));
            host = dbUri.getHost();
            port = dbUri.getPort();

            if (dbUri.getUserInfo() != null) {
                password = dbUri.getUserInfo().split(":")[1];
            }
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            System.exit(1);
        }

        jedisPool = new JedisPool(new Config(), host, port, Protocol.DEFAULT_TIMEOUT, password);
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
