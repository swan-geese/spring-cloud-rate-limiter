package com.hongyan.study.ratelimiter.leakybucket;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zy
 * @version 1.0
 * @date Created in 2023/3/3 6:41 下午
 * @description 利用redis的lua脚本实现漏桶算法限流
 */
@Component
public class RedisLuaLimiterLeakyBucket {

    /**
     * redis key前缀
     */
    private static final String KEY_PREFIX = "rate:limiter:leakybucket:";
    /**
     * 限流qps/桶容量
     */
    private static final Integer QPS = 2;
    /**
     * 时间间隔，单位为秒
     */
    private static final Integer INTERVAL = 1;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    public boolean tryAcquire(String key) {
        String redisKey = KEY_PREFIX + key;
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Long.class);
        //lua文件存放在resources目录下
        redisScript.setLocation(new ClassPathResource("redis-limiter-leaky-bucket.lua"));
        List<String> keys = new ArrayList<>();
        keys.add(redisKey);
        List<Object> args = new ArrayList<>();
        args.add(QPS.toString());
        args.add(INTERVAL.toString());
        Long result = stringRedisTemplate.execute(redisScript, keys, args.toArray());
        return result != null && result == 1;
    }
}
