package com.zkh.service;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zkh.mapper.SysRoleMenuMapper;
import com.zkh.domain.SysRoleMenu;
import com.zkh.service.imp.SysRoleMenuService;
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService{

}
