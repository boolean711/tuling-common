package com.tuling.system.service;

import com.tuling.common.web.service.CrudBaseIService;
import com.tuling.system.domain.dto.SysRolePermissionRelSaveDto;
import com.tuling.system.domain.entity.SysRolePermissionRel;
import com.tuling.system.domain.vo.SysRolePermissionRelVo;

import java.util.List;
import java.util.Map;


public interface SysRolePermissionRelService extends CrudBaseIService<SysRolePermissionRel, SysRolePermissionRelVo, SysRolePermissionRelSaveDto> {




    Map<Long,Long> getRolePermissionIdMap(List<Long> roleId);


    void removeByRoleId(Long roleId);


}
