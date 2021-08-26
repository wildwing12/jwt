package com.server.jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.jwt.config.auth.PrincipalDetail;
import com.server.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있음.
// /login 요청에서  username이나 passowrd를 전송하면
//UsernamePasswordAuthenticationFilter가 동작함
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JWTmaker jwTmaker;



    // /login 요청을 하면 로그인 시도를 위해서 실행되는 함수

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //username, password받아서
        try {
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);
            System.out.println(user);
            System.out.println(user.getUsername());
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            PrincipalDetail principalDetail = (PrincipalDetail) authentication.getPrincipal();
            System.out.println("로그인됨"+principalDetail.getUser().getUsername());
            System.out.println("===================================");
            
            //autehentication을 리턴해주면 session영역에 저장을 하고 그방법이 return 하는 것이다.
            //굳이  JWT토큰을 사용하면서 세션을 만들어줄 이유가 없음. 근데 단지 권한 처리 때문에 session을 넣는다.
            
            //jwt 토큰을 만들어야 한다.
            //근데 굳이 여기서 만들 필요가 없음. 
            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }
        //정상인지 로그인 시도
        //로그인 서비스가 실행되나 확인해보면 되겠지?

        return null;
    }
    //attempAuthentication이 실행후 인증이 정상적이 되면  successfulAuthentication 함수가 실행됨
    //JWT 토큰을 만들어서 request요청한 사용자에게JWT토큰을 response해주면됨.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication이 실행됨, 즉 인증 완료");
        PrincipalDetail principalDetail = (PrincipalDetail) authResult.getPrincipal();
        //hash 암호 방식
        //HMAC512는 비밀키가 있어야한다.
        String jwtToken = jwTmaker.JWTToken(authResult);
        response.addHeader("Authorization","Bearer "+jwtToken);

        String RefreshToken = jwTmaker.RefreshToken(authResult);
        response.addHeader("RefreshToken",RefreshToken);
    }




}