package com.example.test.arithMath;

import java.util.*;

/**
 * 问题：给定一个只包含数字 2-9 的字符串，返回它能表示的所有字母组合。
 *       数字到字母的映射与电话按键相同（九宫格输入法）。
 *       映射：2→"abc", 3→"def", 4→"ghi", 5→"jkl", 6→"mno", 7→"pqrs", 8→"tuv", 9→"wxyz"
 *
 * 示例输入：digits = "23"
 * 示例输出：["ad","ae","af","bd","be","bf","cd","ce","cf"]
 *
 * 解答过程：
 * 1. 回溯法（DFS）：问题本质是求多个字符集合的笛卡尔积。
 * 2. 构建数字到字母的映射数组。
 * 3. 递归函数 backtrack(index, current)：
 *      a. 终止条件：index == digits.length()，将 current 加入结果；
 *      b. 获取当前数字对应的字母集合；
 *      c. 遍历每个字母，将其追加到 current，然后递归处理下一个数字；
 *      d. 递归返回后撤销当前字母（回溯）。
 *
 * 时间复杂度：O(3^m × 4^n)，m 和 n 分别是映射为3个字母和4个字母的数字个数。
 * 空间复杂度：O(m + n)，递归栈深度。
 */
public class LetterCombinations {

    // 数字到字母的映射（下标2-9）
    private static final String[] MAPPING = {
        "", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"
    };

    public static void main(String[] args) {
        String digits = "23";
        System.out.println("输入: \"" + digits + "\"");
        List<String> result = letterCombinations(digits);
        System.out.println("字母组合: " + result);
    }

    public static List<String> letterCombinations(String digits) {
        List<String> result = new ArrayList<>();
        if (digits == null || digits.length() == 0) return result;
        backtrack(digits, 0, new StringBuilder(), result);
        return result;
    }

    private static void backtrack(String digits, int index, StringBuilder current, List<String> result) {
        // 所有数字都已处理完
        if (index == digits.length()) {
            result.add(current.toString());
            return;
        }
        // 获取当前数字对应的字母
        String letters = MAPPING[digits.charAt(index) - '0'];
        for (char c : letters.toCharArray()) {
            current.append(c);                         // 选择
            backtrack(digits, index + 1, current, result); // 递归
            current.deleteCharAt(current.length() - 1); // 撤销（回溯）
        }
    }
}
