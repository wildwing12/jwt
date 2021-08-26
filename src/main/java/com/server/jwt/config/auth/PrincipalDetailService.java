package com.server.jwt.config.auth;

import com.server.jwt.Mapper.UserMapper;
import com.server.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
//http://localhost:8080/login
@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService {

    private final UserMapper mapper;
    @Override
    public UserDetails loadUserByUsername(String username)throws UsernameNotFoundException  {

            User user = mapper.findByUsername(username);
             if(user == null) {
                 throw new UsernameNotFoundException("username " + username + " not found");
            }
               return new PrincipalDetail(user);
    }
}
