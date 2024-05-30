package com.tuling.system.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import com.tuling.common.core.constants.PermissionConstants;
import com.tuling.common.core.param.ApiResponse;
import com.tuling.common.web.controller.CrudBaseController;
import com.tuling.log.annotations.OperationLog;
import com.tuling.system.domain.dto.SysTenantPackageSaveDto;
import com.tuling.system.domain.dto.SysUserSaveDto;
import com.tuling.system.domain.entity.SysUser;
import com.tuling.system.domain.vo.SysUserVo;
import com.tuling.system.service.SysUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/system/user")
public class SysUserController extends CrudBaseController<SysUserService, SysUser, SysUserVo, SysUserSaveDto> {


    @PostMapping("/changeUserStatus")
    @OperationLog(methodName = "changeUserStatus")
    @SaCheckPermission(value = {PermissionConstants.ADMIN, PermissionConstants.TENANT_ADMIN}, mode = SaMode.OR)
    public ApiResponse<String> changeUserStatus(@RequestParam("userId") Long userId, @RequestParam("status") String status) {

        service.changeUserStatus(userId, status);
        return ApiResponse.success("修改成功").setShowMessage(true);
    }

    @PostMapping("/resetPassword/{id}")
    @OperationLog(methodName = "resetPassword")
    @SaCheckPermission(value = {PermissionConstants.ADMIN, PermissionConstants.TENANT_ADMIN}, mode = SaMode.OR)
    public ApiResponse<Boolean> resetPassword(@PathVariable("id") Long id) {
        return  ApiResponse.success(service.resetPassword(id)).setShowMessage(true).setMessage("重置成功");
    }
    @Override
    @PostMapping("/saveOrUpdate")
    @OperationLog(methodName = "userSaveOrUpdate")
    public ApiResponse<Long> saveOrUpdate(@RequestBody SysUserSaveDto dto) {
        return super.saveOrUpdate(dto);
    }

    @Override
    @PostMapping("/removeByIds")
    @OperationLog(methodName = "userRemoveByIds")
    @SaCheckPermission(value = {PermissionConstants.ADMIN, PermissionConstants.TENANT_ADMIN}, mode = SaMode.OR)
    public ApiResponse<Boolean> removeByIds(@RequestBody List<Long> ids) {
        return super.removeByIds(ids);
    }

}
