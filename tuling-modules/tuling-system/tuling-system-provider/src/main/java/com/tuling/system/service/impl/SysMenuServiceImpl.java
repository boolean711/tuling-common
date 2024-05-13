package com.tuling.system.service.impl;


import cn.hutool.core.util.ReflectUtil;
import com.tuling.common.core.param.BaseTreeVo;

import com.tuling.common.satoken.utils.LoginHelper;
import com.tuling.common.web.service.CrudBaseServiceImpl;
import com.tuling.system.domain.dto.SysMenuSaveDto;
import com.tuling.system.domain.entity.SysMenu;
import com.tuling.system.domain.vo.SysMenuVo;
import com.tuling.system.domain.vo.TreeMenuVo;
import com.tuling.system.mapper.SysMenuMapper;
import com.tuling.system.service.SysMenuService;
import com.tuling.system.service.SysRoleMenuRelService;
import com.tuling.system.service.SysRolePermissionRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class SysMenuServiceImpl extends CrudBaseServiceImpl<SysMenu, SysMenuVo, SysMenuSaveDto, SysMenuMapper> implements SysMenuService {


    @Autowired
    private SysRoleMenuRelService roleMenuRelService;

    @Override
    public List<Long> selectMenuCheckIdList() {

        return roleMenuRelService.getMenuIdListByRoleIds(LoginHelper.getRoleIdList());
    }

    @Override
    public List<TreeMenuVo> treeMenuSelect() {
        if (LoginHelper.isAdmin()) {

            //如果是超级管理员，返回所有菜单


            return super.buildTree(TreeMenuVo.class);
        }

        //TODO 如果是租户管理员 返回租户套餐里的菜单


        return new ArrayList<>(0);
    }
}
