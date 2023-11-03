package com.hongyan.study.ratelimiter.slidingwindow;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @author zy
 * @version 1.0
 * @date Created in 2023/3/2 5:41 下午
 * @description 利用redis的lua脚本实现滑动窗口限流
 */
@Component
public class RedisLuaLimiterSlidingWindow {

    /**
     * redis key前缀
     */
    private static final String KEY_PREFIX = "rate:limiter:slidingwindow:";
    /**
     * 限流qps
     */
    private static final Integer QPS = 2;
    /**
     * redis key 有效期（秒）
     */
    private static final Integer EXPIR_TIME = 1;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    public boolean tryAcquire(String key) {
        String redisKey = KEY_PREFIX + key;
        long now = System.currentTimeMillis();
        String oldest = String.valueOf(now - EXPIR_TIME * 1000);
        String score = String.valueOf(now);
        String scoreValue = score;
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Long.class);
        //lua文件存放在resources目录下
        redisScript.setLocation(new ClassPathResource("redis-limiter-sliding-window.lua"));

        return stringRedisTemplate.execute(redisScript, Arrays.asList(redisKey), oldest, score, QPS.toString(), scoreValue) == 1;
    }


}
