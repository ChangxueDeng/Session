package org.example.sessionbackend.service.impl;

import jakarta.annotation.Resource;
import org.example.sessionbackend.entity.Account;
import org.example.sessionbackend.mapper.UserMapper;
import org.example.sessionbackend.service.AuthorizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AuthorizeServiceImpl implements AuthorizeService {
    @Resource
    UserMapper mapper;
    @Resource
    MailSender mailSender;
    @Value("${spring.mail.username}")
    String from;
    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username == null){
            throw new UsernameNotFoundException("用户名不能为空");
        }
        Account account = mapper.findAccountByNameOrEmail(username);
        if(account == null){
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        return User.withUsername(account.getUsername())
                .password(account.getPassword())
                .roles("user")
                .build();
    }

    @Override
    public boolean sendValidateEmail(String email, String sessionId) {
        /**
         * 1. 生成验证码
         * 2. 邮箱与验证码存入redis(过期时间3分钟，重发邮件，剩余时间少于2分钟)
         * 3.发送
         * 4. 发送失败，删除redis插入的数据
         * 5. 注册时取出
         */
        String key = "email:" + email + ":" + sessionId;
        if(Boolean.TRUE.equals(redisTemplate.hasKey(key))){
            Long expire = Optional.ofNullable(redisTemplate.getExpire(key, TimeUnit.SECONDS)).orElse(0L);
            System.out.println(expire);
            if(expire > 120){
                return false;
            }
        }
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject("注册验证码");
        message.setText("您的验证码是：" + code);
        try {
            mailSender.send(message);
            redisTemplate.opsForValue().set(key, String.valueOf(code),3, TimeUnit.MINUTES);
            return true;
        }catch (MailException e){
            e.printStackTrace();
            return false;
        }
    }
}
