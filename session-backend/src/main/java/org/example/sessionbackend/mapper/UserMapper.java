package org.example.sessionbackend.mapper;

import org.apache.ibatis.annotations.*;
import org.example.sessionbackend.entity.auth.Account;
import org.example.sessionbackend.entity.user.AccountUser;

@Mapper
public interface UserMapper {
    @Select("select * from account where username = #{text} or email = #{text}")
    Account findAccountByNameOrEmail(String text);
    @Select("select * from account where username = #{text} or email = #{text}")
    AccountUser findAccountUserByNameOrEmail(String text);
    @Insert("INSERT INTO account(username, password, email) values(#{username}, #{password}, #{email})")
    int createAccount(@Param("username") String username, @Param("password") String password, @Param("email") String email);

    @Update("update account set password=#{password} where email = #{email}")
    int resetPasswordByEmail(@Param("password") String password, @Param("email") String email);
}
