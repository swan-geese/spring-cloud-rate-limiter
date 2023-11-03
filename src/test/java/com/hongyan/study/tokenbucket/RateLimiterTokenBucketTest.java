package com.hongyan.study.tokenbucket;

import com.google.common.util.concurrent.RateLimiter;
import com.hongyan.study.ratelimiter.slidinglog.RateLimiterSildingLog;
import com.hongyan.study.ratelimiter.tokenbucket.RateLimiterTokenBucket;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

/**
 * RateLimiterTokenBucketTest Tester.
 * 令牌桶测试类
 *
 * @author zy
 * @version 1.0
 * @since <pre>3月 1, 2023</pre>
 */
public class RateLimiterTokenBucketTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * 令牌桶算法是实现限流的另一种方法，这种方法比较简单。
     * 代码中限制 QPS 为 2，也就是每隔 500ms 生成一个令牌，但是程序每隔 250ms 获取一次令牌，所以两次获取中只有一次会成功
     * 链接：https://www.jianshu.com/p/e6622df3c377
     */
    @Test
    @DisplayName("令牌桶算法-校验QPS阀值")
    public void testTryAcquire() throws Exception {
        for (int i = 0; i < 10; i++) {
            LocalTime now = LocalTime.now();
            if (RateLimiterTokenBucket.tryAcquire()) {
                System.out.println(now + " 做点什么");
            } else {
                System.out.println(now + " 被限流");
            }
            Thread.sleep(250);
        }
    }

    @Test
    @DisplayName("原始Guava令牌桶算法-校验QPS阀值")
    public void testOriginalTryAcquire() throws Exception {
        RateLimiter rateLimiter = RateLimiter.create(2);
        for (int i = 0; i < 10; i++) {
            LocalTime now = LocalTime.now();
            if (rateLimiter.tryAcquire()) {
                System.out.println(now + " 做点什么");
            } else {
                System.out.println(now + " 被限流");
            }
            Thread.sleep(250);
        }
    }


    /**
     * result：
     *
     * 18:29:34.661367 做点什么
     * 18:29:34.916771 被限流
     * 18:29:35.171969 做点什么
     * 18:29:35.422517 被限流
     * 18:29:35.675233 做点什么
     * 18:29:35.928872 被限流
     * 18:29:36.182583 做点什么
     * 18:29:36.437676 被限流
     * 18:29:36.690544 做点什么
     * 18:29:36.942542 被限流
     */

} 
