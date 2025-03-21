package com.zkh.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkh.domain.SysUser;
import com.zkh.model.Result;
import com.zkh.service.imp.SysUserService;
import com.zkh.util.AuthUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Api("系统管理员接口管理")
@RestController
@RequestMapping("sys/user")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;



//    @ApiOperation("查询登录用户信息")
//    @GetMapping("info")
//    public Result<SysUser> getSysUser(){

////        SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Long userId = AuthUtils.getUserId();
//        SysUser sysUser = sysUserService.getById(userId);
//        return Result.success(sysUser);
//    }
    @ApiOperation("查询登录用户信息")
    @GetMapping("info")
    public Result<SysUser> getSysUser() {
        Long userId = AuthUtils.getUserId();
        SysUser sysUser = sysUserService.getById(userId);
        return Result.success(sysUser);
    }

    @ApiOperation("查询系统管理员列表和分页接口")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('sys:user:page')")
    public Result<Page<SysUser>> getSysUserPage(@RequestParam()Long current,
                                                @RequestParam()Integer size,
                                                @RequestParam(required = false) String username
                                                ) {
        Page<SysUser> page = new Page<>(current,size);

        page = sysUserService.page(page, new LambdaQueryWrapper<SysUser>()
                .like(StringUtils.hasText(username), SysUser::getUsername, username)
                .orderByDesc(SysUser::getCreateTime,SysUser::getUserId)
        );          ////返回的也是page对象
        //通过用户 查询系统管理员列表，并且分页
        return Result.success(page);
    }

    @ApiOperation("添加系统管理员")
    @PostMapping
    public Result<String> get(@RequestBody SysUser sysUser) {
        //
        int count = sysUserService.saveSysUser(sysUser);

        return Result.handle(count > 0);
    }
}
