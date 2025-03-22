package com.zkh.service.imp;

import com.zkh.domain.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
public interface SysUserService extends IService<SysUser>{


    Integer saveSysUser(SysUser sysUser);


    SysUser querySysUserAndRoleListById(Long l);
}
