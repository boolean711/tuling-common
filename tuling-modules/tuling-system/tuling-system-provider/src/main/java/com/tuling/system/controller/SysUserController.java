package com.tuling.system.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import com.tuling.common.core.constants.PermissionConstants;
import com.tuling.common.core.param.ApiResponse;
import com.tuling.common.web.controller.CrudBaseController;
import com.tuling.system.domain.dto.SysUserSaveDto;
import com.tuling.system.domain.entity.SysUser;
import com.tuling.system.domain.vo.SysUserVo;
import com.tuling.system.service.SysUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/system/user")
@SaCheckPermission(value = {PermissionConstants.ADMIN, PermissionConstants.TENANT_ADMIN}, mode = SaMode.OR)
public class SysUserController extends CrudBaseController<SysUserService, SysUser, SysUserVo, SysUserSaveDto> {


    @PostMapping("/changeUserStatus")
    public ApiResponse<String> changeUserStatus(@RequestParam("userId") Long userId, @RequestParam("status") String status) {

        service.changeUserStatus(userId, status);
        return ApiResponse.success("修改成功").setShowMessage(true);
    }



}
