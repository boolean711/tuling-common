package com.tuling.system.service.impl;


import cn.hutool.core.util.ReflectUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.tuling.common.core.exception.ServiceException;
import com.tuling.common.core.param.BaseEntity;
import com.tuling.common.core.param.BaseTreeVo;

import com.tuling.common.mybatis.param.ExpressionQueryDto;
import com.tuling.common.satoken.utils.LoginHelper;
import com.tuling.common.utils.BeanListUtils;
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
import java.util.Comparator;
import java.util.List;


@Service
public class SysMenuServiceImpl extends CrudBaseServiceImpl<SysMenu, SysMenuVo, SysMenuSaveDto, SysMenuMapper> implements SysMenuService {


    @Autowired
    private SysRoleMenuRelService roleMenuRelService;

    @Override
    public List<SysMenuVo> getRouters() {
        List<SysMenuVo> res=null;
        if (LoginHelper.isAdmin()) {
            //如果是超级管理员，返回所有菜单
            ExpressionQueryDto<SysMenu> queryDto = new ExpressionQueryDto<>();

            queryDto.setNeedPage(false);

            res= this.pageListByExpression(queryDto).getRecords();
        }else{
            List<Long> menuIdListByRoleIds = roleMenuRelService.getMenuIdListByRoleIds(LoginHelper.getRoleIdList());

            if (CollectionUtils.isEmpty(menuIdListByRoleIds)) {
                throw new ServiceException("未知角色，请联系管理员");
            }

            List<SysMenu> sysMenus = this.listByIds(menuIdListByRoleIds);
            res=BeanListUtils.copyList(sysMenus, SysMenuVo.class);
        }
        res.sort(Comparator.comparing(SysMenuVo::getOrderNum));
        return res;


    }

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


        //TODO如果是普通用户，返回角色对应的菜单
        return new ArrayList<>(0);
    }

}
