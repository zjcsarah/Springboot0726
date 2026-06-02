package com.example.test.arithMath;

/**
 * 问题：给定一个整数数组，找出具有最大和的连续子数组（至少包含一个元素），返回其最大和。
 *
 * 示例输入：nums = {-2, 1, -3, 4, -1, 2, 1, -5, 4}
 * 示例输出：6  （子数组 [4, -1, 2, 1] 的和）
 *
 * 解答过程：
 * 1. 使用 Kadane 算法（动态规划）：
 *      - 定义 dp[i] 表示以 nums[i] 结尾的最大子数组和；
 *      - 状态转移：dp[i] = max(nums[i], dp[i-1] + nums[i])；
 *         即要么从当前元素重新开始，要么将当前元素追加到之前的子数组后面。
 * 2. 优化空间 —— 只需一个变量 curMax 记录以当前位置结尾的最大和：
 *      a. curMax = Math.max(num, curMax + num)；
 *      b. 每次更新全局最大值 globalMax = Math.max(globalMax, curMax)。
 *
 * 时间复杂度：O(n)，一次遍历。
 * 空间复杂度：O(1)，只用了两个变量。
 */
public class MaximumSubarray {

    public static void main(String[] args) {
        int[] nums = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        System.out.println("数组: " + java.util.Arrays.toString(nums));
        System.out.println("最大子数组和: " + maxSubArray(nums));
    }

    public static int maxSubArray(int[] nums) {
        int curMax = nums[0];   // 以当前位置结尾的最大和
        int globalMax = nums[0];// 全局最大和
        for (int i = 1; i < nums.length; i++) {
            // 决策：要么从当前元素重新开始，要么追加到之前的子数组
            curMax = Math.max(nums[i], curMax + nums[i]);
            globalMax = Math.max(globalMax, curMax);
        }
        return globalMax;
    }
}
