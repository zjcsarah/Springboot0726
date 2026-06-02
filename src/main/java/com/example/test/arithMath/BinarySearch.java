package com.example.test.arithMath;

/**
 * 问题：给定一个升序排列的整数数组和一个目标值，使用二分查找找到目标值的下标。
 *       如果目标值不存在，返回 -1。
 *
 * 示例输入：nums = {1, 3, 5, 7, 9, 11}, target = 7
 * 示例输出：3
 *
 * 解答过程：
 * 1. 二分查找适用于有序数组，每次将搜索范围缩小一半。
 * 2. 定义左右指针 left = 0, right = nums.length - 1：
 *      a. 计算中间位置 mid = left + (right - left) / 2（防溢出）；
 *      b. 若 nums[mid] == target → 找到，返回 mid；
 *      c. 若 nums[mid] < target  → 目标在右半部分，left = mid + 1；
 *      d. 若 nums[mid] > target  → 目标在左半部分，right = mid - 1。
 * 3. 循环直到 left > right，说明目标不存在。
 *
 * 时间复杂度：O(log n)，每次搜索范围减半。
 * 空间复杂度：O(1)，只用了几个指针变量。
 */
public class BinarySearch {

    public static void main(String[] args) {
        int[] nums = {1, 3, 5, 7, 9, 11};
        int target = 7;
        System.out.println("数组: " + java.util.Arrays.toString(nums));
        System.out.println("目标 " + target + " 的下标: " + binarySearch(nums, target));
        System.out.println("目标 4 的下标: " + binarySearch(nums, 4) + " (不存在)");
    }

    public static int binarySearch(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2; // 防止 (left+right) 溢出
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                left = mid + 1; // 目标在右侧
            } else {
                right = mid - 1;// 目标在左侧
            }
        }
        return -1; // 未找到
    }
}
