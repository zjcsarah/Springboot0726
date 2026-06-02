package com.example.test.arithMath;

/**
 * 问题：求斐波那契数列的第 n 项。斐波那契数列定义：F(0)=0, F(1)=1, F(n)=F(n-1)+F(n-2)。
 *
 * 示例输入：n = 10
 * 示例输出：55  （数列：0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55）
 *
 * 解答过程：
 * 1. 递归法（不推荐）：大量重复计算，O(2ⁿ) 时间。
 * 2. 动态规划（迭代法）：
 *      a. 用两个变量 prev2（F(n-2)）和 prev1（F(n-1)）滚动计算；
 *      b. 初始：prev2 = 0, prev1 = 1；
 *      c. 从 2 遍历到 n：cur = prev1 + prev2，然后 prev2 = prev1, prev1 = cur；
 *      d. n=0 直接返回 0，n=1 直接返回 1。
 *
 * 时间复杂度：O(n)，一次遍历。
 * 空间复杂度：O(1)，只用了两个变量滚动。
 */
public class Fibonacci {

    public static void main(String[] args) {
        int n = 10;
        System.out.println("斐波那契数列第 " + n + " 项: " + fib(n));
        // 打印前10项
        System.out.print("前10项: ");
        for (int i = 0; i <= n; i++) {
            System.out.print(fib(i) + (i < n ? ", " : ""));
        }
        System.out.println();
    }

    public static int fib(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        int prev2 = 0; // F(0)
        int prev1 = 1; // F(1)
        int cur = 0;
        for (int i = 2; i <= n; i++) {
            cur = prev1 + prev2;
            prev2 = prev1;
            prev1 = cur;
        }
        return cur;
    }
}
