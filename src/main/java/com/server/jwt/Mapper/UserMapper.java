package com.server.jwt.Mapper;

import com.server.model.User;
import org.apache.ibatis.annotations.Mapper;

import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {
   public User findByUsername(String username);

   public void joinUser(User user);
}
