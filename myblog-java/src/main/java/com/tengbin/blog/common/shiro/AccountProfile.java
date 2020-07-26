package com.tengbin.blog.common.shiro;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName:AccountProfile
 * @Description:
 * @Author Mr.T
 * @date 2020/6/22 1:18
 */
@Data
public class AccountProfile implements Serializable {

    private static final long serialVersionUID = -6274448905218105585L;

    private Long id;

    private String username;

    private String avatar;

    private String email;
}
