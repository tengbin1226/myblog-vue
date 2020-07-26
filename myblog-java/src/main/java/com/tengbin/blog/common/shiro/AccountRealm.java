package com.tengbin.blog.common.shiro;

import com.tengbin.blog.common.utils.JwtUtils;
import com.tengbin.blog.modules.user.entity.User;
import com.tengbin.blog.modules.user.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * @ClassName:AccountRealm
 * @Description:
 * @Author Mr.T
 * @date 2020/6/22 1:20
 */
@Component
public class AccountRealm extends AuthorizingRealm {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;


    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 获取jwt令牌
        JwtToken jwtToken = (JwtToken) token;

        // 获取用户编号
        String userId = jwtUtils.getClaimByToken((String) jwtToken.getPrincipal()).getSubject();

        // 根据获取到的用户编号查询用户
        User user = userService.getById(Long.valueOf(userId));

        if (ObjectUtils.isEmpty(user)) {
            throw new UnknownAccountException("账户不存在");
        }
        if (user.getStatus() == -1) {
            throw new LockedAccountException("账户已被锁定");
        }

        // 初始化对象
        AccountProfile accountProfile = new AccountProfile();
        // 拷贝属性
        BeanUtils.copyProperties(user, accountProfile);

        // 返回简单认证信息对象
        return new SimpleAuthenticationInfo(accountProfile, jwtToken.getCredentials(), getName());
    }
}
