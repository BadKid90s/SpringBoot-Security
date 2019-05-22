package com.wry.security.config.other;

import lombok.Getter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义的 web身份验证详情
 * 该类提供了获取用户登录时携带的额外信息的功能
 */
//使用lombok 注解提供Get方法
@Getter
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

    //定义需要获取的数据
    private final String verificationCode;


    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        //从表单元素中获取验证码
        verificationCode = request.getParameter("verificationCode");
    }

}
