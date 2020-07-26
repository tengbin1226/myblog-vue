package com.tengbin.blog.modules.user.controller;

import com.tengbin.blog.common.utils.Result;
import com.tengbin.blog.common.utils.ResultGenerator;
import com.tengbin.blog.modules.user.entity.User;
import com.tengbin.blog.modules.user.service.UserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户控制器
 *
 * @author Tengbin
 * @date 2020/06/22
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;


    @RequiresAuthentication
    @GetMapping("/index")
    public Result index() {
        User user = userService.getById(1L);
        return ResultGenerator.genSuccessResult(user);
    }


    /**
     * 添加用户信息
     *
     * @param user
     * @return
     */
    @PostMapping("/save")
    public Result save(@Validated @RequestBody User user) {
        //boolean save = userService.save(user);

        return ResultGenerator.genSuccessResult(user);

        // return ResultGenerator.genFailResult("用户添加失败!");
    }

}
