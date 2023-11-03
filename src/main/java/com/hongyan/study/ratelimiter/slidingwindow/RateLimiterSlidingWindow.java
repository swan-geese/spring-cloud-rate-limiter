package com.hongyan.study.ratelimiter.slidingwindow;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zy
 * @version 1.0
 * @date Created in 2023/2/1 6:35 下午
 * @description 滑动窗口限流算法
 * 动窗口算法是对固定窗口/计数器算法的改进。既然固定窗口算法在遇到时间窗口的临界突变时会有问题，那么我们在遇到下一个时间窗口前也调整时间窗口不就可以了吗？
 * 可以发现窗口滑动的间隔越短，时间窗口的临界突变问题发生的概率也就越小，不过只要有时间窗口的存在，还是有可能发生时间窗口的临界突变问题。
 *
 * #
 */
public class RateLimiterSlidingWindow {
    /**
     * 时间窗口
     */
    private static final long WINDOW_TIME = 1000;
    /**
     * 限流阈值
     */
    private static final int QPS = 2;
    /**
     * 窗口大小
     */
    private static final int WINDOW_SIZE = 10;

    /**
     * 窗口列表
     */
    private static WindowInfo[] windowInfos = new WindowInfo[WINDOW_SIZE];

    /**
     * 初始化窗口列表
     * @param size
     */
    public static void init(int size) {
        for (int i = 0; i < size; i++) {
            windowInfos[i] = new WindowInfo(System.currentTimeMillis(), new AtomicInteger(0));
        }
    }

    /**
     * 1. 计算当前时间窗口
     * 2. 更新当前窗口计数 & 重置过期窗口计数
     * 3. 当前 QPS 是否超过限制
     * @return
     */
    public synchronized static boolean tryAcquire() {
        // 计算当前时间窗口
        int index = (int) ((System.currentTimeMillis() / WINDOW_TIME) % WINDOW_SIZE);
//        int index = (int)(System.currentTimeMillis() % WINDOW_TIME / (WINDOW_TIME / WINDOW_SIZE));
        // 更新当前窗口计数 & 重置过期窗口计数
        for (int i = 0; i < WINDOW_SIZE; i++) {
            WindowInfo windowInfo = windowInfos[i];
            if (i == index) {
                windowInfo.getNumber().incrementAndGet();
            } else {
                windowInfo.setTime(System.currentTimeMillis());
                windowInfo.getNumber().set(0);
            }
        }
        // 当前 QPS 是否超过限制
        return windowInfos[index].getNumber().get() <= QPS;
    }

}
