package com.example.test.arithMath;

import java.util.Arrays;

/**
 * 问题：给定一个升序排列的整数数组，原地删除重复出现的元素，使每个元素只出现一次，
 *       返回移除后数组的新长度。不要使用额外的数组空间。
 *
 * 示例输入：nums = {1, 1, 2, 3, 3, 4, 5, 5}
 * 示例输出：长度为 5，前5个元素为 [1, 2, 3, 4, 5]
 *
 * 解答过程：
 * 1. 使用快慢双指针法：
 *      - slow 指针指向当前不重复区间的末尾；
 *      - fast 指针遍历整个数组。
 * 2. 初始 slow = 0：
 *      a. fast 从 1 开始遍历；
 *      b. 当 nums[fast] != nums[slow] 时，说明遇到新元素：
 *          - slow 前进一步，将 nums[fast] 赋值给 nums[slow]；
 *      c. 若相等，fast 继续前进。
 * 3. 最终 slow + 1 即为不重复元素个数。
 *
 * 时间复杂度：O(n)，遍历一次数组。
 * 空间复杂度：O(1)，原地修改。
 */
public class RemoveDuplicates {

    public static void main(String[] args) {
        int[] nums = {1, 1, 2, 3, 3, 4, 5, 5};
        System.out.println("原数组: " + Arrays.toString(nums));
        int len = removeDuplicates(nums);
        System.out.println("新长度: " + len);
        System.out.print("去重后: [");
        for (int i = 0; i < len; i++) {
            System.out.print(nums[i] + (i < len - 1 ? ", " : ""));
        }
        System.out.println("]");
    }

    public static int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;
        int slow = 0; // 不重复区间的末尾
        for (int fast = 1; fast < nums.length; fast++) {
            if (nums[fast] != nums[slow]) {
                slow++;
                nums[slow] = nums[fast]; // 将新元素移到前面
            }
        }
        return slow + 1; // 新长度
    }
}
