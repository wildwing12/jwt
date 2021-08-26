package com.server.jwt.config.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class Interceptor implements WebMvcConfigurer {
    private final JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("인터셉터 등장");
        registry.addInterceptor(jwtInterceptor).addPathPatterns("/api/**").excludePathPatterns("/login");
    }
}
