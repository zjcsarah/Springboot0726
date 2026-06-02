package com.example.test.arithMath;

/**
 * 问题：给定一个数组 prices，其中 prices[i] 表示某只股票第 i 天的价格。
 *       你只能选择某一天买入，并在未来的某一天卖出。求能获得的最大利润。如果无法获利则返回 0。
 *
 * 示例输入：prices = {7, 1, 5, 3, 6, 4}
 * 示例输出：5  （第2天买入价格=1，第5天卖出价格=6，利润=5）
 *
 * 解答过程：
 * 1. 一次遍历，动态维护两个值：
 *      - minPrice：到当前天为止的最低买入价；
 *      - maxProfit：到当前天为止的最大利润。
 * 2. 遍历每一天的价格：
 *      a. 若当天价格 < minPrice，更新 minPrice（找到更低的买入点）；
 *      b. 否则计算当天卖出能获得的利润（price - minPrice），尝试更新 maxProfit。
 *
 * 时间复杂度：O(n)，一次遍历。
 * 空间复杂度：O(1)，两个变量。
 */
public class BestTimeToBuyAndSellStock {

    public static void main(String[] args) {
        int[] prices = {7, 1, 5, 3, 6, 4};
        System.out.println("价格数组: " + java.util.Arrays.toString(prices));
        System.out.println("最大利润: " + maxProfit(prices));
    }

    public static int maxProfit(int[] prices) {
        if (prices == null || prices.length == 0) return 0;
        int minPrice = prices[0]; // 最低买入价
        int maxProfit = 0;        // 最大利润
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] < minPrice) {
                minPrice = prices[i]; // 更新最低价
            } else {
                int profit = prices[i] - minPrice;
                maxProfit = Math.max(maxProfit, profit);
            }
        }
        return maxProfit;
    }
}
