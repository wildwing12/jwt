package com.server.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config  = new CorsConfiguration();
        config.setAllowCredentials(true);//서버가 응답을 할떄 json을 자바스크립트에서 처리 할 수 있는 지 설정
        config.addAllowedOrigin("http://localhost:3000");//ahems ip응답을 허용하겠다.
        config.addAllowedHeader("*");//모든 header의 응답을 허용하겠다.
        config.addAllowedMethod("*");//모든 get,post등등 허용하겠따.
        config.setExposedHeaders(Arrays.asList("Access-Control-Allow-Headers", "Authorization, x-xsrf-token, Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, " +
                "Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers,RefreshToken"));
        source.registerCorsConfiguration("/api/**",config);
        source.registerCorsConfiguration("/*",config);
        return new CorsFilter(source);
    }
}
