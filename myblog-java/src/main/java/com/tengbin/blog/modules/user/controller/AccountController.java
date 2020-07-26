package com.tengbin.blog.modules.user.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tengbin.blog.common.dto.LoginDTO;
import com.tengbin.blog.common.utils.JwtUtils;
import com.tengbin.blog.common.utils.Result;
import com.tengbin.blog.common.utils.ResultGenerator;
import com.tengbin.blog.modules.user.entity.User;
import com.tengbin.blog.modules.user.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.TooManyListenersException;

/**
 * @ClassName:AccountController
 * @Description: 账户后端控制器
 * @Author Mr.T
 * @date 2020/6/22 11:22
 */
@RestController
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 登录
     *
     * @param loginDTO
     * @param httpServletResponse
     * @return
     * @throws TooManyListenersException
     */
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDTO loginDTO, HttpServletResponse httpServletResponse) throws TooManyListenersException {
        // 根据用户名查询用户信息
        User user = userService.getOne(new QueryWrapper<User>().eq("username", loginDTO.getUserName()));
        Assert.notNull(user, "用户名不存在!");

        if (!user.getPassword().equals(SecureUtil.md5(loginDTO.getPassword()))) {
            return ResultGenerator.genFailResult("密码错误,请重新输入!");
        }

        // 根据用户id生成jwt
        String jwt = jwtUtils.generateToken(user.getId());
        httpServletResponse.setHeader("Authorization", jwt);
        httpServletResponse.setHeader("Access-control-Expose-Headers", "Authorization");

        // 将用户信息存入map中,返回给前端
        Map<Object, Object> map = MapUtil.builder().put("id", user.getId()).put("username", user.getUsername()).put("avatar", user.getAvatar()).put("email", user.getEmail()).map();

        return ResultGenerator.genSuccessResult(map);
    }

    /**
     * 退出
     *
     * @return
     */
    @RequiresAuthentication
    @GetMapping("/logout")
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return ResultGenerator.genSuccessResult();
    }


}
