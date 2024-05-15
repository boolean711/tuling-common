package com.tuling.system.service.impl;


import cn.hutool.core.util.ReflectUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.tuling.common.core.constants.PermissionConstants;
import com.tuling.common.core.exception.ServiceException;
import com.tuling.common.core.param.BaseEntity;
import com.tuling.common.core.param.BaseTreeVo;

import com.tuling.common.mybatis.param.ExpressionQueryDto;
import com.tuling.common.satoken.utils.LoginHelper;
import com.tuling.common.utils.BeanListUtils;
import com.tuling.common.web.service.CrudBaseServiceImpl;
import com.tuling.system.domain.dto.SysMenuSaveDto;
import com.tuling.system.domain.entity.SysMenu;
import com.tuling.system.domain.entity.SysRoleMenuRel;
import com.tuling.system.domain.entity.SysTenant;
import com.tuling.system.domain.entity.SysTenantPackage;
import com.tuling.system.domain.vo.SysMenuVo;
import com.tuling.system.domain.vo.SysTenantVo;
import com.tuling.system.domain.vo.TreeMenuVo;
import com.tuling.system.mapper.SysMenuMapper;
import com.tuling.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class SysMenuServiceImpl extends CrudBaseServiceImpl<SysMenu, SysMenuVo, SysMenuSaveDto, SysMenuMapper> implements SysMenuService {


    @Autowired
    private SysRoleMenuRelService roleMenuRelService;

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysTenantPackageService tenantPackageService;


    @Autowired
    private SysTenantService tenantService;

    @Override
    public void afterSave(SysMenuSaveDto dto, SysMenu entity) {

        Long adminRoleId = roleService.getRoleIdByPermissionCode(PermissionConstants.ADMIN);

        SysRoleMenuRel sysRoleMenuRel = new SysRoleMenuRel();

        sysRoleMenuRel.setMenuId(entity.getId());
        sysRoleMenuRel.setRoleId(adminRoleId);

        roleMenuRelService.save(sysRoleMenuRel);
    }

    @Override
    public List<SysMenuVo> getRouters() {
        List<SysMenuVo> res = null;
        if (LoginHelper.isAdmin()) {
            //如果是超级管理员，返回所有菜单
            ExpressionQueryDto<SysMenu> queryDto = new ExpressionQueryDto<>();

            queryDto.setNeedPage(false);

            res = this.pageListByExpression(queryDto).getRecords();
        } else {
            List<Long> menuIdListByRoleIds = roleMenuRelService.getMenuIdListByRoleIds(LoginHelper.getRoleIdList());

            if (CollectionUtils.isEmpty(menuIdListByRoleIds)) {
                throw new ServiceException("未知角色，请联系管理员");
            }

            List<SysMenu> sysMenus = this.listByIds(menuIdListByRoleIds);
            res = BeanListUtils.copyList(sysMenus, SysMenuVo.class);
        }
        res.sort(Comparator.comparing(SysMenuVo::getOrderNum));
        return res;


    }

    @Override
    public List<Long> selectMenuCheckIdList(Long roleId) {

        return roleMenuRelService.getMenuIdListByRoleIds(Collections.singletonList(roleId));
    }

    @Override
    public List<TreeMenuVo> treeMenuSelect() {
        //如果是超级管理员，返回所有菜单
        ExpressionQueryDto<SysMenu> queryDto = new ExpressionQueryDto<>();
        queryDto.setNeedPage(false);

        IPage<SysMenuVo> voiPage = this.pageListByExpression(queryDto);
        List<SysMenuVo> records = voiPage.getRecords();
        if (!LoginHelper.isAdmin()) {
            //TODO如果是普通用户，返回租户套餐中的菜单
            Long currentTenantId = LoginHelper.getCurrentTenantId();
            SysTenant tenant = tenantService.getById(currentTenantId);

            if (tenant == null) {
                throw new ServiceException("未知租户信息，请联系管理员");
            }
            SysTenantPackage tenantPackage = tenantPackageService.getById(tenant.getPackageId());

            if (tenantPackage == null) {
                throw new ServiceException("未知租户信息，请联系管理员");
            }
            List<String> packageMenuIds = Arrays.asList(tenantPackage.getMenuIds().split(","));

            records = records.stream().filter(item -> packageMenuIds.contains(String.valueOf(item.getId()))).collect(Collectors.toList());

        }

        return super.buildTree(TreeMenuVo.class, records);
    }

}
