package com.tuling.system.service.impl;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.tuling.common.web.service.CrudBaseIService;
import com.tuling.common.web.service.CrudBaseServiceImpl;
import com.tuling.system.domain.dto.SysTenantPackageSaveDto;
import com.tuling.system.domain.entity.SysRoleMenuRel;
import com.tuling.system.domain.entity.SysTenantPackage;
import com.tuling.system.domain.vo.SysTenantPackageVo;
import com.tuling.system.mapper.SysRoleMapper;
import com.tuling.system.mapper.SysTenantPackageMapper;
import com.tuling.system.service.SysRoleMenuRelService;
import com.tuling.system.service.SysTenantPackageService;
import com.tuling.system.service.SysTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class SysTenantPackageServiceImpl extends CrudBaseServiceImpl<SysTenantPackage, SysTenantPackageVo, SysTenantPackageSaveDto, SysTenantPackageMapper> implements SysTenantPackageService {


    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleMenuRelService roleMenuRelService;


    @Override
    public void afterSave(SysTenantPackageSaveDto dto, SysTenantPackage entity) {
        //租户套餐修改后，所有租户管理员绑定菜单关系需要重置


        this.resetTenantAdminRoleMenus(entity);
    }

    @Override
    public void resetTenantAdminRoleMenus(Long id) {
        SysTenantPackage tenantPackage = this.getById(id);

        if (tenantPackage != null) {
            this.resetTenantAdminRoleMenus(tenantPackage);
        }


    }

    public void resetTenantAdminRoleMenus(SysTenantPackage entity) {
        List<Long> tenantAdminRoleIds = roleMapper.selectTenantAdminRoleIdByPackageId(entity.getId());

        if (CollectionUtils.isNotEmpty(tenantAdminRoleIds)) {
            roleMenuRelService.removeByRoleId(tenantAdminRoleIds);
            List<Long> menuIds = Arrays.stream(entity.getMenuIds().split(",")).map(Long::parseLong).collect(Collectors.toList());
            List<SysRoleMenuRel> roleMenuRelList = new ArrayList<>();

            for (Long roleId : tenantAdminRoleIds) {
                for (Long menuId : menuIds) {
                    SysRoleMenuRel roleMenuRel = new SysRoleMenuRel();
                    roleMenuRel.setRoleId(roleId);
                    roleMenuRel.setMenuId(menuId);
                    roleMenuRelList.add(roleMenuRel);
                }
            }

            roleMenuRelService.saveBatch(roleMenuRelList);
        }

    }
}
