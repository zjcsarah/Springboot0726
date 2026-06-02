package com.example.test.arithMath;

/**
 * 问题：给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现一次的元素。
 *
 * 示例输入：nums = {4, 1, 2, 1, 2}
 * 示例输出：4
 *
 * 解答过程：
 * 1. 使用异或运算（XOR）的性质：
 *      - a ^ a = 0（相同数异或为0）
 *      - a ^ 0 = a（任何数与0异或为自身）
 *      - 异或满足交换律和结合律
 * 2. 将数组中所有元素进行异或：
 *      - 成对出现的元素异或后变为 0；
 *      - 最终结果就是只出现一次的元素。
 * 3. 如：4 ^ 1 ^ 2 ^ 1 ^ 2 = 4 ^ (1 ^ 1) ^ (2 ^ 2) = 4 ^ 0 ^ 0 = 4
 *
 * 时间复杂度：O(n)，一次遍历。
 * 空间复杂度：O(1)，只用一个变量。
 */
public class SingleNumber {

    public static void main(String[] args) {
        int[] nums = {4, 1, 2, 1, 2};
        System.out.println("数组: " + java.util.Arrays.toString(nums));
        System.out.println("只出现一次的数字: " + singleNumber(nums));
    }

    public static int singleNumber(int[] nums) {
        int result = 0;
        for (int num : nums) {
            result ^= num; // 异或：相同消为0，不同保留
        }
        return result;
    }
}
