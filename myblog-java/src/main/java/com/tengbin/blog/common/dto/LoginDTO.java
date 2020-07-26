package com.tengbin.blog.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @ClassName:LoginDTO
 * @Description: 登录数据传输对象
 * @Author Mr.T
 * @date 2020/6/22 0:38
 */
@Data
public class LoginDTO implements Serializable {

    private static final long serialVersionUID = 3423014184851500508L;

    @NotBlank(message = "登录名称不能为空")
    private String userName;

    @NotBlank(message = "密码不能为空")
    private String password;
}
