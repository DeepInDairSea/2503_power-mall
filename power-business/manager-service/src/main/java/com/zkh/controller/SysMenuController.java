package com.zkh.controller;

import com.zkh.domain.SysMenu;
import com.zkh.model.Result;
import com.zkh.service.imp.SysMenuService;
import com.zkh.util.AuthUtils;
import com.zkh.vo.MenuAndAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Api(tags = "系统按钮权限控制器")
@RestController
@RequestMapping("sys/menu/")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;
    @GetMapping("nav")
    @ApiOperation("查询用户菜单权限和操作权限")
    public Result<MenuAndAuth> loadUserMenuAndAuth(){
        //从请求里面获取用户标识
//        SecurityUserDetailsImpl  userDetails= (SecurityUserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Long userId = userDetails.getUserId();
        //使用工具类从请求里面获取用户标识
        Long loginUserId = AuthUtils.getUserId();
        //使用工具类获取用户操作权限 type = 2
        Set<String> userPerms = AuthUtils.getUserPerms();
        //获取用户的菜单目录权限 type = 1 | 0
        Set<SysMenu> userMenuPerms =sysMenuService.queryUserMenuPermsByUserId(loginUserId);
//        ObjectMapper objectMapper = new ObjectMapper();
        //封装到菜单和操作权限集合对象中
        MenuAndAuth menuAndAuth = MenuAndAuth.builder().menuList(userMenuPerms).authorities(userPerms).build();

        return Result.success(menuAndAuth);
    }
}
