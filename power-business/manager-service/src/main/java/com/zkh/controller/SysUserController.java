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

//        SecurityContextHolder.getContext().getAuthentication().getPrincipal();

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
    public Result<Page<SysUser>> getSysUserPage(@RequestParam() Long current,
                                                @RequestParam() Integer size,
                                                @RequestParam(required = false) String username
    ) {
        Page<SysUser> page = new Page<>(current, size);

        page = sysUserService.page(page, new LambdaQueryWrapper<SysUser>()
                .like(StringUtils.hasText(username), SysUser::getUsername, username)
                .orderByDesc(SysUser::getCreateTime, SysUser::getUserId)
        );          ////返回的也是page对象
        //通过用户 查询系统管理员列表，并且分页
        return Result.success(page);
    }

    @ApiOperation("添加系统管理员")
    @PostMapping
    @PreAuthorize("hasAuthority('sys:user:save')")
    public Result<String> get(@RequestBody SysUser sysUser) {
        //
        int count = sysUserService.saveSysUser(sysUser);

        return Result.handle(count > 0);
    }

    @ApiOperation("获取系统用户及角色")
    @GetMapping("info/{id}")
    @PreAuthorize("hasAuthority('sys:user:info')")
    public Result<SysUser> loadSysUserInfo(@PathVariable String id) {
        //以下代码只是查询了sysUser的info，但是没有查询sysuser对应的角色
//        Long l = 0L;
//        if (StringUtils.hasText(id)&&id!=""){
//             l = Long.valueOf(id);
//        }
//        SysUser sysUser = sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserId, l));
//        return Result.success(sysUser);
        /***
         * 前端所需要的数据
         *               this.dataForm.userName = data.username
         *               this.dataForm.email = data.email
         *               this.dataForm.mobile = data.mobile
         *               this.dataForm.roleIdList = data.roleIdList
         *               this.dataForm.status = data.status
         */
        Long l = 0L;
        if (StringUtils.hasText(id) && id != "") {
            l = Long.valueOf(id);
        }
        SysUser sysUser = sysUserService.querySysUserAndRoleListById(l);
        return Result.success(sysUser);
    }
}
