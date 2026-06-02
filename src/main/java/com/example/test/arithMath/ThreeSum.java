package com.example.test.arithMath;

import java.util.*;

/**
 * 问题：给定一个整数数组，找出所有和为 0 且不重复的三元组 (a, b, c)。
 *
 * 示例输入：nums = {-1, 0, 1, 2, -1, -4}
 * 示例输出：[[-1, -1, 2], [-1, 0, 1]]
 *
 * 解答过程：
 * 1. 排序 + 双指针法（避免 O(n³) 暴力）：
 * 2. 首先将数组升序排序，便于去重和使用双指针。
 * 3. 固定第一个数 nums[i]，然后在 i+1 到末尾的区间内使用双指针找两数之和为 -nums[i]：
 *      a. left = i + 1, right = nums.length - 1；
 *      b. sum = nums[i] + nums[left] + nums[right]：
 *          - sum == 0 → 找到一个三元组，加入结果，同时左右指针向内收缩并跳过重复值；
 *          - sum < 0  → left++（需要更大的数）；
 *          - sum > 0  → right--（需要更小的数）。
 * 4. 跳过重复的 nums[i] 以避免结果中有重复三元组。
 *
 * 时间复杂度：O(n²)，外层 O(n)，内层双指针 O(n)。
 * 空间复杂度：O(1)（不计返回结果），排序使用 O(log n) 栈空间。
 */
public class ThreeSum {

    public static void main(String[] args) {
        int[] nums = {-1, 0, 1, 2, -1, -4};
        System.out.println("数组: " + Arrays.toString(nums));
        List<List<Integer>> result = threeSum(nums);
        System.out.println("和为0的三元组: " + result);
    }

    public static List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums); // 先排序
        for (int i = 0; i < nums.length - 2; i++) {
            // 跳过重复的固定元素
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            int left = i + 1, right = nums.length - 1;
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                if (sum == 0) {
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    // 跳过重复的 left 和 right
                    while (left < right && nums[left] == nums[left + 1]) left++;
                    while (left < right && nums[right] == nums[right - 1]) right--;
                    left++;
                    right--;
                } else if (sum < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        return result;
    }
}
