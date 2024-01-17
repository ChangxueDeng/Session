package org.example.sessionbackend.config;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.sessionbackend.entity.RestBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                //管理安全权限
                .authorizeHttpRequests(conf->{
                    conf.anyRequest().authenticated();
                })
                //登录配置
                .formLogin(conf ->{
                    conf.loginProcessingUrl("/api/auth/login");
                    conf.successHandler(this::onAuthenticationSuccess);
                    conf.failureHandler(this::onAuthenticationFailure);
                })
                //退出配置
                .logout(conf ->{
                    conf.logoutUrl("/api/auth/logout");
                })
                //关闭csrf校验
                .csrf(conf ->{
                    conf.disable();
                })
                .exceptionHandling(conf ->{
                    conf.authenticationEntryPoint(this::onAuthenticationFailure);
                })
                .build();
    }



    void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(RestBean.success("登录成功").toJson());
    }
    void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException{
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(RestBean.failure(401).toJson());
    }
}
