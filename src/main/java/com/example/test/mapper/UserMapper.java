package com.example.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.test.bean.UserBean;
import org.apache.ibatis.annotations.Param;

/**
 * 用户数据访问层
 */
public interface UserMapper extends BaseMapper<UserBean> {

    /** 登录验证（根据用户名和密码查询用户） */
    UserBean getInfo(@Param("name") String name, @Param("password") String password);
}
