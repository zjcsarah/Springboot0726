package com.example.test.arithMath;

/**
 * 问题：假设你正在爬楼梯，需要 n 阶才能到达楼顶。每次可以爬 1 或 2 个台阶。
 *       有多少种不同的方法可以爬到楼顶？
 *
 * 示例输入：n = 5
 * 示例输出：8  （方法：1+1+1+1+1, 1+1+1+2, 1+1+2+1, 1+2+1+1, 1+2+2, 2+1+1+1, 2+1+2, 2+2+1）
 *
 * 解答过程：
 * 1. 本质是斐波那契数列：到达第 n 阶的方法数 = 到达第 n-1 阶的方法数 + 到达第 n-2 阶的方法数。
 *      - 从第 n-1 阶走 1 步到 n；
 *      - 从第 n-2 阶走 2 步到 n。
 * 2. 边界条件：dp[1]=1（只有1种方式到第1阶），dp[2]=2（1+1 或 2）。
 * 3. 迭代计算 dp[i] = dp[i-1] + dp[i-2]，用两个变量滚动。
 *
 * 时间复杂度：O(n)，一次遍历。
 * 空间复杂度：O(1)，两个变量滚动。
 */
public class ClimbingStairs {

    public static void main(String[] args) {
        int n = 5;
        System.out.println(n + " 阶楼梯的爬法总数: " + climbStairs(n));
    }

    public static int climbStairs(int n) {
        if (n <= 2) return n;
        int prev2 = 1; // dp[1]
        int prev1 = 2; // dp[2]
        int cur = 0;
        for (int i = 3; i <= n; i++) {
            cur = prev1 + prev2;
            prev2 = prev1;
            prev1 = cur;
        }
        return cur;
    }
}
