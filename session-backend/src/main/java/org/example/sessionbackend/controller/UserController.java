package org.example.sessionbackend.controller;

import org.example.sessionbackend.entity.RestBean;
import org.example.sessionbackend.entity.user.AccountUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @GetMapping("/me")
    public RestBean<AccountUser> me(){
        return RestBean.success();
    }
}