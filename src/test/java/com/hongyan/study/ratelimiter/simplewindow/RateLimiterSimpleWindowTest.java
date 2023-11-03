package com.hongyan.study.ratelimiter.simplewindow;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalTime;


/**
 * 固定窗口算法又叫计数器算法，是一种简单方便的限流算法。
 * 主要通过一个支持原子操作的计数器来累计 1 秒内的请求次数，当 1 秒内计数达到限流阈值时触发拒绝策略。每过 1 秒，计数器重置为 0 开始重新计数。
 *  url: https://www.wdbyte.com/java/rate-limiter.html#_2-%E5%9B%BA%E5%AE%9A%E7%AA%97%E5%8F%A3%E7%AE%97%E6%B3%95
 */
@SpringBootTest
class RateLimiterSimpleWindowTest {

    /**
     * 计数器算法demo1：
     * 思考：？
     * 从输出结果中可以看到大概每秒操作 3 次，由于限制 QPS 为 2，所以平均会有一次被限流。
     */
    @SneakyThrows
    @Test
    @DisplayName("固定窗口算法/计数器算法-校验QPS阀值")
    void tryAcquire() {
        for (int i = 0; i < 10; i++) {
            Thread.sleep(250);
            LocalTime now = LocalTime.now();
            if (!RateLimiterSimpleWindow.tryAcquire()) {
                System.out.println(now + " 被限流");
            } else {
                System.out.println(now + " 做点什么");
            }
        }
    }

    /**
     * result:
     * 18:29:34.475645 做点什么
     * 18:29:34.728072 做点什么
     * 18:29:34.982118 被限流
     * 18:29:35.233146 被限流
     * 18:29:35.488203 做点什么
     * 18:29:35.742263 做点什么
     * 18:29:35.994344 被限流
     * 18:29:36.248094 被限流
     * 18:29:36.498380 做点什么
     * 18:29:36.750501 做点什么
     */


    /**
     * 计数器算法demo2：
     * 虽然我们限制了 QPS 为 2，
     * 但是当遇到时间窗口的临界突变时，
     * 如 1s 中的后 500 ms 和第 2s 的前 500ms 时，
     * 虽然是加起来是 1s 时间，却可以被请求 4 次。
     */
    @SneakyThrows
    @Test
    @DisplayName("固定窗口算法/计数器算法-构造1s中超出QPS阀值")
    public void tryAfterAcquire() {
        // 先休眠 400ms，可以更快的到达时间窗口。
        Thread.sleep(400);
        for (int i = 0; i < 10; i++) {
            Thread.sleep(250);
            if (!RateLimiterSimpleWindow.tryAcquire()) {
                System.out.println("被限流");
            } else {
                System.out.println("做点什么");
            }
        }
    }


}