package com.demo.redis.common.dao;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class RedisDAO {

    private static final Logger log = LoggerFactory.getLogger(RedisDAO.class);
    private final List<JedisPool> jedisPoolListLocal = new ArrayList<>();
    private static final int timeout = 1000;
    private static final String PASSWORD = "password";
    private static final int db =1;
    private static final AtomicInteger counter = new AtomicInteger();

    private RedisDAO() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMinIdle(10);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMaxTotal(500);
        poolConfig.setTestOnReturn(true);
        poolConfig.setMaxWaitMillis(1000);
        jedisPoolListLocal.add(new JedisPool(poolConfig, "127.0.0.1", 6379, timeout));
    }

    private static final RedisDAO instance = new RedisDAO();

    public static RedisDAO getInstance() {
        return instance;
    }


    // local
    private JedisPool getJedisPool_local() {
        JedisPool jedisPool = jedisPoolListLocal.get(0);
        return jedisPool;
    }

    public Optional<Jedis> getJedisLocal() {
        JedisPool jedisPool = getJedisPool_local();
        for (int i = 0; i < 10; i++) {
            try {
                Jedis resource = jedisPool.getResource();
                if(resource != null) {
                    return Optional.of(resource);
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
        return Optional.empty();
    }

    public void close(Jedis jedis) {
        try {
            jedis.close();
            counter.decrementAndGet();
        } catch (Exception e) {
            log.error("", e);
        }
    }
    public int getCounter() {
        return counter.get();
    }
}
