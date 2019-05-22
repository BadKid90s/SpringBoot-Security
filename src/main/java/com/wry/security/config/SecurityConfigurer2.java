package com.wry.security.config;

import com.wry.security.config.handler.LoginFailHandler;
import com.wry.security.config.handler.LoginSuccessHandler;
import com.wry.security.config.handler.MyLogoutSuccessHandler;
import com.wry.security.service.CustomAccessDecisionManager;
import com.wry.security.service.CustomMetadataSource;
import com.wry.security.service.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * 适用于全后端分离
 */
//@EnableWebSecurity
public class SecurityConfigurer2 extends WebSecurityConfigurerAdapter {
    //从数据库获取的用户信息  1
    @Autowired
    CustomUserService customUserService;
    //自定义的安全目标资源（数据库获取的权限资源）
    @Autowired
    private CustomMetadataSource securityMetadataSource;
    //自定义的操作授权管理器（匹配数据库获取的权限资源进行认证放行）
    @Autowired
    private CustomAccessDecisionManager accessDecisionManager;
    //前后端分离时使用的登录失败Handler
    @Autowired
    LoginFailHandler loginFailHandler;
    //前后端分离时使用的登录成功Handler
    @Autowired
    LoginSuccessHandler loginSuccessHandler;
    //前后端分离时使用的注销成功Handler
    @Autowired
    MyLogoutSuccessHandler myLogoutSuccessHandler;

    //资源访问权限设置
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
//                //实现后置处理器添加自定义的权限拦截器
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setSecurityMetadataSource(securityMetadataSource);
                        o.setAccessDecisionManager(accessDecisionManager);
                        return o;
                    }
                })
                .anyRequest().authenticated()
                .and()
                //开启登录
                .formLogin()
                //设置登录页
                .loginPage("/login")
                //前后端分离时使用
                //登录失败返回的信息
                .failureHandler(loginFailHandler)
                //登录成功返回的信息
                .successHandler(loginSuccessHandler)
                .permitAll()
                .and()
                //注销
                .logout()
                //前后端分离时使用
                .logoutSuccessHandler(myLogoutSuccessHandler)
                .permitAll()
                .and()
                //权限拒绝的页面
                .exceptionHandling().accessDeniedPage("/403");
    }

    //身份验证管理器配置
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //添加自定义的用户 （上边选择customUserService 时使用本方法）  1
        auth.userDetailsService(customUserService).passwordEncoder(new BCryptPasswordEncoder());
    }

    //web资源配置
    @Override
    public void configure(WebSecurity web) throws Exception {
        //解决静态资源被拦截的问题
        web.ignoring().antMatchers("/css/**", "/js/**");
    }
}
