package com.server.jwt.config.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
@Configuration
public class MyFilter1 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest req = (HttpServletRequest) request;
//        HttpServletResponse res = (HttpServletResponse) response;
//
//        System.out.println(req.getMethod());
//        if(req.getMethod().equals("POST")){
//            System.out.println("POST요청됨");
//            String headerAuth = req.getHeader("Authorization");
//            System.out.println(headerAuth);
//
//            if(headerAuth.equals("cos")) {
//                chain.doFilter(req, res);
//            }else{
//                PrintWriter out = res.getWriter();
//                out.println("인증 노노함");
//            }
//        }
//        System.out.println("필터임");
        chain.doFilter(request, response);
    }
}
