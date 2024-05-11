package com.tuling.system.controller;


import com.tuling.common.web.controller.CrudBaseController;
import com.tuling.common.web.service.CrudBaseServiceImpl;
import com.tuling.system.domain.dto.SysPermissionSaveDto;
import com.tuling.system.domain.entity.SysPermission;
import com.tuling.system.domain.vo.SysPermissionVo;
import com.tuling.system.mapper.SysPermissionMapper;
import com.tuling.system.service.SysPermissionService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/system/permission")
public class SysPermissionController
        extends CrudBaseController<SysPermissionService, SysPermission, SysPermissionVo, SysPermissionSaveDto> {


}
