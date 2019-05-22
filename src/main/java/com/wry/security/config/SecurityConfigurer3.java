package com.wry.security.config;

import com.wry.security.config.other.CustomAuthenticationDetailsSource;
import com.wry.security.config.other.CustomAuthenticationProvider;
import com.wry.security.service.CustomAccessDecisionManager;
import com.wry.security.service.CustomMetadataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * 自定义了验证码验证
 */
@EnableWebSecurity
public class SecurityConfigurer3 extends WebSecurityConfigurerAdapter {

    //自定义登录验证  2  （1或2选择一个就可以）
    @Autowired
    CustomAuthenticationProvider customAuthenticationProvider;
    //自定义的安全目标资源（数据库获取的权限资源）
    @Autowired
    private CustomMetadataSource securityMetadataSource;
    //自定义的操作授权管理器（匹配数据库获取的权限资源进行认证放行）
    @Autowired
    private CustomAccessDecisionManager accessDecisionManager;
    //自定义的角色认证详情资源
    @Autowired
    private CustomAuthenticationDetailsSource customAuthenticationDetailsSource;

    //资源访问权限设置
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //实现后置处理器添加自定义的权限拦截器
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
                //设置自定义的认证详情资源
                .authenticationDetailsSource(customAuthenticationDetailsSource)
                //设置登录页
                .loginPage("/login")
                //登陆失败的处理
                .failureUrl("/login?error=true")
                .successForwardUrl("/index")
                .permitAll()
                .and()
                //注销
                .logout()
                .permitAll()
                .and()
                //权限拒绝的页面
                .exceptionHandling().accessDeniedPage("/403");
    }

    //身份验证管理器配置
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //自定义登录验证  2
        auth.authenticationProvider(customAuthenticationProvider);
    }

    //web资源配置
    @Override
    public void configure(WebSecurity web) throws Exception {
        //解决静态资源被拦截的问题
        web.ignoring().antMatchers("/css/**", "/js/**");
    }

//    //进行密码加密处理
//    public static void main(String[] args) {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String str = encoder.encode("admin");
//        System.out.println(str);
//
//        boolean a = encoder.matches("admin", str);
//        System.out.println(a);
//
//    }
}
