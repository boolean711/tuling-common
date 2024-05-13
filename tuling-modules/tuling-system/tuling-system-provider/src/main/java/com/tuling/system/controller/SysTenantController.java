package com.tuling.system.controller;



import com.tuling.common.core.param.ApiResponse;
import com.tuling.common.web.controller.CrudBaseController;
import com.tuling.system.domain.dto.SysTenantSaveDto;
import com.tuling.system.domain.entity.SysTenant;
import com.tuling.system.domain.vo.SysTenantVo;
import com.tuling.system.service.SysTenantService;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/system/tenant")
@RestController
public class SysTenantController extends CrudBaseController<SysTenantService, SysTenant, SysTenantVo, SysTenantSaveDto> {


    @PostMapping("/resetPassword/{tenantId}")
    public ApiResponse<Boolean> resetPassword(@PathVariable("tenantId") Long tenantId) {

        return  ApiResponse.success(service.resetPassword(tenantId)).setShowMessage(true).setMessage("重置成功");

    }
}
