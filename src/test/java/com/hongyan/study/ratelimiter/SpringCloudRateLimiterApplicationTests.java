package com.hongyan.study.ratelimiter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedList;

@SpringBootTest
class SpringCloudRateLimiterApplicationTests {

    @Test
    void contextLoads() {
    }

    /**
     * 给定一个整数数组和一个整数 k，判断数组中是否存在两个不同的索引 i 和 j，使得 nums [i] = nums [j]，并且 i 和 j 的差的绝对值最大为 k。
     * 示例 1:
     * 输入: nums = [1,2,3,1], k = 3
     * 输出: true
     * 示例 2:
     * 输入: nums = [1,0,1,1], k = 1
     * 输出: true
     * 示例 3:
     * 输入: nums = [1,2,3,1,2,3], k = 2
     * 输出: false
     *
     * 链接：https://www.jianshu.com/p/e6622df3c377
     * @param nums
     * @param k
     * @return
     */
    public boolean containsStrSlidingWindow(int[] nums, int k){
        LinkedList<Integer> integers = new LinkedList<>();
        for (int i = 0; i < nums.length; i++) {

            if(integers.contains(nums[i])) {
                return true;
            }
            integers.add(nums[i]);

            if(integers.size() > k){
                integers.removeFirst();
            }
        }
        return false;
    }

    /**
     * 给定一个整数数组和一个整数 k，判断数组中是否存在两个不同的索引 i 和 j，使得 nums [i] = nums [j]，并且 i 和 j 的差的绝对值最大为 k
     */
    @Test
    public void containsStrSlidingWindowTest(){
        int[] nums = {1,2,3,1,2,3};
        int k = 2;
        System.out.println(containsStrSlidingWindow(nums,k));
    }


    @Test
    public void minLenSlidingWindowTest() {
        int[] nums = {2,3,1,2,4,3};
        int s = 7;
        System.out.println(minLenSlidingWindow(nums,s));
    }

    /**
     * 给定一个含有 n 个正整数的数组和一个正整数 s ，找出该数组中满足其和 ≥ s 的长度最小的连续子数组。如果不存在符合条件的连续子数组，返回 0；
     * 输入: s = 7, nums = [2,3,1,2,4,3]
     * 输出: 2
     * 解释: 子数组 [4,3] 是该条件下的长度最小的连续子数组
     * 链接：https://www.jianshu.com/p/e6622df3c377
     * @param nums
     * @param s
     * @return
     */
    public static int minLenSlidingWindow(int [] nums ,int s){
        int sum = 0;
        int res = nums.length;
        LinkedList<Integer> integers = new LinkedList<>();
        for (int i = 0; i < nums.length; i++) {
            integers.add(nums[i]);
            sum += nums[i];
//            如果 sum >= s 给res 赋值为 集合长度
//            循环删除0 位置数据，直到小于 s
            while (sum >= s){
                res = Math.min(integers.size(), res) ;
                sum -= integers.get(0);
                integers.remove(0);
            }


        }
        return res;
    }

}
