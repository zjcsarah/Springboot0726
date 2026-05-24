package com.example.test.serviceImpl;

import com.example.test.bean.UserBean;
import com.example.test.mapper.UserMapper;
import com.example.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户业务逻辑实现
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    /** 登录验证 */
    @Override
    public UserBean loginIn(String name, String password) {
        return userMapper.getInfo(name, password);
    }

    /** 根据ID查询用户信息 */
    @Override
    public UserBean queryUserById(String id) {
        return userMapper.selectById(id);
    }

    /** 新增用户 */
    @Override
    public int addUser(UserBean userBean) {
        return userMapper.insert(userBean);
    }

    /** 根据ID删除用户 */
    @Override
    public int dropUser(String id) {
        return userMapper.deleteById(id);
    }

    /** 修改用户信息 */
    @Override


    public int modifyUser(UserBean userBean) {
        return userMapper.updateById(userBean);
    }

    /** 查询所有用户 */
    @Override
    public List<UserBean> queryAllUser() {
        return userMapper.selectList(null);
    }
}
