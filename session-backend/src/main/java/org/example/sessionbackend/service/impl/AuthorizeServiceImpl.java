package org.example.sessionbackend.service.impl;

import jakarta.annotation.Resource;
import org.example.sessionbackend.entity.auth.Account;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

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
    public String sendValidateEmail(String email, String sessionId, boolean hasAccount) {
        /*
          1. 生成验证码
          2. 邮箱与验证码存入redis(过期时间3分钟，重发邮件，剩余时间少于2分钟)
          3.发送
          4. 发送失败，删除redis插入的数据
          5. 注册时取出
         */
        //查询redis,避免频繁请求
        String key = "email:" + email + ":" + sessionId + ":" + hasAccount;
        if(Boolean.TRUE.equals(redisTemplate.hasKey(key))){
            Long expire = Optional.ofNullable(redisTemplate.getExpire(key, TimeUnit.SECONDS)).orElse(0L);
            System.out.println(expire);
            if(expire > 120){
                return "请求频繁，请稍后重试";
            }
        }
        //判断邮箱是否被使用
        Account account = mapper.findAccountByNameOrEmail(email);
        if(hasAccount && account == null){
            return "该邮箱未注册";
        } else if (!hasAccount && account != null) {
            return "该邮箱已被使用";
        }
        //生成验证码
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        //发送验证码
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject("注册验证码");
        message.setText("您的验证码是：" + code);
        try {
            mailSender.send(message);
            redisTemplate.opsForValue().set(key, String.valueOf(code),3, TimeUnit.MINUTES);
            return null;
        }catch (MailException e){
            e.printStackTrace();
            return "邮件发送失败，请检查邮件地址，联系管理员";
        }
    }

    @Override
    public String validateAndRegister(String username, String password, String email, String code, String sessionId) {
        String key = "email:" + email + ":" + sessionId + ":false";
        if(Boolean.TRUE.equals(redisTemplate.hasKey(key))){
            String s = redisTemplate.opsForValue().get(key);
            if(s == null){
                return "验证码失效，请重新获取验证码";
            }
            if(s.equals(code)){
                //使用后删除
                redisTemplate.delete(key);
                password = encoder.encode(password);
                if(mapper.createAccount(username, password, email) > 0){
                    return null;
                }else {
                    return "内部错误，请联系管理员";
                }
            }else {
                return "验证码错误,请检查后提交";
            }
        }else {
            return "请先获取验证码";
        }
    }

    /**
     * 验证邮件和验证码，并添加标记
     * @param email
     * @param code
     * @param sessionId
     * @return
     */
    @Override
    public String validateOnly(String email, String code, String sessionId) {
        //生成key
        String key = "email:" + email + ":" + sessionId + ":true";
        //判断是否获取验证码
        if(Boolean.TRUE.equals(redisTemplate.hasKey(key))){
            //得到验证码
            String s = redisTemplate.opsForValue().get(key);
            //判断是否失效
            if(s == null){
                return "验证码失效，请重新获取";
            }
            if(s.equals(code)){
                redisTemplate.delete(key);
                return null;
            }else return "验证码错误，请检查后提交";
        }else {
            return "请先获取验证码";
        }
    }

    @Override
    public boolean resetPassword(String password, String email) {
        password = encoder.encode(password);
        return mapper.resetPasswordByEmail(password,email) > 0;
    }
}
