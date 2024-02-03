package org.example.sessionbackend.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Pattern;
import org.example.sessionbackend.entity.RestBean;
import org.example.sessionbackend.service.AuthorizeService;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {
    @Resource
    AuthorizeService service;

    final String EMAIL_REGEXP = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    final String USERNAME_REGEXP = "[a-zA-Z0-9\\u4e00-\\u9fa5]+$";

    //验证邮件
    @PostMapping("/valid-email")
    public RestBean<String> validateEmail(@Pattern(regexp = EMAIL_REGEXP) @RequestParam("email") String email, HttpSession session){
        String s = service.sendValidateEmail(email, session.getId());
        if(s == null){
            return RestBean.success("邮件已发送到指定邮箱，请查收");
        }else {
            return RestBean.failure(400,s);
        }
    }
    //注册
    @PostMapping("/register")
    public RestBean<String> registerUser(@Pattern(regexp = USERNAME_REGEXP) @Length(min = 3,max = 5) @RequestParam("username") String username,
                                         @Length(min = 6,max = 16)@RequestParam("password") String password,
                                         @RequestParam("email") String email,
                                         @Length(min = 6, max = 6)@RequestParam("email_code") String code,
                                         HttpSession session){
        String s = service.validateAndRegister(username,password,email,code,session.getId());
        if(s == null){
            return RestBean.success("注册成功");
        }else {
            return RestBean.failure(400, s);
        }
    }
}
