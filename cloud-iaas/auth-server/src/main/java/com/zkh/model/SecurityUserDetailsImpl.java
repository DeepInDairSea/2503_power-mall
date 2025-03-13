package com.zkh.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Repository
public class SecurityUserDetailsImpl implements UserDetails {
    /**
     * 商城管理系统用户相关属性
     */
    private Long userId;
    private String username;
    private String password;
    private Integer status;
    private Long shopId;
    private Set<String> perms = new HashSet<>();
    private String loginType;


    /***
     *商城购物系统用户的相关属性
     */


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.loginType + this.userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.status == 1;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.status == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.status == 1;
    }

    @Override
    public boolean isEnabled() {
        return this.status == 1;
    }

    //处理一个权限perms，包含多条权限
    public Set<String> getPerms() {
        //创建一个perms容器
        HashSet<String> permsSet = new HashSet<>();

        //一条权限有可能有多个权限，做分割
        perms.forEach(perm -> {
            if (perm.contains(",")) {
                //含有逗号
                String[] splitPerms = perm.split(",");
                for (String splitPerm : splitPerms) {
                    permsSet.add(splitPerm);
                }
            } else {
                //没有逗号
                permsSet.add(perm);
            }
        });
        // this.perms = permsSet;
        return permsSet;
    }
}
