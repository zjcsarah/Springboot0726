package com.example.test.arithMath;

import java.util.*;

/**
 * 问题：给定两个整数数组，计算它们的交集。结果中每个元素出现的次数，应与它在两个数组中同时出现次数的最小值一致。
 *
 * 示例输入：nums1 = {1, 2, 2, 1}, nums2 = {2, 2}
 * 示例输出：[2, 2]
 *
 * 解答过程：
 * 1. 使用 HashMap 统计第一个数组中每个数字的出现次数。
 * 2. 遍历第二个数组：
 *      a. 若当前数字在 HashMap 中且次数 > 0，说明它是交集元素；
 *      b. 将其加入结果列表，并将 HashMap 中对应次数减 1；
 *      c. 若次数减到 0，从 Map 中删除该 key。
 * 3. 最后将结果列表转为数组返回。
 *
 * 时间复杂度：O(m + n)，m 和 n 分别为两个数组长度。
 * 空间复杂度：O(min(m, n))，HashMap 存储较小数组的元素。
 */
public class IntersectionOfTwoArrays {

    public static void main(String[] args) {
        int[] nums1 = {1, 2, 2, 1};
        int[] nums2 = {2, 2};
        System.out.println("数组1: " + Arrays.toString(nums1));
        System.out.println("数组2: " + Arrays.toString(nums2));
        int[] result = intersect(nums1, nums2);
        System.out.println("交集: " + Arrays.toString(result));
    }

    public static int[] intersect(int[] nums1, int[] nums2) {
        // 统计 nums1 中每个数字的出现次数
        Map<Integer, Integer> countMap = new HashMap<>();
        for (int num : nums1) {
            countMap.put(num, countMap.getOrDefault(num, 0) + 1);
        }
        List<Integer> result = new ArrayList<>();
        // 遍历 nums2，找交集
        for (int num : nums2) {
            if (countMap.getOrDefault(num, 0) > 0) {
                result.add(num);
                countMap.put(num, countMap.get(num) - 1); // 次数减一
            }
        }
        // 转为数组
        int[] arr = new int[result.size()];
        for (int i = 0; i < result.size(); i++) {
            arr[i] = result.get(i);
        }
        return arr;
    }
}
