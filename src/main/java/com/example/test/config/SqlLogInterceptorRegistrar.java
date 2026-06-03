package com.example.test.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 显式将 SqlLogInterceptor 注册到 MyBatis Configuration
 * 不依赖 MyBatis-Plus 的自动收集机制
 */
@Component
public class SqlLogInterceptorRegistrar implements CommandLineRunner {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public void run(String... args) {
        System.err.println("[Registrar] ===== 开始注册 SqlLogInterceptor =====");
        org.apache.ibatis.session.Configuration config = sqlSessionFactory.getConfiguration();
        SqlLogInterceptor interceptor = new SqlLogInterceptor(config);
        config.addInterceptor(interceptor);
        System.err.println("[Registrar] ===== SqlLogInterceptor 已注册到 MyBatis Configuration =====");
    }
}
