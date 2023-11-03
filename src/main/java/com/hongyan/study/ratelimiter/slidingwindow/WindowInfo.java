package com.hongyan.study.ratelimiter.slidingwindow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zy
 * @version 1.0
 * @date Created in 2023/2/1 6:36 下午
 * @description 窗口列表
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WindowInfo implements Serializable {

    /**
     * 窗口开始时间
     */
    private Long time;

    /**
     * 计数器
     */
    private AtomicInteger number;


}
