package com.wry.security.service;

import com.wry.security.mapper.SysPermissionMapper;
import com.wry.security.pojo.SysPermission;
import com.wry.security.pojo.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

/**
 * Created by yangyibo on 17/1/19.
 */
@Service
public class CustomMetadataSource implements
        FilterInvocationSecurityMetadataSource {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    //此方法是为了判定用户请求的url 是否在权限表中，如果在权限表中，则返回给 decide 方法，用来判定用户是否有此权限。如果不在权限表中则放行。
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        //加载权限表中所有权限
        List<SysPermission> permissions = sysPermissionMapper.findByAll(null);
        //object 中包含用户请求的request 信息
        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        //获取Url
        String requestUrl = request.getRequestURI();
        //遍历权限资源
        for (SysPermission permission : permissions) {
            //判断请求Url是否在权限资源中
            if (antPathMatcher.match(permission.getUrl(), requestUrl)){
                //获取此权限对应的角色
                List<SysRole> roles = permission.getRoles();
                //返回一个新创建的角色集合
                int size = roles.size();
                String[] values = new String[size];
                for (int i = 0; i < size; i++) {
                    values[i] = roles.get(i).getName();
                }
                return SecurityConfig.createList(values);
            }
        }
        //没有匹配上的资源，都是登录访问
//        return SecurityConfig.createList("ROLE_LOGIN");
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
