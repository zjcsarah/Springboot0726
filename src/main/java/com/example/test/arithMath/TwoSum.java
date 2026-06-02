package com.example.test.arithMath;

import java.util.*;

/**
 * 问题：给定一个整数数组 nums 和一个目标值 target，找出数组中和为目标值的两个数的下标。
 *       假设每种输入只会对应一个答案，且同一个元素不能使用两次。
 *
 * 示例输入：nums = {2, 7, 11, 15}, target = 9
 * 示例输出：[0, 1]  （因为 nums[0] + nums[1] = 2 + 7 = 9）
 *
 * 解答过程：
 * 1. 暴力法：双重循环遍历所有组合，O(n²)，不推荐。
 * 2. 优化 —— 使用 HashMap 以空间换时间：
 *      a. 遍历数组，对于当前元素 nums[i]，计算 complement = target - nums[i]；
 *      b. 检查 complement 是否已在 HashMap 中：
 *          - 若存在，说明之前某个元素与当前元素之和等于 target，返回两者下标；
 *          - 若不存在，将当前元素及其下标存入 HashMap，继续遍历。
 * 3. 这样只需一次遍历即可找到答案。
 *
 * 时间复杂度：O(n)，只需遍历一次数组。
 * 空间复杂度：O(n)，HashMap 最坏存储所有元素。
 */
public class TwoSum {

    public static void main(String[] args) {
        int[] nums = {2, 7, 11, 15};
        int target = 9;
        int[] result = twoSum(nums, target);
        System.out.println("两数之和下标: [" + result[0] + ", " + result[1] + "]");
        System.out.println("验证: " + nums[result[0]] + " + " + nums[result[1]] + " = " + target);
    }

    public static int[] twoSum(int[] nums, int target) {
        // key=元素值, value=下标
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[]{map.get(complement), i};
            }
            map.put(nums[i], i);
        }
        return new int[]{-1, -1}; // 未找到
    }
}
