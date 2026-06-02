package com.example.test.arithMath;

/**
 * 问题：给定一个字符串数组，找出所有字符串的最长公共前缀。如果不存在，返回空字符串 ""。
 *
 * 示例输入：strs = {"flower", "flow", "flight"}
 * 示例输出："fl"
 *
 * 解答过程：
 * 1. 纵向扫描法：逐个字符位置进行比较。
 * 2. 以第一个字符串为基准：
 *      a. 遍历第一个字符串的每个字符位置 i；
 *      b. 对于该位置的字符 c，检查其余字符串在第 i 位是否也是 c；
 *      c. 如果某个字符串长度不足 i+1，或者字符不匹配，返回当前已累积的前缀；
 *      d. 如果全部匹配，将该字符追加到结果中。
 * 3. 遍历完第一个字符串后返回累积的结果。
 *
 * 时间复杂度：O(S)，S 为所有字符串的字符总数（最坏情况）。
 * 空间复杂度：O(1)，不计算返回结果的开销。
 */
public class LongestCommonPrefix {

    public static void main(String[] args) {
        String[] strs = {"flower", "flow", "flight"};
        System.out.println("数组: " + java.util.Arrays.toString(strs));
        System.out.println("最长公共前缀: \"" + longestCommonPrefix(strs) + "\"");
    }

    public static String longestCommonPrefix(String[] strs) {
        if (strs == null || strs.length == 0) return "";
        // 以第一个字符串为基准
        for (int i = 0; i < strs[0].length(); i++) {
            char c = strs[0].charAt(i);
            // 检查其余字符串的第 i 位
            for (int j = 1; j < strs.length; j++) {
                if (i >= strs[j].length() || strs[j].charAt(i) != c) {
                    return strs[0].substring(0, i);
                }
            }
        }
        return strs[0];
    }
}
