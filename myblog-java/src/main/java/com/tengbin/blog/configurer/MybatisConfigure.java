package com.tengbin.blog.configurer;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Mybatis 配置
 *
 * @author TengBin
 */
@Configuration
@EnableTransactionManagement
@MapperScan(value = {"com.tengbin.blog.modules.user.mapper", "com.tengbin.blog.modules.blogs.mapper"})
public class MybatisConfigure {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor;
    }

}
