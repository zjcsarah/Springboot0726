package com.example.test.arithMath;

import java.util.*;

/**
 * 问题：给定一个只包含 '('、')'、'{'、'}'、'['、']' 的字符串，判断字符串中的括号是否有效。
 *       有效条件：左括号必须用相同类型的右括号闭合，且闭合顺序必须正确。
 *
 * 示例输入：s = "()[]{}"  → true
 *          s = "([)]"    → false
 *
 * 解答过程：
 * 1. 使用栈（Stack）数据结构，利用其后进先出的特性匹配括号。
 * 2. 遍历字符串中的每个字符：
 *      a. 若是左括号（'('、'{'、'['），压入栈中；
 *      b. 若是右括号，检查栈顶：
 *          - 栈为空 → 没有对应的左括号 → 无效；
 *          - 栈顶元素与当前右括号不匹配 → 无效；
 *          - 匹配成功 → 弹出栈顶元素。
 * 3. 遍历结束后，栈为空则全部匹配成功，否则有未闭合的左括号。
 *
 * 时间复杂度：O(n)，遍历一次字符串。
 * 空间复杂度：O(n)，栈最坏存储所有字符。
 */
public class ValidParentheses {

    public static void main(String[] args) {
        String[] tests = {"()[]{}", "([)]", "{[]}", "((()))", "(()"};
        for (String s : tests) {
            System.out.println("\"" + s + "\" → " + isValid(s));
        }
    }

    public static boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        for (char c : s.toCharArray()) {
            // 左括号压栈
            if (c == '(' || c == '{' || c == '[') {
                stack.push(c);
            } else {
                // 右括号：栈为空则无效
                if (stack.isEmpty()) return false;
                char top = stack.pop();
                // 检查括号类型是否匹配
                if (c == ')' && top != '(') return false;
                if (c == '}' && top != '{') return false;
                if (c == ']' && top != '[') return false;
            }
        }
        return stack.isEmpty(); // 栈为空则全部匹配
    }
}
