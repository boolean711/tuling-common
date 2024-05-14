package com.tuling.system.service;


import com.tuling.common.web.service.CrudBaseIService;
import com.tuling.system.domain.dto.SysUserSaveDto;
import com.tuling.system.domain.entity.SysUser;
import com.tuling.system.domain.vo.SysUserVo;


public interface SysUserService extends CrudBaseIService<SysUser, SysUserVo, SysUserSaveDto> {



    SysUserVo getUserByUsername(String username);


    void changeUserStatus(Long userId, String status);
}
