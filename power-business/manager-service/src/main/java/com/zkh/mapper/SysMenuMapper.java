package com.zkh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zkh.domain.SysMenu;

import java.util.Set;

public interface SysMenuMapper extends BaseMapper<SysMenu> {
    /**
     * 查询角色对应的菜单权限
     * @param loginUserId
     * @return
     */
    Set<SysMenu> queryUserMenuPermsByUserId(Long loginUserId);
}