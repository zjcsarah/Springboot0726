package com.example.test.arithMath;

/**
 * 问题：给定一个字符串 s，找出其中最长的回文子串。回文串是指正读和反读都一样的字符串。
 *
 * 示例输入：s = "babad"
 * 示例输出："bab" 或 "aba"
 *
 * 解答过程：
 * 1. 中心扩展法：回文串是对称的，可以从中心向两边扩展。
 * 2. 遍历每个字符位置作为中心（考虑奇数长度和偶数长度两种情况）：
 *      a. 奇数长度中心：以 s[i] 为中心向两边扩展；
 *      b. 偶数长度中心：以 s[i] 和 s[i+1] 之间为中心向两边扩展。
 * 3. 每次扩展找到以当前中心的最长回文子串，更新全局最长结果。
 * 4. 扩展条件：左右指针不越界，且 s[left] == s[right]。
 *
 * 时间复杂度：O(n²)，每个中心最多扩展 O(n)。
 * 空间复杂度：O(1)，只记录起止位置。
 */
public class LongestPalindrome {

    public static void main(String[] args) {
        String s = "babad";
        System.out.println("字符串: \"" + s + "\"");
        System.out.println("最长回文子串: \"" + longestPalindrome(s) + "\"");
    }

    public static String longestPalindrome(String s) {
        if (s == null || s.length() < 2) return s;
        int start = 0, maxLen = 0;
        for (int i = 0; i < s.length(); i++) {
            // 奇数长度扩展
            int len1 = expandAroundCenter(s, i, i);
            // 偶数长度扩展
            int len2 = expandAroundCenter(s, i, i + 1);
            int len = Math.max(len1, len2);
            if (len > maxLen) {
                maxLen = len;
                start = i - (len - 1) / 2; // 计算起始位置
            }
        }
        return s.substring(start, start + maxLen);
    }

    /**
     * 从中心向两边扩展，返回回文子串长度
     */
    private static int expandAroundCenter(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        return right - left - 1; // 注意：此时 left 和 right 已多走了一步
    }
}
