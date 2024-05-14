package com.tuling.system.controller;



import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.annotation.SaMode;
import com.tuling.common.core.constants.PermissionConstants;
import com.tuling.common.core.param.ApiResponse;
import com.tuling.common.web.controller.CrudBaseController;
import com.tuling.system.domain.dto.SysTenantSaveDto;
import com.tuling.system.domain.entity.SysTenant;
import com.tuling.system.domain.vo.SysTenantVo;
import com.tuling.system.service.SysTenantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/system/tenant")
@RestController
public class SysTenantController extends CrudBaseController<SysTenantService, SysTenant, SysTenantVo, SysTenantSaveDto> {


    @PostMapping("/resetPassword/{tenantId}")
    public ApiResponse<Boolean> resetPassword(@PathVariable("tenantId") Long tenantId) {
        return  ApiResponse.success(service.resetPassword(tenantId)).setShowMessage(true).setMessage("重置成功");
    }

    @Override
    @SaCheckPermission(value = {PermissionConstants.ADMIN})
    @PostMapping("/saveOrUpdate")
    public ApiResponse<Long> saveOrUpdate(@RequestBody SysTenantSaveDto dto) {
        return super.saveOrUpdate(dto);
    }

    @Override
    @SaCheckPermission(value = {PermissionConstants.ADMIN})
    @PostMapping("/removeByIds")
    public ApiResponse<Boolean> removeByIds(@RequestBody List<Long> ids) {
        return super.removeByIds(ids);
    }

    @PostMapping("/getTenantByUserName")
    @SaIgnore
    public ApiResponse<List<SysTenantVo>> getTenantByUserName(@RequestParam("userName") String userName) {

        List<SysTenantVo> tenantVoList = service.getTenantByUserName(userName);

        return ApiResponse.success(tenantVoList);
    }
}
