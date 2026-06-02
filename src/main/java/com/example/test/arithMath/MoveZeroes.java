package com.example.test.arithMath;

import java.util.Arrays;

/**
 * 问题：给定一个数组，将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。必须在原数组上操作。
 *
 * 示例输入：nums = {0, 1, 0, 3, 12}
 * 示例输出：[1, 3, 12, 0, 0]
 *
 * 解答过程：
 * 1. 双指针法：一个指针 slow 指向下一个非零元素应该放置的位置。
 * 2. 遍历数组：
 *      a. 遇到非零元素时，将其与 slow 位置的元素交换（或直接覆盖），然后 slow++；
 *      b. 遇到 0 则跳过。
 * 3. 遍历完成后：
 *      a. slow 之前全是非零元素（保持原顺序）；
 *      b. 将 slow 之后的位置全部填 0。
 *
 * 时间复杂度：O(n)，遍历一次。
 * 空间复杂度：O(1)，原地操作。
 */
public class MoveZeroes {

    public static void main(String[] args) {
        int[] nums = {0, 1, 0, 3, 12};
        System.out.println("原数组: " + Arrays.toString(nums));
        moveZeroes(nums);
        System.out.println("移动后: " + Arrays.toString(nums));
    }

    public static void moveZeroes(int[] nums) {
        int slow = 0; // 下一个非零元素应放的位置
        // 将所有非零元素移到前面
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                nums[slow++] = nums[i];
            }
        }
        // 剩余位置填 0
        while (slow < nums.length) {
            nums[slow++] = 0;
        }
    }
}
