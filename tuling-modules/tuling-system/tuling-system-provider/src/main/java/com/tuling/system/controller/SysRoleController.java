package com.tuling.system.controller;


import com.tuling.common.web.controller.CrudBaseController;
import com.tuling.common.web.service.CrudBaseServiceImpl;
import com.tuling.system.domain.dto.SysRoleSaveDto;
import com.tuling.system.domain.entity.SysRole;
import com.tuling.system.domain.vo.SysRoleVo;
import com.tuling.system.mapper.SysRoleMapper;
import com.tuling.system.service.SysRoleService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/system/role")
public class SysRoleController extends CrudBaseController<SysRoleService, SysRole, SysRoleVo, SysRoleSaveDto> {


}
