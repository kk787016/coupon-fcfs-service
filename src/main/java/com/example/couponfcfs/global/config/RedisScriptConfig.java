package com.example.couponfcfs.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

@Configuration
public class RedisScriptConfig {

    @Bean
    public RedisScript<String> issueCouponScript() {
        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();

        redisScript.setLocation(new ClassPathResource("scripts/issue_coupon.lua"));
        redisScript.setResultType(String.class);
        return redisScript;
    }


}
