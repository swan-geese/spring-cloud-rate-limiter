package com.hongyan.study.ratelimiter.slidingwindow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;

/**
 * RateLimiterSlidingWindow Tester.
 *  滑动窗口测试类
 * @author <Authors name>
 * @version 1.0
 * @since <pre>2月 10, 2023</pre>
 */
@SpringBootTest
public class RateLimiterSlidingWindowTest {


    /**
     * 动窗口算法是对固定窗口/计算器算法的改进。既然固定窗口算法在遇到时间窗口的临界突变时会有问题，那么我们在遇到下一个时间窗口前也调整时间窗口不就可以了吗？
     *  可以发现窗口滑动的间隔越短，时间窗口的临界突变问题发生的概率也就越小，不过只要有时间窗口的存在，还是有可能发生时间窗口的临界突变问题。
     *
     * 链接：https://www.jianshu.com/p/e6622df3c377
     */
    @DisplayName("滑动窗口算法-校验QPS阀值")
    @Test
    public void testTryAcquire() throws Exception {
        int qps = 2, count = 20, sleep = 300, success = count * sleep / 1000 * qps;
        System.out.println(String.format("当前QPS限制为:%d,当前测试次数:%d,间隔:%dms,预计成功次数:%d", qps, count, sleep, success));
        RateLimiterSlidingWindow.init(10);
        success = 0;
        for (int i = 0; i < count; i++) {
            Thread.sleep(sleep);
            if (RateLimiterSlidingWindow.tryAcquire()) {
                success++;
                System.out.println(LocalTime.now() + ": success");
            } else {
                System.out.println(LocalTime.now() + ": fail");
            }
        }
        System.out.println();
        System.out.println("实际测试成功次数:" + success);
    }


} 
