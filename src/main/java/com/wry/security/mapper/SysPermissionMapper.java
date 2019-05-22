package com.wry.security.mapper;
import com.wry.security.pojo.SysPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysPermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysPermission record);

    int insertSelective(SysPermission record);

    SysPermission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysPermission record);

    int updateByPrimaryKey(SysPermission record);

    List<SysPermission> findByAll(SysPermission sysPermission);

    List<SysPermission> findByAdminUserId(@Param("userId") Integer userId);


}