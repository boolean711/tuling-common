package com.tuling.system.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.annotation.SaMode;
import com.tuling.common.core.constants.PermissionConstants;
import com.tuling.common.core.param.ApiResponse;
import com.tuling.common.web.controller.CrudBaseController;
import com.tuling.log.annotations.OperationLog;
import com.tuling.system.domain.dto.SysTenantSaveDto;
import com.tuling.system.domain.entity.SysTenant;
import com.tuling.system.domain.vo.SysTenantVo;
import com.tuling.system.service.SysTenantService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/system/tenant")
@RestController
public class SysTenantController extends CrudBaseController<SysTenantService, SysTenant, SysTenantVo, SysTenantSaveDto> {


    @Override
    @SaCheckPermission(value = {PermissionConstants.ADMIN, PermissionConstants.TENANT_ADMIN}, mode = SaMode.OR)
    @PostMapping("/saveOrUpdate")
    @OperationLog(methodName = "tenantSaveOrUpdate")
    public ApiResponse<Long> saveOrUpdate(@RequestBody @Validated SysTenantSaveDto dto) {
        return super.saveOrUpdate(dto);
    }

    @Override
    @SaCheckPermission(value = {PermissionConstants.ADMIN})
    @PostMapping("/removeByIds")
    @OperationLog(methodName = "tenantRemoveByIds")
    public ApiResponse<Boolean> removeByIds(@RequestBody List<Long> ids) {
        return super.removeByIds(ids);
    }

    @SaCheckPermission(value = {PermissionConstants.ADMIN})
    @PostMapping("/renew")
    @OperationLog(methodName = "tenantRenew")
    public ApiResponse<Void> renew(@RequestParam("numMonth") Integer numMonth,@RequestParam("id") Long id) {
        service.renew(numMonth,id);
        return ApiResponse.successNoData().setMessage("续费成功").setShowMessage(true);
    }

    @PostMapping("/getTenantByUserName")
    @SaIgnore
    public ApiResponse<List<SysTenantVo>> getTenantByUserName(@RequestParam("userName") String userName) {

        List<SysTenantVo> tenantVoList = service.getTenantByUserName(userName);

        return ApiResponse.success(tenantVoList);
    }


}
