package org.example.sessionbackend.interceptor;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.sessionbackend.entity.user.AccountUser;
import org.example.sessionbackend.mapper.UserMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
public class AuthorizeInterceptor implements HandlerInterceptor {

    @Resource
    UserMapper mapper;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();
        AccountUser accountUser = mapper.findAccountUserByNameOrEmail(username);
        request.getSession().setAttribute("account", accountUser);
        return true;
    }
}
