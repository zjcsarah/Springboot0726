package com.example.test.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.test.bean.BaseResult;
import com.example.test.bean.UserBean;
import com.example.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 读者管理 REST API（含登录注册）
 */
@RestController
@RequestMapping("/api/readers")
public class ReaderController {

    @Autowired
    UserService userService;

    @GetMapping
    public BaseResult<IPage<UserBean>> listReaders(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword) {
        return BaseResult.ok(userService.queryReaders(pageNum, pageSize, keyword));
    }

    @GetMapping("/{id}")
    public BaseResult<UserBean> getReader(@PathVariable String id) {
        return BaseResult.ok(userService.queryUserById(id));
    }

    @PostMapping("/login")
    public BaseResult<UserBean> login(@RequestBody Map<String, Object> body) {
        String name = body.get("name").toString();
        String password = body.get("password").toString();
        UserBean userBean = userService.loginIn(name, password);
        if (userBean != null) {
            return BaseResult.ok(userBean, "登录成功");
        }
        return BaseResult.fail("用户名或密码错误");
    }

    @PutMapping("/{id}")
    public BaseResult<?> updateReader(@PathVariable String id, @RequestBody UserBean userBean) {
        userBean.setId(Integer.parseInt(id));
        int flag = userService.modifyUser(userBean);
        return flag == 1 ? BaseResult.ok(null, "修改成功") : BaseResult.fail("修改失败");
    }

    @DeleteMapping("/{id}")
    public BaseResult<?> deleteReader(@PathVariable String id) {
        int flag = userService.dropUser(id);
        return flag == 1 ? BaseResult.ok(null, "删除成功") : BaseResult.fail("删除失败");
    }

    @PostMapping("/register")
    public BaseResult<?> register(@RequestBody UserBean userBean) {
        userBean.setRole("user");
        int flag = userService.addUser(userBean);
        return flag == 1 ? BaseResult.ok(null, "注册成功") : BaseResult.fail("注册失败");
    }
}