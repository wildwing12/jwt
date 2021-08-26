package com.server.jwt.controller;

import com.server.jwt.Mapper.UserMapper;
import com.server.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RestAPiController {
    private final UserMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/home")
    public String home(){
        return "<h1>home</h1>";
    }

    @PostMapping("/home")
    public String homePost(){
        User user =mapper.findByUsername("kim");
        System.out.println(user);
        return "<h1>home</h1>";
    }
    @Transactional
    @PostMapping("/join")
    public String join(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        mapper.joinUser(user);
        return"가입 완료";
    }

    @GetMapping("/api/v1/user")
    public String user(){
        return "user";
    }

    @GetMapping("/api/v1/manager")
    public String manager(){
        return "manager";
    }

    @GetMapping("/api/v1/admin")
    public String admin(){
        return "admin";
    }
}
