package com.zkh.service.imp;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zkh.domain.SysMenu;

import java.util.Set;

public interface SysMenuService extends IService<SysMenu>{

    /**
     * 查询菜单权限
     * @param loginUserId
     * @return
     */
    Set<SysMenu> queryUserMenuPermsByUserId(Long loginUserId);
}
