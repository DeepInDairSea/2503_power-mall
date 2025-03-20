package com.zkh.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zkh.domain.SysMenu;
import com.zkh.mapper.SysMenuMapper;
import com.zkh.service.imp.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "com.zkh.service.SysMenuServiceImpl")   //权限改动的少，查询的频率高，所以给权限加进缓存
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService{
    @Autowired
    private SysMenuMapper sysMenuMapper;
    /**
     *获取用户菜单权限 
     * @param loginUserId
     * @return
     */
    @Override
    @Cacheable(key = "#loginUserId")    //缓存的key ,把这个查询的结果根据loginUserId存redis中
    public Set<SysMenu> queryUserMenuPermsByUserId(Long loginUserId) {
//        SysMenu sysMenu = sysMenuMapper.selectById(loginUserId);
        Set<SysMenu> userMenus = sysMenuMapper.queryUserMenuPermsByUserId(loginUserId);
        //将权限集合转换成Tree树结构层级关系的set集合，传入的根节点的父节点id，根节点没有父节点所以pid是0
        return translationTree(userMenus,0L);
    }

    /***
     *我们这里默认只有两个层级
     */
//    private Set<SysMenu> translationTree(Set<SysMenu> userMenuPerms, Long pid) {
//        //查询出没有父节点的根节点
//        Set<SysMenu> roots = userMenuPerms.stream()
//                .filter(mPerms -> mPerms.getParentId().equals(pid))
//                .collect(Collectors.toSet());
//        //循环遍历出每个根节点的子节点
//        roots.forEach(root -> {
//            Set<SysMenu> child = userMenuPerms.stream().filter(menu -> menu.getParentId().equals(root.getMenuId())).collect(Collectors.toSet());
//            root.setMenuSet(child);
//        });
//        return roots;
//    }
    /**
     * 默认不知道层级有多少
     * 递归
     */
    private Set<SysMenu> translationTree(Set<SysMenu> userMenus,Long pid){
        //过滤出根节点
        Set<SysMenu> roots = userMenus.stream()
                .filter(menu -> menu.getParentId().equals(pid))
                .collect(Collectors.toSet());
        //遍历根节点的子节点
        roots.forEach(root -> {
            root.setMenuSet(translationTree(userMenus,root.getMenuId()));
        });
        return roots;
    }
}
