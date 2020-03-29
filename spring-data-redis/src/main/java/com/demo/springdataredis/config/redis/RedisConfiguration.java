package com.demo.springdataredis.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @program: javaArchitecture
 * @description: redis配置类
 * @author: LiuYunKai
 * @create: 2020-03-28 14:02
 **/
@Configuration
public class RedisConfiguration {

//   @Bean
//   @SuppressWarnings("all")
//   public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
//       RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
//       template.setConnectionFactory(factory);
//       Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//       ObjectMapper om = new ObjectMapper();
//       om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//       om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//       jackson2JsonRedisSerializer.setObjectMapper(om);
//       StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//       // key采用String的序列化方式
//       template.setKeySerializer(stringRedisSerializer);
//       // hash的key也采用String的序列化方式
//       template.setHashKeySerializer(stringRedisSerializer);
//       // value序列化方式采用jackson
//       template.setValueSerializer(jackson2JsonRedisSerializer);
//       // hash的value序列化方式采用jackson
//       template.setHashValueSerializer(jackson2JsonRedisSerializer);
//       template.afterPropertiesSet();
//       return template;
//   }

    /**
     * springboot2.x 使用LettuceConnectionFactory 代替 RedisConnectionFactory
     * application.yml配置基本信息后,springboot2.x  RedisAutoConfiguration能够自动装配
     * LettuceConnectionFactory 和 RedisConnectionFactory 及其 RedisTemplate
     *
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

}
