package com.tengbin.blog.modules.blogs.controller;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tengbin.blog.common.utils.DateUtils;
import com.tengbin.blog.common.utils.Result;
import com.tengbin.blog.common.utils.ResultGenerator;
import com.tengbin.blog.common.utils.ShiroUtils;
import com.tengbin.blog.modules.blogs.entity.Blog;
import com.tengbin.blog.modules.blogs.service.BlogService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;


/**
 * 博客后端控制器
 *
 * @author Sunshine
 */
@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    /**
     * 查询所有博客信息
     *
     * @param currentPage
     * @return
     */
    @GetMapping("/blogs")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage) {
        List<Blog> blogs = blogService.list();
        return ResultGenerator.genSuccessResult(blogs);
    }

    /**
     * 查询博客详情
     *
     * @param id
     * @return
     */
    @GetMapping("/blog/{id}")
    public Result findById(@PathVariable(name = "id") Long id) {
        // 根据编号查询
        Blog blog = blogService.getById(id);
        Assert.notNull(blog, "该博客已被删除!");

        return ResultGenerator.genSuccessResult(blog);
    }

    /**
     * 添加或修改博客
     *
     * @return
     */
    @RequiresAuthentication
    @PostMapping("/blog/edit")
    public Result edit(@Validated @RequestBody Blog blog) {
        // 初始化对象
        Blog tempBlog = null;

        if (!ObjectUtils.isEmpty(blog.getUserId())) {
            // 根据编号查询
            tempBlog = blogService.getById(blog.getId());

            System.out.println(ShiroUtils.getProfile().getId());
            // 只能编辑自己的文章
            Assert.isTrue(tempBlog.getId().longValue() == ShiroUtils.getProfile().getId().longValue(), "没有权限编辑");
        } else {
            // 实例化对象
            tempBlog = new Blog();
            // 设置属性
            tempBlog.setUserId(ShiroUtils.getProfile().getId());
            tempBlog.setCreated(DateUtils.asDate(LocalDateTime.now()));
            tempBlog.setStatus(0);
        }

        // 对象属性拷贝
        BeanUtil.copyProperties(blog, tempBlog, "id", "userId", "created", "status");
        // 调用添加或修改
        blogService.saveOrUpdate(tempBlog);

        return ResultGenerator.genSuccessResult(null);
    }


}
