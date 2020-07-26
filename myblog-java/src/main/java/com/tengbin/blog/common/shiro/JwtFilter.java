package com.tengbin.blog.common.shiro;

import cn.hutool.json.JSONUtil;
import com.tengbin.blog.common.utils.JwtUtils;
import com.tengbin.blog.common.utils.Result;
import com.tengbin.blog.common.utils.ResultGenerator;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName:JwtFilter
 * @Description: jwt过滤器
 * @Author Mr.T
 * @date 2020/6/22 1:03
 */
public class JwtFilter extends AuthenticatingFilter {

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 认证令牌
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        // 获取请求信息
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        // 获取请求头信息,用于获取jwt令牌信息
        String jwt = httpServletRequest.getHeader("Authorization");

        if (StringUtils.isEmpty(jwt)) {
            return null;
        }
        return new JwtToken(jwt);
    }

    /**
     * 表示访问拒绝时是否自己处理，如果返回true表示自己不处理且继续拦截器链执行，
     * 返回false表示自己已经处理了（比如重定向到另一个页面）。
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        // 获取请求信息
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        // 获取请求头信息,用于获取jwt令牌信息
        String jwt = httpServletRequest.getHeader("Authorization");

        if (StringUtils.isNotEmpty(jwt)) {
            // 校验jwt
            Claims claims = jwtUtils.getClaimByToken(jwt);
            if (ObjectUtils.isEmpty(claims) || jwtUtils.isTokenExpired(claims.getExpiration())) {
                throw new ExpiredCredentialsException("token已失效，请重新登录");
            }
            return executeLogin(servletRequest, servletResponse);
        }
        return true;
    }

    /**
     * 登录失败
     *
     * @param token
     * @param e
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        // 获取响应信息
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // 把被包装的原始异常提取出来，再用它们自己的catch子句进行处理
        Throwable throwable = e.getCause() == null ? e : e.getCause();
        // 返回失败结果
        Result result = ResultGenerator.genFailResult(throwable.getMessage());
        // 将结果对象转为json对象
        String jsonStr = JSONUtil.toJsonStr(result);

        try {
            httpServletResponse.getWriter().println(jsonStr);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }


    /**
     * 会进行url模式与请求url进行匹配，如果匹配会调用onPreHandle；
     * 如果没有配置url模式/没有url模式匹配，默认直接返回true
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        // 获取请求信息
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        // 获取响应信息
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        // 设置头部信息
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }

        return super.preHandle(servletRequest, servletResponse);
    }
}
