package com.zkh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

@Configuration
public class RedisCacheConfig {
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration.serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()) ) //redis  序列化方式json
            .entryTtl(Duration.ofDays(7))               //缓存有效期 7天
            .disableCachingNullValues()                 //禁止缓存空值

         ;
        return redisCacheConfiguration;
    }
}
