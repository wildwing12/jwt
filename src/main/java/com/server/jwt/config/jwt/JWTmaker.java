package com.server.jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.server.jwt.config.auth.PrincipalDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class JWTmaker {


    public String JWTToken(Authentication authResult){
        PrincipalDetail principalDetail = (PrincipalDetail) authResult.getPrincipal();
        return JWT.create()
                .withSubject("cos토큰")
                .withExpiresAt(new Date(System.currentTimeMillis() +(60000)*10))
                .withClaim("id",principalDetail.getUser().getId())//아무값넣어도됨
                .withClaim("username",principalDetail.getUser().getUsername())//아무값 넣어도됨.
                .sign(Algorithm.HMAC512("cos"));//cos는 비밀값을 넣어;
    }

    public String RefreshToken(Authentication authResult){
        PrincipalDetail principalDetail = (PrincipalDetail) authResult.getPrincipal();
        return JWT.create()
                .withSubject("cos토큰")
                .withExpiresAt(new Date(System.currentTimeMillis()+60*60*24*1000))
                .withClaim("id",principalDetail.getUser().getId())
                .withClaim("username",principalDetail.getUser().getUsername())//아무값 넣어도됨.
                .sign(Algorithm.HMAC512("cos"));
    }
    public  boolean isValidToken(String token, String refreashToken, HttpServletRequest request, HttpServletResponse response){
        try{
            if(compareToken(token,refreashToken)){
                response.addHeader("Authorization","Bearer "+token);
                response.addHeader("RefreshToken",refreashToken);
                return true;
            }
                return false;
        } catch (Exception e){
            System.out.println("토큰 생성 실패");
            return false;
        }
    }

     boolean compareToken(String token, String refreashToken) {
        try{
            Map<String,Object> DecodeJWT = decodeToken(token);
            Map<String,Object>  DecodeReJWT = decodeToken(refreashToken);
            Date expiredDate = (Date) DecodeJWT.get("time");
            if(expiredDate.after(expiredDate)){
                return DecodeJWT.get("id") == DecodeReJWT.get("id")
                        && DecodeJWT.get("username") == DecodeReJWT.get("username")
                        && DecodeJWT.get("secret") == DecodeReJWT.get("secret")
                        && DecodeJWT.get("secret").toString().equals("cos")
                        && DecodeReJWT.get("secret").toString().equals("cos");
            }
            return false;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    private Map<String,Object> decodeToken(String token) {
        Map<String,Object> map = new HashMap<>();
        String id = JWT.decode(token).getClaim("username").toString();
        String username = JWT.decode(token).getClaim("username").toString();
        String secret =JWT.decode(token).getSignature();
        Date time = JWT.decode(token).getExpiresAt();
        map.put("id",id);
        map.put("username",username);
        map.put("secret",secret);
        map.put("time",time);
        return map;
    }
}
