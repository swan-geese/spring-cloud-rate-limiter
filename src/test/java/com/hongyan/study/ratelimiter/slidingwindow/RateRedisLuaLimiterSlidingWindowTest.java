package com.hongyan.study.ratelimiter.slidingwindow;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import javax.annotation.Resource;
import java.time.LocalTime;


/**
 * 利用redis实现滑动窗口算法
 *  url: https://www.wdbyte.com/java/rate-limiter.html#_7-3-%E6%BB%91%E5%8A%A8%E7%AA%97%E5%8F%A3%E9%99%90%E6%B5%81
 */
@SpringBootTest
class RateRedisLuaLimiterSlidingWindowTest {

    @Resource
    private RedisLuaLimiterSlidingWindow redisLuaLimiterSlidingWindow;

    /**
     *
     * 通过对上面的基于 incr 命令实现的 Redis 限流方式的测试，我们已经发现了固定窗口限流所带来的问题，
     * 在这篇文章的第三部分已经介绍了滑动窗口限流的优势，它可以大幅度降低因为窗口临界突变带来的问题，那么如何使用 Redis 来实现滑动窗口限流呢？
     *
     * 这里主要使用 ZSET 有序集合来实现滑动窗口限流，ZSET 集合有下面几个特点：
     *
     * ZSET 集合中的 key 值可以自动排序。
     * ZSET 集合中的 value 不能有重复值。
     * ZSET 集合可以方便的使用 ZCARD 命令获取元素个数。
     * ZSET 集合可以方便的使用 ZREMRANGEBYLEX 命令移除指定范围的 key 值。
     */
    @SneakyThrows
    @Test
    @DisplayName("Redis-Lua滑动算法-校验QPS阀值")
    void tryAcquire() {
        final String user = "user";
        for (int i = 0; i < 10; i++) {
            Thread.sleep(200);
            LocalTime now = LocalTime.now();
            if (!redisLuaLimiterSlidingWindow.tryAcquire(user)) {
                System.out.println(now + " 被限流");
            } else {
                System.out.println(now + " 做点什么");
            }
        }
    }

    /**
     * 代码中虽然限制了 QPS 为 2，但是因为这种限流实现是把毫秒时间戳作为 key 的，
     * 所以会有临界窗口突变的问题，下面是运行结果，可以看到因为时间窗口的变化，导致了 QPS 超过了限制值 2
     * 优化为以秒作为 key，则会屏蔽该情况
     *
     * result:
     * 10:27:07.830375 做点什么
     * 10:27:08.443091 做点什么
     * 10:27:08.698974 做点什么
     * 10:27:08.957397 被限流
     * 10:27:09.212884 做点什么
     * 10:27:09.468631 做点什么
     * 10:27:09.724375 被限流
     * 10:27:09.979109 被限流
     * 10:27:10.237735 做点什么
     * 10:27:10.491125 做点什么
     *
     *
     */
}