package com.example.test.arithMath;

/**
 * 问题：实现一个函数，将字符串转换成整数（类似 C 语言的 atoi 函数）。
 *       规则：忽略前导空格 → 识别正负号 → 读取连续数字 → 处理溢出（返回 INT_MAX 或 INT_MIN）。
 *
 * 示例输入：s = "   -42"
 * 示例输出：-42
 *
 * 解答过程：
 * 1. 跳过前导空格。
 * 2. 判断正负号（'+' 或 '-'），默认正数。
 * 3. 逐字符读取数字，构建整数值：
 *      a. result = result * 10 + digit；
 *      b. 每次累加前检查是否会溢出：
 *          - 若 result > Integer.MAX_VALUE / 10，或 result == Integer.MAX_VALUE/10 且 digit > 7 → 溢出；
 *          - 正数溢出返回 Integer.MAX_VALUE，负数溢出返回 Integer.MIN_VALUE。
 * 4. 返回带符号的结果。
 *
 * 时间复杂度：O(n)，遍历一次字符串。
 * 空间复杂度：O(1)。
 */
public class StringToInteger {

    public static void main(String[] args) {
        String[] tests = {"   -42", "4193 with words", "words and 987", "-91283472332", "  +0 123"};
        for (String s : tests) {
            System.out.println("\"" + s + "\" → " + myAtoi(s));
        }
    }

    public static int myAtoi(String s) {
        if (s == null || s.length() == 0) return 0;
        int i = 0, n = s.length();
        // 1. 跳过前导空格
        while (i < n && s.charAt(i) == ' ') i++;
        if (i >= n) return 0;
        // 2. 判断正负号
        int sign = 1;
        if (s.charAt(i) == '+' || s.charAt(i) == '-') {
            sign = (s.charAt(i) == '-') ? -1 : 1;
            i++;
        }
        // 3. 读取数字
        int result = 0;
        while (i < n && Character.isDigit(s.charAt(i))) {
            int digit = s.charAt(i) - '0';
            // 4. 溢出检查
            if (result > Integer.MAX_VALUE / 10 ||
                    (result == Integer.MAX_VALUE / 10 && digit > 7)) {
                return (sign == 1) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            }
            result = result * 10 + digit;
            i++;
        }
        return result * sign;
    }
}
