package com.jia16.marketing.config;

import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

@Configuration
public class RedisConfiguration {

    private final static Logger logger = Logger.getLogger(RedisConfiguration.class);

    @Resource
    private RedisProperties redisProperties;

    @Bean(destroyMethod = "close")
    public Pool<Jedis> jedisSentinelPool(JedisConnectionFactory conn) {
        Set<String> sentinels = new HashSet<>(asList(redisProperties.getSentinel().getNodes().split(",")));
        if (StringUtils.isEmpty(conn.getPassword())) {
            return new JedisSentinelPool(redisProperties.getSentinel().getMaster(), sentinels, conn.getPoolConfig(), conn.getTimeout());
        } else {
            return new JedisSentinelPool(redisProperties.getSentinel().getMaster(), sentinels, conn.getPoolConfig(), conn.getTimeout(), conn.getPassword());
        }
    }

}
