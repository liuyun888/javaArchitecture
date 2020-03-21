package com.demo.redis.repository.redis;

import com.demo.redis.common.constant.Constant;
import com.demo.redis.common.dao.RedisDAO;
import com.demo.redis.entity.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Optional;

/**
 * @program: javaArchitecture
 * @description: 测试
 * @author: LiuYunKai
 * @create: 2020-03-17 18:32
 **/
@Repository
public class TestRedisRepository implements Constant {

    private final static Logger logger = LoggerFactory.getLogger(TestRedisRepository.class);

    /**
     * 测试保存
     *
     * @param test 测试
     * @return 设置状态
     */
    public boolean setTest(Test test) {
        Jedis jedis = null;
        boolean result = true;
        try {
            Optional<Jedis> jedisOp = RedisDAO.getInstance().getJedisLocal();
            if (jedisOp.isPresent()) {
                jedis = jedisOp.get();
                String key = TEST + test.getId();
                jedis.set(key, test.getJson());
            } else {
                logger.error("get resource error");
            }

        } catch (Exception e) {
            result = false;
            logger.error("", e);
        } finally {
            RedisDAO.getInstance().close(jedis);
        }
        return result;
    }

    public String getTest(String time) {
        Jedis jedis = null;
        String TestJsonStr = "";
        try {
            Optional<Jedis> jedisOp = RedisDAO.getInstance().getJedisLocal();
            if (jedisOp.isPresent()) {
                jedis = jedisOp.get();
                String key = TEST + time;
                TestJsonStr = jedis.get(key);

            } else {
                logger.error("get resource error");
            }

        } catch (Exception e) {
            logger.error("", e);
        } finally {
            RedisDAO.getInstance().close(jedis);
        }

        return TestJsonStr;
    }


}
