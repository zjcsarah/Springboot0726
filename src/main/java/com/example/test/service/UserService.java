package com.example.test.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.test.bean.UserBean;

import java.util.List;

/**
 * 用户业务逻辑接口
 */
public interface UserService {

    /** 登录验证 */
    UserBean loginIn(String name, String password);

    /** 根据ID查询用户信息 */
    UserBean queryUserById(String id);

    /** 新增用户 */
    int addUser(UserBean userBean);

    /** 根据ID删除用户 */
    int dropUser(String id);

    /** 修改用户信息 */
    int modifyUser(UserBean userBean);

    /** 查询所有用户 */
    List<UserBean> queryAllUser();

    /** 分页查询读者列表（仅角色为 user） */
    IPage<UserBean> queryReaders(int pageNum, int pageSize);
}
