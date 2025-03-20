package com.zkh.vo;

import com.zkh.domain.SysMenu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * menu auth 封装对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuAndAuth {
    /**
     * menuList 菜单权限集合
     */
    private Set<SysMenu> menuList;
    /**
     * 操作权限集合
     */
    private Set<String> authorities;
}
