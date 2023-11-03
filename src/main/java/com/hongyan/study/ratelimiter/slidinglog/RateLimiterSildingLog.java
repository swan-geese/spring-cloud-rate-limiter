package com.hongyan.study.ratelimiter.slidinglog;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author zy
 * @version 1.0
 * @date Created in 2023/2/14 6:35 下午
 * @description 滑动日志算法
 * 滑动日志算法是实现限流的另一种方法，这种方法比较简单。
 * 基本逻辑就是记录下所有的请求时间点，新请求到来时先判断最近指定时间范围内的请求数量是否超过指定阈值，
 * 由此来确定是否达到限流，这种方式没有了时间窗口突变的问题，限流比较准确，但是因为要记录下每次请求的时间点，所以占用的内存较多。
 *
 * 滑动日志方式限流
 * 设置 QPS 为 2.
 *
 * 因为滑动日志要每次请求单独存储一条记录，可能占用内存过多。所以下面这个实现其实不算严谨的滑动日志，更像一个把 1 秒时间切分成 1000 个时间窗口的滑动窗口算法
 */
public class RateLimiterSildingLog {

    /**
     * 阀值
     */
    private Integer qps = 2;

    /**
     * 记录请求的时间戳和数量
     */
    private TreeMap<Long, Long> treeMap = new TreeMap<>();

    /**
     * 清理记录时间间隔，默认60s
     */
    private Long cleanTime = 60 * 1000L;

    public RateLimiterSildingLog(Integer qps) {
        this.qps = qps;
    }

    /**
     * 限流:尝试获取限流令牌
     */
    public synchronized boolean tryAcquire() {
        long now = System.currentTimeMillis();
        // 清理过期的数据老数据，最长 60 秒清理一次
        long beforeTime  = now - 1000;
        if (!treeMap.isEmpty() && treeMap.firstKey() < now - cleanTime) {
            Set<Long> keySet = new HashSet<>(treeMap.subMap(0L, beforeTime).keySet());
            for (Long key : keySet) {
                treeMap.remove(key);
            }
        }
        //计算当前请求次数
        int sum = 0;
        for (Long value : treeMap.subMap(beforeTime, now).values()) {
            sum += value;
        }
        //超过QPS限制，直接返回 false
        if (sum >= qps) {
            return false;
        }
        //记录本次请求
        if (treeMap.containsKey(now)) {
            //method1：对数据进行+1操作
//                treeMap.put(now, treeMap.get(now) + 1);
            //method2：对数据进行+1操作
            treeMap.compute(now, (k, v) -> v + 1);
        } else {
            treeMap.put(now, 1L);
        }
        return sum <= qps;

    }


}
