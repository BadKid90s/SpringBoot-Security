package com.wry.security.pojo;

import lombok.Data;

import java.util.List;

@Data
public class SysPermission {
    private Integer id;

    private String name;

    private String descritpion;

    private String url;

    private Integer pid;

    private List<SysRole> roles;
}