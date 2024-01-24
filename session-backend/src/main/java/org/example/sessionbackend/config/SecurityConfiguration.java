package org.example.sessionbackend.config;


import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.sessionbackend.entity.RestBean;
import org.example.sessionbackend.service.AuthorizeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Resource
    DataSource dataSource;
    @Resource
    AuthorizeService authorizeService;
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
                    conf.logoutSuccessHandler(this::onAuthenticationSuccess);
                })
                //关闭csrf校验
                .csrf(conf ->{
                    conf.disable();
                })
                .exceptionHandling(conf ->{
                    conf.authenticationEntryPoint(this::onAuthenticationFailure);
                })
                .userDetailsService(authorizeService)
                //跨域
                .cors(conf->{
                    CorsConfiguration configuration = new CorsConfiguration();
                    //添加前端站点
                    configuration.addAllowedOrigin("http://localhost:8080");
                    //运行携带Cookie
                    configuration.setAllowCredentials(true);
                    configuration.addExposedHeader("*");
                    configuration.addAllowedHeader("*");
                    configuration.addAllowedMethod("*");
                    //封装
                    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                    //注册
                    source.registerCorsConfiguration("/**",configuration);
                    conf.configurationSource(source);
                })
                //记住我
                .rememberMe(conf ->{
                    conf.rememberMeParameter("remember");
                    conf.tokenRepository(persistentTokenRepository());
                    conf.tokenValiditySeconds(3600 * 24); // 一天时间

                })
                .build();
    }
    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        //jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
//                .userDetailsService(authorizeService)
//                .and()
//                .build();
//    }
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{
        response.setCharacterEncoding("UTF-8");
        if(request.getRequestURI().endsWith("/login")){
            response.getWriter().write(RestBean.success("登录成功").toJson());
        } else if (request.getRequestURI().endsWith("/logout")) {
            response.getWriter().write(RestBean.success("退出登录成功").toJson());
        }

    }
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException{
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(RestBean.failure(401,exception.getMessage()).toJson());
    }
}
