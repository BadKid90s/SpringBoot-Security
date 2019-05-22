package com.wry.security.service;

import com.wry.security.mapper.SysPermissionMapper;
import com.wry.security.mapper.SysUserMapper;
import com.wry.security.pojo.SysRole;
import com.wry.security.pojo.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserService implements UserDetailsService {
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    SysPermissionMapper sysPermissionMapper;


    //重写loadUserByUsername 方法获得 userdetails 类型用户
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        SysUser sysUsers = sysUserMapper.findByUsername(s);
        if (sysUsers == null) {
            throw new BadCredentialsException("用户不存在！");
        }
        if (sysUsers != null) {
            //用于添加用户的角色。只要把用户角色添加到authorities 就万事大吉。
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            for (SysRole role : sysUsers.getRoles()) {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            }
            //返回一个User对象(用户名，密码，权限）
            return new User(sysUsers.getUsername(), sysUsers.getPassword(), authorities);
        } else {
            throw new UsernameNotFoundException("admin: " + s + " do not exist!");
        }
    }
}
