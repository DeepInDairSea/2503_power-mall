package com.zkh.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zkh.constant.ManagerConstants;
import com.zkh.domain.SysRole;
import com.zkh.mapper.SysRoleMapper;
import com.zkh.service.imp.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@CacheConfig(cacheNames = "com.zkh.service.SysRoleServiceImpl")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService{
//    com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    @Cacheable(key = ManagerConstants.SYS_ALL_ROLE_LIST_KEY)
    public List<SysRole> queryAllRole() {
        return sysRoleMapper.selectList(new LambdaQueryWrapper<SysRole>()
                .orderByDesc(SysRole::getCreateTime)
        );
    }
}
