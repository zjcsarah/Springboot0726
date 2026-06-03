package com.example.test.config;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MyBatis SQL 日志拦截器
 * 拦截 StatementHandler.prepare 获取最终 SQL（PageHelper 分页后），替换 ? 为实际参数值
 *
 * @author 郑建川
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class SqlLogInterceptor implements Interceptor {

    private static final Logger log = LoggerFactory.getLogger(SqlLogInterceptor.class);
    private static final Pattern PARAM_PATTERN = Pattern.compile("\\?");

    private final Configuration configuration;

    public SqlLogInterceptor(Configuration configuration) {
        this.configuration = configuration;
        System.err.println("[SqlLogInterceptor] ===== 拦截器已实例化 =====");
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 使用公开 API 获取 BoundSql（避免反射被代理对象拦截）
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();

        String formattedSql = formatSql(configuration, boundSql);
        long start = System.currentTimeMillis();
        Object result = invocation.proceed();
        long elapsed = System.currentTimeMillis() - start;

        System.out.println("[SQL " + elapsed + "ms] " + formattedSql);
        log.info("SQL [{}ms] >>> {}", elapsed, formattedSql);
        return result;
    }

    /**
     * 将 BoundSql 中的 ? 替换为实际参数值
     */
    private String formatSql(Configuration configuration, BoundSql boundSql) {
        String sql = boundSql.getSql().replaceAll("\\s+", " ").trim();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        Object parameterObject = boundSql.getParameterObject();

        if (parameterMappings.isEmpty() || parameterObject == null) {
            return sql;
        }

        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();

        // 简单类型参数：直接替换
        if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
            return PARAM_PATTERN.matcher(sql).replaceFirst(
                    Matcher.quoteReplacement(formatValue(parameterObject)));
        }

        // 复杂对象参数：通过 MetaObject 逐字段提取
        MetaObject metaObject = configuration.newMetaObject(parameterObject);
        for (ParameterMapping mapping : parameterMappings) {
            String property = mapping.getProperty();
            Object value;
            if (boundSql.hasAdditionalParameter(property)) {
                value = boundSql.getAdditionalParameter(property);
            } else if (metaObject.hasGetter(property)) {
                value = metaObject.getValue(property);
            } else {
                continue;
            }
            sql = PARAM_PATTERN.matcher(sql).replaceFirst(
                    Matcher.quoteReplacement(formatValue(value)));
        }

        return sql;
    }

    /**
     * 格式化参数值，字符串加引号，日期加引号，其余直接输出
     */
    private String formatValue(Object value) {
        if (value == null) {
            return "NULL";
        }
        if (value instanceof String) {
            return "'" + value.toString().replace("\\", "\\\\").replace("'", "''") + "'";
        }
        if (value instanceof Date) {
            return "'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) value) + "'";
        }
        return value.toString();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
