package com.hongyan.study.ratelimiter.leakybucket;

import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.After;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalTime;

/**
 * RedisLuaLimiterLeakyBucket Tester.
 * 利用redis实现漏桶算法
 * @author zy
 * @version 1.0
 * @since <pre>3月 3, 2023</pre>
 */
@SpringBootTest
public class RedisLuaLimiterLeakyBucketTest {

    @Resource
    private RedisLuaLimiterLeakyBucket redisLuaLimiterLeakyBucket;

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: tryAcquire(String key, int capacity, int interval)
     */
    @SneakyThrows
    @Test
    @DisplayName("Redis-Lua漏桶算法-校验QPS阀值")
    public void testTryAcquire() throws Exception {
        final String user = "user";
        for (int i = 0; i < 10; i++) {
            Thread.sleep(200);
            LocalTime now = LocalTime.now();
            if (!redisLuaLimiterLeakyBucket.tryAcquire(user)) {
                System.out.println(now + " 被限流");
            } else {
                System.out.println(now + " 做点什么");
            }
        }
    }


} 
