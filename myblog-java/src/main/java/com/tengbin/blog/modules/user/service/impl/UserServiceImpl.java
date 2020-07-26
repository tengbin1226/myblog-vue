package com.tengbin.blog.modules.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tengbin.blog.modules.user.entity.User;
import com.tengbin.blog.modules.user.mapper.UserMapper;
import com.tengbin.blog.modules.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {

}
