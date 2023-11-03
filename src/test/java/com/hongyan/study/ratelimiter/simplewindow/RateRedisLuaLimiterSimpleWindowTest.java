package com.hongyan.study.ratelimiter.simplewindow;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import javax.annotation.Resource;
import java.time.LocalTime;


/**
 * 利用redis实现固定窗口算法/计数器算法
 *  url: https://www.wdbyte.com/java/rate-limiter.html#_2-%E5%9B%BA%E5%AE%9A%E7%AA%97%E5%8F%A3%E7%AE%97%E6%B3%95
 */
@SpringBootTest
class RateRedisLuaLimiterSimpleWindowTest {

    @Resource
    private RedisLuaLimiterSimpleWindow redisLuaLimiterSimpleWindow;

    /**
     * Redis 中的固定窗口限流是使用 incr 命令实现的，incr 命令通常用来自增计数；如果我们使用时间戳信息作为 key，自然就可以统计每秒的请求量了，以此达到限流目的。
     * 这里有两点要注意。
     * 对于不存在的 key，第一次新增时，value 始终为 1。
     * INCR 和 EXPIRE 命令操作应该在一个原子操作中提交，以保证每个 key 都正确设置了过期时间，不然会有 key 值无法自动删除而导致的内存溢出
     */
    @SneakyThrows
    @Test
    @DisplayName("Redis-Lua计数器算法-校验QPS阀值")
    void tryAcquire() {
        final String user = "user";
        for (int i = 0; i < 10; i++) {
            Thread.sleep(250);
            LocalTime now = LocalTime.now();
            if (!redisLuaLimiterSimpleWindow.tryAcquire(user)) {
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