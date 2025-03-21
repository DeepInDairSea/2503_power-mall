package com.zkh.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zkh.domain.SysUser;
import com.zkh.domain.SysUserRole;
import com.zkh.mapper.SysUserMapper;
import com.zkh.service.imp.SysUserRoleService;
import com.zkh.service.imp.SysUserService;
import com.zkh.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    //    @Autowired
//    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 前端传到controller的数据
     * userId
     * username
     * password
     * email
     * mobile
     * status
     * shopId
     * roleIdList
     */
    @Override
    public Integer saveSysUser(SysUser sysUser) {
        //补充前端没有的数据
        sysUser.setCreateUserId(AuthUtils.getUserId());
        sysUser.setCreateTime(new Date());
        //整个系统就一个店铺
        sysUser.setShopId(1L);


        int insertCount = sysUserMapper.insert(sysUser);

        if (insertCount > 0) {
            //新增角色成功，更新sysUserRole
//            List<SysUserRole> sysUserRoles = new arrayList();
            ArrayList<SysUserRole> list = new ArrayList<>();
            SysUserRole sysUserRole = new SysUserRole();
            if (CollectionUtil.isNotEmpty(sysUser.getRoleIdList()) && sysUser.getRoleIdList().size() != 0) {
                //前端给新增的系统用户选择了角色
                sysUser.getRoleIdList().forEach(roleId -> {
                    //需要将用户和角色的关系进行绑定
                    //前端可能为一个用户选择了多个角色,
                    sysUserRole.setUserId(sysUser.getUserId());
                    sysUserRole.setRoleId(roleId);
                    list.add(sysUserRole);
                });
                //在循环中尽量不要操作数据库，如果数据量过大，则会开启关闭sqlSession太过频繁
                //可以使用mybatis plus 提供给service层批量操作方法saveBach()
                boolean isSuccessful = sysUserRoleService.saveBatch(list);
            }

        }
        //新增角色失败 则返回0

        return insertCount;
    }
}
