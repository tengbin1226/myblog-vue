package com.tengbin.blog.modules.blogs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tengbin.blog.modules.blogs.entity.Blog;
import com.tengbin.blog.modules.blogs.mapper.BlogMapper;
import com.tengbin.blog.modules.blogs.service.BlogService;
import org.springframework.stereotype.Service;

/**
 * 博客业务逻辑层实现类
 * @author Sunshine
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {


}
