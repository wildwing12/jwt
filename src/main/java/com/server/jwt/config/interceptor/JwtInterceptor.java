package com.server.jwt.config.interceptor;

import com.server.jwt.config.jwt.JWTmaker;
import com.server.jwt.config.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {
    private JWTmaker jwTmaker;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("JWTTOken 호출");
        String Authorization = request.getHeader("Authorization");
        String RefreshToken = request.getHeader("RefreshToken");

        if (Authorization != null){
            return jwTmaker.isValidToken(Authorization,RefreshToken,request,response);
        }

            return false;
    }
}
