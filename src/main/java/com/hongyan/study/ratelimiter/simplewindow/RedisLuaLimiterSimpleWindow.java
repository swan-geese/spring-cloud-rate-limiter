package com.hongyan.study.ratelimiter.simplewindow;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @author zy
 * @version 1.0
 * @date Created in 2023/3/1 6:41 下午
 * @description 利用redis的lua脚本实现固定窗口限流/计数器算法限流
 */
@Component
public class RedisLuaLimiterSimpleWindow {

    /**
     * redis key前缀
     */
    private static final String KEY_PREFIX = "rate:limiter:simplewindow:";
    /**
     * 限流qps
     */
    private static final Integer QPS = 2;
    /**
     * redis key 有效期（秒）
     */
    private static final Integer EXPIR_TIME = 10;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    public boolean tryAcquire(String key) {
        String redisKey = KEY_PREFIX + key + ":" + System.currentTimeMillis() / 1000;
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Long.class);

        //lua文件存放在resources目录下
        redisScript.setLocation(new ClassPathResource("redis-limiter-simple-window.lua"));

        return stringRedisTemplate.execute(redisScript, Arrays.asList(redisKey), QPS.toString(), EXPIR_TIME.toString()) == 1;
    }


}
