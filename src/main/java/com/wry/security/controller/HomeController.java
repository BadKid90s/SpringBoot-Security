package com.wry.security.controller;

import com.wry.security.pojo.RespBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Controller
public class HomeController {

    private Authentication authentication;

    @RequestMapping("/index")
    public String index2(Model model) {
        //获取
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        List<String> rolesNameList = new ArrayList<>();
        Collection<? extends GrantedAuthority> collection = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : collection) {
            rolesNameList.add(grantedAuthority.getAuthority());
            System.out.println(grantedAuthority.getAuthority());
        }

        RespBean msg = RespBean.ok("测试内容", "额外信息，只对" + userName + "显示");
        model.addAttribute("message", msg);
        model.addAttribute("roles", collection);
        model.addAttribute("rolesNameList", rolesNameList);
        return "index";
    }

    @RequestMapping("/admin")
    @ResponseBody
    public String hello() {
        return "hello admin";
    }
}