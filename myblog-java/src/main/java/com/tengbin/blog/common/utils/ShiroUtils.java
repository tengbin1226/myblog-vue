package com.tengbin.blog.common.utils;

import com.tengbin.blog.common.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;

/**
 * @ClassName:ShiroUtils
 * @Description: shiro工具类
 * @Author Mr.T
 * @date 2020/6/22 1:17
 */
public class ShiroUtils {

    public static AccountProfile getProfile() {
        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
    }

}
