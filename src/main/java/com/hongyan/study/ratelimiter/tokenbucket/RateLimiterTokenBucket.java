package com.hongyan.study.ratelimiter.tokenbucket;

import com.google.common.util.concurrent.RateLimiter;

/**
 * @author zy
 * @version 1.0
 * @date Created in 2023/2/15 5:59 下午
 * @description 令牌桶限流算法
 */
public class RateLimiterTokenBucket {

    /**
     * 限流阈值
     */
    private final static Integer qps = 2;

    /**
     * 创建令牌添加时间间隔为
     */
    private static RateLimiter rateLimiter = RateLimiter.create(qps);

    /**
     * 限流:尝试获取限流令牌
     * @return
     */
    public static boolean tryAcquire() {
        return rateLimiter.tryAcquire();
    }
}
