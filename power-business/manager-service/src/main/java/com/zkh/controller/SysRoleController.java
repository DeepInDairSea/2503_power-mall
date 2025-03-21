package com.zkh.controller;

import com.zkh.domain.SysRole;
import com.zkh.model.Result;
import com.zkh.service.imp.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("系统角色接口管理")
@RestController
@RequestMapping("sys/role")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;

    @ApiOperation("获取所有系统角色接口")
    @GetMapping("list")
    public Result<List<SysRole>> getRoleList() {
        List<SysRole> roleList= sysRoleService.queryAllRole();
        return Result.success(roleList);
    }


}
