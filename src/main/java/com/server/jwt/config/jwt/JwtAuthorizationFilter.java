package com.server.jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.server.jwt.Mapper.UserMapper;
import com.server.jwt.config.auth.PrincipalDetail;
import com.server.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//시큐리티가 filter가지고 있는데 그중에 BasicAuthenticationFilter라는 것이 있음.
//권한이나 인증이 필요한 특정 주소를 요청했을 떄 필터를 무조건 타게 되어 있음.
//만약에 권한이나 인증이 필요한 주소가 아니라면 이 필터 안타요
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final UserMapper userMapper;
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserMapper userMapper) {
        super(authenticationManager);
        this.userMapper = userMapper;

    }

    //인증이나 권한이 필요한 주소요청이 있을때 해당 필터를 타게 될 것임.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        System.out.println("인증이나 권한이 필요한 주소 요청이 됨.");
        String jwtHeader = request.getHeader("Authorization");
        System.out.println("jwtHeader : " + jwtHeader);

        //header가 있는지 확인
        if (jwtHeader == null || !jwtHeader.startsWith("Bear")) {
            chain.doFilter(request, response);
            return;
        }
        String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");
        String username = JWT.require(Algorithm.HMAC512("cos"))
                .build()
                .verify(jwtToken)
                .getClaim("username")
                .asString();
        //서명이 제대로 됐으면
        if(username  !=null){
            User user = userMapper.findByUsername(username);
            System.out.println(user.getRoles());
            PrincipalDetail principalDetail = new PrincipalDetail(user);
            //토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어 준다.
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(principalDetail,principalDetail.getPassword(),principalDetail.getAuthorities());
            //강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request,response);
        }
    }
}
