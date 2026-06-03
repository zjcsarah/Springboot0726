package com.example.test.util.page;

import java.lang.annotation.*;

/**
 * 分页插件注解
 * <p>
 * 用于自动从方法参数或VO对象中提取 pageNum/pageSize，
 * 并存入 PageContext 供后续 MyBatis-Plus 分页使用。
 * <pre>
 * 取值优先级：
 *   1. 方法参数中名为 pageNum / pageSize 的参数
 *   2. VO 对象中的 pageNum / pageSize 字段
 *   3. 默认值 pageNum=1, pageSize=1000
 *   4. 注解的 pageSize 属性可覆盖以上逻辑
 *
 * pageSize 校验：
 *   pageSize &gt; 1000 或 pageSize &lt; 0 时，自动重置为 1000
 * </pre>
 *
 * @author 郑建川
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BookPageHelper {

    /**
     * 指定分页大小，默认 -1 表示不覆盖，由方法参数/VO/默认值决定
     */
    int pageSize() default -1;
}
