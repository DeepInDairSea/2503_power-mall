package com.zkh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zkh.domain.LoginSysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;


@Mapper
public interface LoginSysUserMapper extends BaseMapper<LoginSysUser> {
    Set<String> selectPermsByLoginSysUserId(Long userId);
}