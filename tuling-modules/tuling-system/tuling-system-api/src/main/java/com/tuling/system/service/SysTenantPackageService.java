package com.tuling.system.service;


import com.tuling.common.core.param.BaseSaveDto;
import com.tuling.common.web.service.CrudBaseIService;
import com.tuling.system.domain.dto.SysTenantPackageSaveDto;
import com.tuling.system.domain.entity.SysTenantPackage;
import com.tuling.system.domain.vo.SysTenantPackageVo;
import lombok.Data;


public interface SysTenantPackageService extends CrudBaseIService<SysTenantPackage,SysTenantPackageVo,SysTenantPackageSaveDto> {


  void   resetTenantAdminRoleMenus(Long id);

}
