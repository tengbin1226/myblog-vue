package com.tengbin.blog.common.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @ClassName:JwtToken
 * @Description: jwt令牌
 * @Author Mr.T
 * @date 2020/6/22 1:06
 */
public class JwtToken implements AuthenticationToken {
    /**
     * 令牌
     */
    private String token;

    public JwtToken(String jwt) {
        this.token = jwt;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
