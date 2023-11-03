package com.hongyan.study.ratelimiter.slidinglog;

import org.junit.Before;
import org.junit.After;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

/**
 * RateLimiterSildingLog Tester.
 * 滑动日志测试类
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>2月 14, 2023</pre>
 */
public class RateLimiterSildingLogTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * 滑动日志算法是实现限流的另一种方法，这种方法比较简单。
     * 基本逻辑就是记录下所有的请求时间点，新请求到来时先判断最近指定时间范围内的请求数量是否超过指定阈值，
     * 由此来确定是否达到限流，这种方式没有了时间窗口突变的问题，限流比较准确，但是因为要记录下每次请求的时间点，所以占用的内存较多。
     */
    @Test
    @DisplayName("滑动日志算法-校验QPS阀值")
    public void testTryAcquire() throws Exception {
        RateLimiterSildingLog rateLimiterSildingLog = new RateLimiterSildingLog(3);
        for (int i = 0; i < 10; i++) {
            Thread.sleep(250);
            LocalTime now = LocalTime.now();
            if (rateLimiterSildingLog.tryAcquire()) {
                System.out.println(now + " 做点什么");
            } else {
                System.out.println(now + " 被限流");
            }
        }
    }


} 
