package com.wry.security.config.other;

import com.wry.security.service.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    CustomUserService customUserService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        //获取验证码
        CustomWebAuthenticationDetails webAuthenticationDetails = (CustomWebAuthenticationDetails) authentication.getDetails();
        String verificationCode = webAuthenticationDetails.getVerificationCode();
        System.out.println("---------> 验证码："+verificationCode);
        // 获取用户输入的用户名和密码
        String inputName = authentication.getName();
        String inputPassword = authentication.getCredentials().toString();

        // userDetails为数据库中查询到的用户信息
        UserDetails userDetails = customUserService.loadUserByUsername(inputName);

        // 如果是自定义AuthenticationProvider，需要手动密码校验
        if(!(new BCryptPasswordEncoder().matches(inputPassword,userDetails.getPassword()))) {
            throw new BadCredentialsException("密码错误");
        }

        return new UsernamePasswordAuthenticationToken(inputName, inputPassword, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        // 这里不要忘记，和UsernamePasswordAuthenticationToken比较
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
