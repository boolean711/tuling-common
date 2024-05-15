package com.tuling.system.service;


import com.tuling.common.web.service.CrudBaseIService;
import com.tuling.system.domain.dto.SysPermissionSaveDto;
import com.tuling.system.domain.entity.SysPermission;
import com.tuling.system.domain.vo.SysPermissionVo;

import java.util.List;


public interface SysPermissionService extends CrudBaseIService<SysPermission, SysPermissionVo, SysPermissionSaveDto> {




    SysPermissionVo getPermissionByCode(String code);

    /**
     * 根据角色id判断是否是指具有指定权限
     * @param roleId
     * @param permissionCode
     * @return
     */
    boolean isGivenPermissionByRoleId(Long roleId,List<String> permissionCode);






}
