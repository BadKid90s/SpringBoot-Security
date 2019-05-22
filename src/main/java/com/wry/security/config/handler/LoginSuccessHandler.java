package com.wry.security.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wry.security.pojo.RespBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        RespBean respBean = RespBean.ok("登录成功!",  SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        ObjectMapper om = new ObjectMapper();
        PrintWriter out = httpServletResponse.getWriter();
        out.write(om.writeValueAsString(respBean));
        out.flush();
        out.close();
    }
}
