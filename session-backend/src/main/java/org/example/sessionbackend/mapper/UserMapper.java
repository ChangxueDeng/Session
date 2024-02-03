package org.example.sessionbackend.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.sessionbackend.entity.Account;
import org.springframework.security.access.method.P;

@Mapper
public interface UserMapper {
    @Select("select * from account where username = #{text} or email = #{text}")
    Account findAccountByNameOrEmail(String text);

    @Insert("INSERT INTO account(username, password, email) values(#{username}, #{password}, #{email})")
    int createAccount(@Param("username") String username, @Param("password") String password, @Param("email") String email);
}
