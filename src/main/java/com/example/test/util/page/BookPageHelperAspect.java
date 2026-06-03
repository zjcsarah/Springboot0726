package com.example.test.util.page;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * {@link BookPageHelper} 注解的 AOP 切面实现
 * <p>
 * 拦截标注了 @BookPageHelper 的方法，自动提取分页参数并调用 PageHelper.startPage()。
 *
 * @author 郑建川
 */
@Aspect
@Component
public class BookPageHelperAspect {

    /** 默认页码 */
    private static final int DEFAULT_PAGE_NUM = 1;
    /** 默认每页条数 */
    private static final int DEFAULT_PAGE_SIZE = 1000;
    /** 最大每页条数 */
    private static final int MAX_PAGE_SIZE = 1000;

    private final ParameterNameDiscoverer paramNameDiscoverer = new DefaultParameterNameDiscoverer();

    /**
     * 环绕通知：提取分页参数 → PageHelper.startPage() → 执行方法 → PageHelper.clearPage()
     */
    @Around("@annotation(bookPageHelper)")
    public Object around(ProceedingJoinPoint joinPoint, BookPageHelper bookPageHelper) throws Throwable {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String[] paramNames = paramNameDiscoverer.getParameterNames(method);

        int pageNum = DEFAULT_PAGE_NUM;
        int pageSize = DEFAULT_PAGE_SIZE;
        boolean foundPageNum = false;
        boolean foundPageSize = false;

        // ===== 优先级 1：从方法参数中获取 pageNum / pageSize =====
        if (paramNames != null) {
            for (int i = 0; i < paramNames.length && i < args.length; i++) {
                if ("pageNum".equals(paramNames[i]) && args[i] != null) {
                    pageNum = toInt(args[i], DEFAULT_PAGE_NUM);
                    foundPageNum = true;
                }
                if ("pageSize".equals(paramNames[i]) && args[i] != null) {
                    pageSize = toInt(args[i], DEFAULT_PAGE_SIZE);
                    foundPageSize = true;
                }
            }
        }

        // ===== 优先级 2：从 VO 对象中反射获取 =====
        if (!foundPageNum || !foundPageSize) {
            for (Object arg : args) {
                if (arg == null || isSimpleType(arg.getClass())) {
                    continue;
                }
                if (!foundPageNum) {
                    Integer pn = getFieldValueAsInt(arg, "pageNum");
                    if (pn != null) {
                        pageNum = pn;
                        foundPageNum = true;
                    }
                }
                if (!foundPageSize) {
                    Integer ps = getFieldValueAsInt(arg, "pageSize");
                    if (ps != null) {
                        pageSize = ps;
                        foundPageSize = true;
                    }
                }
                if (foundPageNum && foundPageSize) {
                    break;
                }
            }
        }

        // ===== 优先级 3：注解参数覆盖 pageSize =====
        if (bookPageHelper.pageSize() > 0) {
            pageSize = bookPageHelper.pageSize();
        }

        // ===== 参数校验 =====
        if (pageNum < 1) {
            pageNum = DEFAULT_PAGE_NUM;
        }
        if (pageSize > MAX_PAGE_SIZE || pageSize < 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        // ===== 调用 PageHelper 开启分页 =====
        PageHelper.startPage(pageNum, pageSize);

        try {
            return joinPoint.proceed();
        } finally {
            PageHelper.clearPage();
        }
    }

    /**
     * 将对象转为 int，失败返回默认值
     */
    private int toInt(Object obj, int defaultValue) {
        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }
        if (obj instanceof String) {
            try {
                return Integer.parseInt((String) obj);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * 反射获取对象中的 int 字段值（优先 getter，其次直接访问字段）
     */
    private Integer getFieldValueAsInt(Object obj, String fieldName) {
        Class<?> clazz = obj.getClass();
        try {
            Method getter = clazz.getMethod(toGetterName(fieldName));
            Object value = getter.invoke(obj);
            if (value != null) {
                return toInt(value, -1);
            }
        } catch (Exception ignored) {
            // getter 不存在
        }
        try {
            Field field = findField(clazz, fieldName);
            if (field != null) {
                field.setAccessible(true);
                Object value = field.get(obj);
                if (value != null) {
                    return toInt(value, -1);
                }
            }
        } catch (Exception ignored) {
            // 反射失败
        }
        return null;
    }

    private Field findField(Class<?> clazz, String fieldName) {
        Class<?> current = clazz;
        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        return null;
    }

    private String toGetterName(String fieldName) {
        return "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
    }

    private boolean isSimpleType(Class<?> clazz) {
        return clazz.isPrimitive()
                || clazz.getName().startsWith("java.lang.")
                || clazz.getName().startsWith("java.math.")
                || clazz.getName().startsWith("java.time.")
                || clazz.getName().startsWith("java.util.")
                || clazz.isArray();
    }
}
