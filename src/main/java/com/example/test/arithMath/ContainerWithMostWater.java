package com.example.test.arithMath;

import java.util.*;

/**
 * 问题：给定 n 个非负整数 a1, a2, ..., an，每个数代表坐标中的一个点 (i, ai)。
 *       画 n 条垂直线，找出其中两条线，使它们与 x 轴共同构成的容器可以容纳最多的水。
 *       注意：不能倾斜容器，容器的高度由较短的线决定。
 *
 * 示例输入：height = {1, 8, 6, 2, 5, 4, 8, 3, 7}
 * 示例输出：49  （由高度为 8 的线（下标1）和高度为 7 的线（下标8）组成，宽度=7，面积=7*7=49）
 *
 * 解答过程：
 * 1. 双指针法：left 指向最左，right 指向最右。
 * 2. 面积由较短的线决定：area = min(height[left], height[right]) * (right - left)。
 * 3. 每次计算当前面积并更新最大值。
 * 4. 移动较短的线（因为移动较长的线不会增加面积，而移动较短的线有可能找到更高的线）：
 *      - 若 height[left] < height[right] → left++
 *      - 否则 → right--
 * 5. 重复直到 left >= right。
 *
 * 时间复杂度：O(n)，左右指针各走一次。
 * 空间复杂度：O(1)。
 */
public class ContainerWithMostWater {

    public static void main(String[] args) {
        int[] height = {1, 8, 6, 2, 5, 4, 8, 3, 7};
        System.out.println("高度数组: " + Arrays.toString(height));
        System.out.println("最大盛水量: " + maxArea(height));
    }

    public static int maxArea(int[] height) {
        int left = 0, right = height.length - 1;
        int maxArea = 0;
        while (left < right) {
            int h = Math.min(height[left], height[right]); // 容器高度由短板决定
            int w = right - left;                          // 宽度
            maxArea = Math.max(maxArea, h * w);
            // 移动较短的线
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return maxArea;
    }
}
