package com.example.test.arithMath;

import java.util.Arrays;

/**
 * 问题：给定两个有序整数数组 nums1 和 nums2，将 nums2 合并到 nums1 中，使 nums1 成为一个有序数组。
 *       nums1 有足够的空间（尾部用 0 填充）容纳 nums2 的元素。
 *
 * 示例输入：nums1 = {1, 3, 5, 0, 0, 0} (m=3), nums2 = {2, 4, 6} (n=3)
 * 示例输出：[1, 2, 3, 4, 5, 6]
 *
 * 解答过程：
 * 1. 使用三指针法，从后往前填充，避免覆盖 nums1 中未处理的元素。
 * 2. 定义三个指针：
 *      - p1 = m - 1，指向 nums1 有效部分的末尾；
 *      - p2 = n - 1，指向 nums2 的末尾；
 *      - p  = m + n - 1，指向 nums1 的尾部（填充位置）。
 * 3. 从后往前比较 nums1[p1] 和 nums2[p2]，将较大者放入 nums1[p]，对应指针前移。
 * 4. 若 nums2 还有剩余元素，直接复制到 nums1 前面。
 *
 * 时间复杂度：O(m + n)，一次遍历。
 * 空间复杂度：O(1)，原地修改。
 */
public class MergeSortedArray {

    public static void main(String[] args) {
        int[] nums1 = {1, 3, 5, 0, 0, 0};
        int m = 3;
        int[] nums2 = {2, 4, 6};
        int n = 3;
        merge(nums1, m, nums2, n);
        System.out.println("合并后: " + Arrays.toString(nums1));
    }

    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        int p1 = m - 1;      // nums1 有效部分末尾
        int p2 = n - 1;      // nums2 末尾
        int p = m + n - 1;   // 填充位置
        // 从后往前填充
        while (p1 >= 0 && p2 >= 0) {
            nums1[p--] = (nums1[p1] > nums2[p2]) ? nums1[p1--] : nums2[p2--];
        }
        // 若 nums2 还有剩余，直接复制（nums1 有剩余则无需处理，已在正确位置）
        while (p2 >= 0) {
            nums1[p--] = nums2[p2--];
        }
    }
}
