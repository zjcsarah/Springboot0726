package com.example.test.arithMath;

import java.util.*;

/**
 * 问题：给定一个大小为 n 的数组，找出其中的多数元素。多数元素是指在数组中出现次数大于 n/2 的元素。
 *       假设数组非空，且多数元素一定存在。
 *
 * 示例输入：nums = {2, 2, 1, 1, 1, 2, 2}
 * 示例输出：2
 *
 * 解答过程：
 * 1. Boyer-Moore 投票算法：
 *      - 核心思想：多数元素的出现次数 > 其他所有元素出现次数之和。
 *      - 因此可以通过"对抗消耗"的方式找到多数元素。
 * 2. 算法步骤：
 *      a. 初始化 candidate = nums[0], count = 1；
 *      b. 从下标 1 开始遍历：
 *          - 若 count == 0：将当前元素设为新 candidate，count = 1；
 *          - 若当前元素 == candidate：count++；
 *          - 若当前元素 != candidate：count--（用不同元素"抵消"一个投票）。
 *      c. 最终 candidate 即为多数元素。
 *
 * 时间复杂度：O(n)，一次遍历。
 * 空间复杂度：O(1)，两个变量。
 */
public class MajorityElement {

    public static void main(String[] args) {
        int[] nums = {2, 2, 1, 1, 1, 2, 2};
        System.out.println("数组: " + Arrays.toString(nums));
        System.out.println("多数元素: " + majorityElement(nums));
    }

    public static int majorityElement(int[] nums) {
        int candidate = nums[0];
        int count = 1;
        for (int i = 1; i < nums.length; i++) {
            if (count == 0) {
                candidate = nums[i]; // 换候选人
                count = 1;
            } else if (nums[i] == candidate) {
                count++; // 支持
            } else {
                count--; // 反对（抵消）
            }
        }
        return candidate;
    }
}
