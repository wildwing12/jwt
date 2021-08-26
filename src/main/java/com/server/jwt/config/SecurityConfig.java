package com.server.jwt.config;

import com.server.jwt.Mapper.UserMapper;
import com.server.jwt.config.filter.MyFilter1;
import com.server.jwt.config.jwt.JWTmaker;
import com.server.jwt.config.jwt.JwtAuthenticationFilter;
import com.server.jwt.config.jwt.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CorsConfig corsConfig;
    private final UserMapper userMapper;
    private final JWTmaker jwTmaker;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //.addFilterBefore(new MyFilter1(), SecurityContextPersistenceFilter.class)
                //.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrf().disable()
                //.and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(corsConfig.corsFilter())//인증이 필요할때는 걸어야 한다.
                .formLogin().disable()//로그인 페이지 안쓴다.
                .httpBasic().disable()//
                .addFilter(new JwtAuthenticationFilter(authenticationManager(),jwTmaker))//AuthenticationManager을 파라미터로 줘야함.
                .addFilter(new JwtAuthorizationFilter(authenticationManager(),userMapper))
                .authorizeRequests()
                .antMatchers("/api/v1/user/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/manager/**")
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll();
    }
}
