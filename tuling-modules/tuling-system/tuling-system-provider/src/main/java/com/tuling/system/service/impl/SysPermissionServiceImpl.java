package com.tuling.system.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.tuling.common.core.constants.PermissionConstants;
import com.tuling.common.core.exception.ServiceException;
import com.tuling.common.satoken.utils.LoginHelper;
import com.tuling.common.utils.BeanListUtils;
import com.tuling.common.web.service.CrudBaseServiceImpl;
import com.tuling.system.domain.dto.SysPermissionSaveDto;
import com.tuling.system.domain.entity.SysPermission;
import com.tuling.system.domain.vo.SysPermissionVo;
import com.tuling.system.mapper.SysPermissionMapper;
import com.tuling.system.service.SysPermissionService;
import com.tuling.system.service.SysRolePermissionRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class SysPermissionServiceImpl
        extends CrudBaseServiceImpl<SysPermission, SysPermissionVo, SysPermissionSaveDto, SysPermissionMapper>
        implements SysPermissionService {


    @Autowired
    private SysRolePermissionRelService rolePermissionRelService;


    @Override
    public void afterPageListByExpression(List<SysPermissionVo> records) {
        if (!LoginHelper.isAdmin()) {
            // 非超级管理员过滤掉超级管理员权限
            records.removeIf(item -> PermissionConstants.ADMIN.equals(item.getPermissionCode()));
        }

    }


    @Override
    public SysPermissionVo getPermissionByCode(String code) {

        LambdaQueryWrapper<SysPermission> lqw = new LambdaQueryWrapper<>();

        lqw.eq(SysPermission::getPermissionCode, code);
        SysPermission sysPermission = this.getOne(lqw);


        return BeanUtil.toBean(sysPermission, SysPermissionVo.class);
    }

    @Override
    public boolean isGivenPermissionByRoleId(Long roleId, List<String> permissionCodeList) {
        if (roleId == null || CollectionUtils.isEmpty(permissionCodeList)) {
            throw new ServiceException("未知参数");
        }

        Map<Long, Long> rolePermissionIdMap = rolePermissionRelService.getRolePermissionIdMap(Collections.singletonList(roleId));

        Long permissionId = rolePermissionIdMap.get(roleId);

        if (permissionId != null) {
            SysPermission sysPermission = this.getById(permissionId);


            if (sysPermission != null) {
                return permissionCodeList.contains(sysPermission.getPermissionCode());
            }

        }


        return false;
    }


}
