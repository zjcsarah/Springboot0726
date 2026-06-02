package com.example.test.arithMath;

import java.util.*;

/**
 * 问题：给定一个字符串数组，找出其中由相同字母组成的字符串（字母异位词），并分组输出。
 *
 * 示例输入：{"tse", "tes", "tae", "rre", "eat", "tea"}
 *
 * 解答过程：
 * 1. 核心思路 —— 字母异位词排序后得到的字符串一定相同，因此可以将排序后的字符串作为唯一标识（key）进行分组。
 * 2. 使用 HashMap<String, List<String>>：
 *      - key   = 原字符串按字母排序后的结果（如 "tse" → "est"）
 *      - value = 所有排序后等于该 key 的原始字符串列表
 * 3. 遍历数组，对每个字符串：
 *      a. 转为 char[] 并用 Arrays.sort() 排序；
 *      b. 将排序结果转回 String 作为 key；
 *      c. 用 computeIfAbsent 将原字符串加入对应列表中（不存在则先创建新列表）。
 * 4. 遍历 HashMap，输出 size > 1 的分组（至少有两个字符串由相同字母组成）。
 *
 * 运行结果：
 *   由字母为: aet 组合为 [tae, eat, tea]
 *   由字母为: est 组合为 [tse, tes]
 *
 * 时间复杂度：O(n * k log k)，其中 n 为数组长度，k 为字符串平均长度（排序开销）。
 * 空间复杂度：O(n * k)，HashMap 存储所有字符串。
 */
public class sortTest {

    public static void main(String[] args) {
        String[] str = new String[]{"tse", "tes", "tae", "rre", "eat", "tea"};

        // HashMap: key=排序后的字符串, value=由相同字母组成的字符串列表
        Map<String, List<String>> map = new HashMap<>();

        for (String s : str) {
            // 将字符串转为字符数组并排序
            char[] chars = s.toCharArray();
            Arrays.sort(chars);
            String sortedKey = new String(chars);

            // 将原字符串加入对应分组
            map.computeIfAbsent(sortedKey, k -> new ArrayList<>()).add(s);
        }

        // 输出结果
        System.out.println("由相同字母组成的字符串数组分组：");
        for (List<String> group : map.values()) {
            if (group.size() > 1) {
                System.out.println(" 由字母为: " + sortString(group.get(0)) + " 组合为" + group);
            }
        }
    }

    /**
     * 对字符串的字符进行排序
     */
    private static String sortString(String s) {
        char[] chars = s.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }
}