package com.example.test.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.test.bean.UserBean;
import com.example.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 读者管理 REST API（含登录注册）
 */
@RestController
@RequestMapping("/api/readers")
public class ReaderController {

    @Autowired
    UserService userService;

    /** 分页获取读者列表 */
    @GetMapping
    public Map<String, Object> listReaders(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword) {
        Map<String, Object> map = new HashMap<>();
        IPage<UserBean> page = userService.queryReaders(pageNum, pageSize, keyword);
        map.put("code", 0);
        map.put("data", page);
        return map;
    }

    /** 根据ID查询读者详情 */
    @GetMapping("/{id}")
    public Map<String, Object> getReader(@PathVariable String id) {
        Map<String, Object> map = new HashMap<>();
        UserBean user = userService.queryUserById(id);
        map.put("code", 0);
        map.put("data", user);
        return map;
    }

    /** 读者登录，校验用户名密码，返回用户信息和角色 */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, Object> body) {
        Map<String, Object> map = new HashMap<>();
        String name = body.get("name").toString();
        String password = body.get("password").toString();
        UserBean userBean = userService.loginIn(name, password);
        if (userBean != null) {
            map.put("code", 0);
            map.put("data", userBean);
            map.put("msg", "登录成功");
        } else {
            map.put("code", -1);
            map.put("msg", "用户名或密码错误");
        }
        return map;
    }

    /** 修改读者信息 */
    @PutMapping("/{id}")
    public Map<String, Object> updateReader(@PathVariable String id, @RequestBody UserBean userBean) {
        Map<String, Object> map = new HashMap<>();
        userBean.setId(Integer.parseInt(id));
        int flag = userService.modifyUser(userBean);
        map.put("code", flag == 1 ? 0 : -1);
        map.put("msg", flag == 1 ? "修改成功" : "修改失败");
        return map;
    }

    /** 删除读者 */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteReader(@PathVariable String id) {
        Map<String, Object> map = new HashMap<>();
        int flag = userService.dropUser(id);
        map.put("code", flag == 1 ? 0 : -1);
        map.put("msg", flag == 1 ? "删除成功" : "删除失败");
        return map;
    }

    /** 读者注册，默认角色为 user */
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody UserBean userBean) {
        Map<String, Object> map = new HashMap<>();
        userBean.setRole("user");
        int flag = userService.addUser(userBean);
        map.put("code", flag == 1 ? 0 : -1);
        map.put("msg", flag == 1 ? "注册成功" : "注册失败");
        return map;
    }
}
