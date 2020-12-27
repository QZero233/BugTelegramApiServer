package com.qzero.bt.common.config;

import com.qzero.bt.common.authorize.TokenCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlobalMvcConfig implements WebMvcConfigurer {

    @Autowired
    private TokenCheckInterceptor checkInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkInterceptor);
    }

}
