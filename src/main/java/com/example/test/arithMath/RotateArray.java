package com.example.test.arithMath;

import java.util.*;

/**
 * 问题：给定一个数组，将数组中的元素向右旋转 k 个位置，其中 k 是非负数。要求原地修改。
 *
 * 示例输入：nums = {1, 2, 3, 4, 5, 6, 7}, k = 3
 * 示例输出：[5, 6, 7, 1, 2, 3, 4]
 *
 * 解答过程：
 * 1. 三次反转法（无需额外数组）：
 *      - k 可能大于数组长度，先取模：k = k % n；
 *      - 步骤一：反转整个数组 → [7, 6, 5, 4, 3, 2, 1]；
 *      - 步骤二：反转前 k 个元素 → [5, 6, 7, 4, 3, 2, 1]；
 *      - 步骤三：反转剩余 n-k 个元素 → [5, 6, 7, 1, 2, 3, 4]。
 * 2. 反转函数使用双指针，左右交换直到相遇。
 *
 * 时间复杂度：O(n)，每个元素被反转两次（常数倍）。
 * 空间复杂度：O(1)，原地操作。
 */
public class RotateArray {

    public static void main(String[] args) {
        int[] nums = {1, 2, 3, 4, 5, 6, 7};
        int k = 3;
        System.out.println("原数组: " + Arrays.toString(nums) + ", k = " + k);
        rotate(nums, k);
        System.out.println("旋转后: " + Arrays.toString(nums));
    }

    public static void rotate(int[] nums, int k) {
        int n = nums.length;
        k = k % n; // 处理 k > n 的情况
        // 三次反转
        reverse(nums, 0, n - 1);       // 整体反转
        reverse(nums, 0, k - 1);       // 反转前 k 个
        reverse(nums, k, n - 1);       // 反转剩余部分
    }

    private static void reverse(int[] nums, int left, int right) {
        while (left < right) {
            int temp = nums[left];
            nums[left] = nums[right];
            nums[right] = temp;
            left++;
            right--;
        }
    }
}
