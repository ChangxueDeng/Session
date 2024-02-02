package org.example.sessionbackend.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Pattern;
import org.example.sessionbackend.entity.RestBean;
import org.example.sessionbackend.service.AuthorizeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {
    @Resource
    AuthorizeService service;

    final String EMAIL_REGEXP = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    //验证邮件
    @PostMapping("/valid-email")
    public RestBean<String> validateEmail(@Pattern(regexp = EMAIL_REGEXP) @RequestParam("email") String email, HttpSession session){
        if(service.sendValidateEmail(email, session.getId())){
            return RestBean.success("邮件已发送到指定邮箱，请查收");
        }else {
            return RestBean.failure(400,"邮件发送失败，请联系管理员");
        }
    }
}
