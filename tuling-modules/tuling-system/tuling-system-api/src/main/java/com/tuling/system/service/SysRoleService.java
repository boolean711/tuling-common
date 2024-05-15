package com.tuling.system.service;


import com.tuling.common.core.param.BaseSaveDto;
import com.tuling.common.web.service.CrudBaseIService;
import com.tuling.system.domain.dto.SysRoleSaveDto;
import com.tuling.system.domain.entity.SysRole;
import com.tuling.system.domain.vo.SysRoleVo;
import lombok.Data;

import java.util.List;


public interface SysRoleService extends CrudBaseIService<SysRole, SysRoleVo, SysRoleSaveDto> {


    List<String> getPermissionCodeByRoleIds(List<Long> roleIds);


    Long getRoleIdByPermissionCode(String permissionCode);


    List<SysRoleVo> getRoleListByUserId(Long userId);


}
