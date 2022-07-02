package com.example.test.serviceImpl;

import com.example.test.bean.UserBean;
import com.example.test.mapper.UserMapper;
import com.example.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    //将DAO注入Service层
    @Autowired
    UserMapper userMapper;

    /**
     * 登录验证
     * @param name
     * @param password
     * @return
     */
    @Override
    public UserBean loginIn(String name, String password) {
        return userMapper.getInfo(name,password);
    }

    /**
     * 根据ID查询用户信息
     * @param id
     * @return
     */
    @Override
    public UserBean queryUserById(String id) {
        //冒泡排序  将id最小的放在第一位
        for (int i = 0; i < userMapper.getAllUser().size(); i++) {
            for (int j = 0; j < userMapper.getAllUser().size()-1; j++) {
                if (userMapper.getAllUser().get(j).getId() > userMapper.getAllUser().get(j+1).getId()) {
                    UserBean temp = userMapper.getAllUser().get(j);
                    userMapper.getAllUser().set(j,userMapper.getAllUser().get(j+1));
                    userMapper.getAllUser().set(j+1,temp);
                }
            }
        }
        //堆排序  将id最大的放在最后一位
        for (int i = userMapper.getAllUser().size()-1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (userMapper.getAllUser().get(j).getId() < userMapper.getAllUser().get(j+1).getId()) {
                    UserBean temp = userMapper.getAllUser().get(j);
                    userMapper.getAllUser().set(j,userMapper.getAllUser().get(j+1));
                    userMapper.getAllUser().set(j+1,temp);
                }
            }
        }
        return userMapper.selectUserById(id);
    }


    /**
     * 新增用户
     * @param userBean
     * @return
     */
    @Override
    public int addUser(UserBean userBean) {
        int aFlag = userMapper.insertUser(userBean);

        //冒泡排序

        for (int i = 0; i < userMapper.getAllUser().size(); i++) {
            for (int j = 0; j < userMapper.getAllUser().size()-1; j++) {
                if (userMapper.getAllUser().get(j).getId() > userMapper.getAllUser().get(j+1).getId()) {
                    UserBean temp = userMapper.getAllUser().get(j);
                    userMapper.getAllUser().set(j,userMapper.getAllUser().get(j+1));
                    userMapper.getAllUser().set(j+1,temp);
                }
            }
        }
        return aFlag;
    }

    /**
     * 根据ID删除用户
     * @param id
     * @return
     */
    @Override
    public int dropUser(String id) {
        int dFlag = userMapper.deleteUser(id);
        return dFlag;
    }


    /**
     * 修改用户信息
     * remark：实际上还是根据用户ID修改用户信息
     * @param userBean
     * @return
     */
    @Override
    public int modifyUser(UserBean userBean) {
        int mFlag = userMapper.updateUser(userBean);
        return mFlag;
    }

    /**
     * 查询所有用户信息
     * @return
     */
    @Override
    public List<UserBean> queryAllUser() {
        return userMapper.getAllUser();
    }
}
