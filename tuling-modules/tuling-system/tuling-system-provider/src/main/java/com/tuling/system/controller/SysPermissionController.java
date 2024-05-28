package com.tuling.system.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.tuling.common.core.constants.PermissionConstants;
import com.tuling.common.core.param.ApiResponse;
import com.tuling.common.web.controller.CrudBaseController;
import com.tuling.common.web.service.CrudBaseServiceImpl;
import com.tuling.log.annotations.OperationLog;
import com.tuling.system.domain.dto.SysPermissionSaveDto;
import com.tuling.system.domain.entity.SysPermission;
import com.tuling.system.domain.vo.SysPermissionVo;
import com.tuling.system.mapper.SysPermissionMapper;
import com.tuling.system.service.SysPermissionService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/system/permission")
@SaCheckPermission(PermissionConstants.ADMIN)
public class SysPermissionController
        extends CrudBaseController<SysPermissionService, SysPermission, SysPermissionVo, SysPermissionSaveDto> {
    @Override
    @PostMapping("/saveOrUpdate")
    @OperationLog(methodName = "permissionSaveOrUpdate")
    public ApiResponse<Long> saveOrUpdate(SysPermissionSaveDto dto) {
        return super.saveOrUpdate(dto);
    }

    @Override
    @PostMapping("/removeByIds")
    @OperationLog(methodName = "permissionRemoveByIds")
    public ApiResponse<Boolean> removeByIds(@RequestBody List<Long> ids) {
        return super.removeByIds(ids);
    }
}
