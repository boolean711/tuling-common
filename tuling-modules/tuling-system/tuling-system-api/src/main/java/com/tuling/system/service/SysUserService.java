package com.tuling.system.service;


import com.tuling.common.web.service.CrudBaseIService;
import com.tuling.system.domain.dto.SysUserSaveDto;
import com.tuling.system.domain.entity.SysUser;
import com.tuling.system.domain.vo.SysUserVo;

import java.util.List;


public interface SysUserService extends CrudBaseIService<SysUser, SysUserVo, SysUserSaveDto> {



    List<SysUserVo> getUserByUsername(String username,Long tenantId);


    void changeUserStatus(Long userId, String status);
}
