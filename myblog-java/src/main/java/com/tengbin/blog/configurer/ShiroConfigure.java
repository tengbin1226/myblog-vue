package com.tengbin.blog.configurer;

import com.tengbin.blog.common.shiro.AccountRealm;
import com.tengbin.blog.common.shiro.JwtFilter;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName:ShiroConfigure
 * @Description: shiro配置
 * @Author Mr.T
 * @date 2020/6/22 0:56
 */
@Configuration
public class ShiroConfigure {

    @Autowired
    private JwtFilter jwtFilter;

    /**
     * session管理
     *
     * @param redisSessionDAO
     * @return
     */
    @Bean
    public SessionManager sessionManager(RedisSessionDAO redisSessionDAO) {
        // 创建默认webSession管理对象
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        // 设值
        defaultWebSessionManager.setSessionDAO(redisSessionDAO);
        // 返回
        return defaultWebSessionManager;
    }

    /**
     * 默认web权限管理对象
     *
     * @param accountRealm
     * @param sessionManager
     * @param redisCacheManager
     * @return
     */
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(AccountRealm accountRealm, SessionManager sessionManager, RedisCacheManager redisCacheManager) {
        // 1.通过AccountRealm创建DefaultWebSecurityManager
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager(accountRealm);

        // 2.设置属性
        defaultWebSecurityManager.setSessionManager(sessionManager);
        defaultWebSecurityManager.setCacheManager(redisCacheManager);

        // 3.返回
        return defaultWebSecurityManager;
    }

    /**
     * shiro过滤器链
     *
     * @return
     */
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        // 1.实例化默认shiro过滤器链
        DefaultShiroFilterChainDefinition defaultShiroFilterChainDefinition = new DefaultShiroFilterChainDefinition();

        // 2.初始化map
        Map<String, String> map = new LinkedHashMap<>();
        map.put("/**", "jwt");

        // 3.过滤器链对象添加path
        defaultShiroFilterChainDefinition.addPathDefinitions(map);

        // 返回
        return defaultShiroFilterChainDefinition;

    }

    /**
     * shiro过滤器工厂bean
     *
     * @param sessionManager
     * @param shiroFilterChainDefinition
     * @return
     */
    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager, ShiroFilterChainDefinition shiroFilterChainDefinition) {
        // 1.实例化shiro过滤器工厂bean
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 1.2.设置权限管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 2.定义存Filter的map
        Map<String, Filter> filters = new HashMap<>();
        filters.put("jwt", jwtFilter);

        // 1.3.设置过滤器
        shiroFilterFactoryBean.setFilters(filters);

        // 3.获取过滤器链集合
        Map<String, String> filterMap = shiroFilterChainDefinition.getFilterChainMap();

        // 1.4.设置过滤器链集合
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);

        return shiroFilterFactoryBean;
    }


}
