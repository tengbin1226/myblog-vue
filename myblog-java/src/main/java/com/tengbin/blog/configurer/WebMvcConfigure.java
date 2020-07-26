package com.tengbin.blog.configurer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.common.collect.Lists;
import com.tengbin.blog.common.exception.ServiceException;
import com.tengbin.blog.common.utils.Result;
import com.tengbin.blog.common.utils.ResultCode;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Spring MVC 配置
 *
 * @author TengBin
 */
@Configuration
@PropertySource("classpath:application.yml")
public class WebMvcConfigure implements WebMvcConfigurer {

    /**
     * 获取当前类日志并打印
     */
    private final Logger logger = LoggerFactory.getLogger(WebMvcConfigure.class);

    /**
     * 当前激活的配置文件
     */
    @Value("${spring.profiles.active:#{dev}}")
    private String active;

    /**
     * 配置自定义消息转换器(使用阿里FastJson作为 Json消息转换器)
     *
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 实例化convert转换消息的对象
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();

        // 添加fastjson配置消息. 如: 格式化返回json数据
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat, SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty);

        // 处理中文乱码问题(不然出现中文乱码)
        List<MediaType> mediaTypes = Lists.newArrayList();
        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);

        fastJsonHttpMessageConverter.setSupportedMediaTypes(mediaTypes);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);

        HttpMessageConverter<?> converter = fastJsonHttpMessageConverter;

        converters.add(converter);
    }

    /**
     * 静态资源处理
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    /**
     * 配置视图解析器
     *
     * @param registry
     */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {

    }

    /**
     * 页面跳转
     *
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 重定向到指定url
        RedirectViewControllerRegistration redirectViewControllerRegistration = registry.addRedirectViewController("/urlPath", "/redirectUrl");
    }

    /**
     * 解决跨域问题
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 允许指定的pathPattern可以进行跨域请求
        CorsRegistration corsRegistration = registry.addMapping("/pathPattern");

        // 设置允许哪些可以进行跨域访问，设置为"*"表示允许所有
        // 默认设置为允许所有
        corsRegistration.allowedOrigins("http://domain1.com", "http://domain2.com");

        // 设置允许的跨域请求动作，设置为"*"表示允许所有
        // 默认设置为允许简单动作，包括GET POST HEAD
        corsRegistration.allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS");

        // 设置允许的请求头，默认设置为允许所有，即"*"
        corsRegistration.allowedHeaders("Cache-Control", "Content-Language");

        // 设置response的头结构，不支持"*"
        corsRegistration.exposedHeaders("Cache-Control", "Content-Language");

        // 设置浏览器是否需要发送认证信息
        corsRegistration.allowCredentials(true);

        // 设置客户端保存pre-flight request缓存的时间
        // pre-flight request 预检请求
        corsRegistration.maxAge(1);
    }


    /**
     * 请求结果
     *
     * @param response
     * @param result
     */
    private void responseResult(HttpServletResponse response, Result result) {
        // 设置编码
        response.setCharacterEncoding("UTF-8");
        // 设置请求头
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        // 设置状态码
        response.setStatus(200);
        try {
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }


    /**
     * 获取客户端IP地址
     *
     * @param request
     * @return
     */
    private String getIpAddress(HttpServletRequest request) {
        // 获取请求中的ip地址
        String ip = request.getHeader("x-forwarded-for");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果是多级代理，那么取第一个ip为客户端ip
        if (ip != null && ip.indexOf(",") != -1) {
            ip = ip.substring(0, ip.indexOf(",")).trim();
        }
        return ip;
    }

}
