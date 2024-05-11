package com.tuling.system.service.impl;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.tuling.common.core.constants.PermissionConstants;
import com.tuling.common.satoken.utils.LoginHelper;
import com.tuling.common.utils.BeanListUtils;
import com.tuling.common.web.service.CrudBaseServiceImpl;
import com.tuling.system.domain.dto.SysPermissionSaveDto;
import com.tuling.system.domain.entity.SysPermission;
import com.tuling.system.domain.vo.SysPermissionVo;
import com.tuling.system.mapper.SysPermissionMapper;
import com.tuling.system.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class SysPermissionServiceImpl
        extends CrudBaseServiceImpl<SysPermission, SysPermissionVo, SysPermissionSaveDto, SysPermissionMapper>
        implements SysPermissionService {

    @Override
    public void afterPageListByExpression(List<SysPermissionVo> records) {
        if (!LoginHelper.isAdmin()) {
            // 非超级管理员过滤掉超级管理员权限
            records.removeIf(item -> PermissionConstants.ADMIN.equals(item.getPermissionCode()));
        }

    }
}
