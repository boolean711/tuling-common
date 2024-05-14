package com.tuling.system.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.tuling.common.core.constants.PermissionConstants;
import com.tuling.common.web.controller.CrudBaseController;
import com.tuling.common.web.service.CrudBaseServiceImpl;
import com.tuling.system.domain.dto.SysTenantPackageSaveDto;
import com.tuling.system.domain.entity.SysTenantPackage;
import com.tuling.system.domain.vo.SysTenantPackageVo;
import com.tuling.system.mapper.SysTenantPackageMapper;
import com.tuling.system.service.SysTenantPackageService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/system/tenantPackage")
@SaCheckPermission(value = {PermissionConstants.ADMIN})
public class SysTenantPackageController extends CrudBaseController<SysTenantPackageService,SysTenantPackage,SysTenantPackageVo,SysTenantPackageSaveDto>  {




}
