package com.yangzhuo.book_management.config;

import com.yangzhuo.book_management.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginWebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/UpdateReader","/toEditReader")
                .addPathPatterns("/admin/**")
                .addPathPatterns("/reader/**");
    }
}
