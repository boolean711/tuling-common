package com.tuling.system.service.impl;


import com.tuling.common.web.service.CrudBaseIService;
import com.tuling.common.web.service.CrudBaseServiceImpl;
import com.tuling.system.domain.dto.SysTenantPackageSaveDto;
import com.tuling.system.domain.entity.SysTenantPackage;
import com.tuling.system.domain.vo.SysTenantPackageVo;
import com.tuling.system.mapper.SysTenantPackageMapper;
import com.tuling.system.service.SysTenantPackageService;
import org.springframework.stereotype.Service;


@Service
public class SysTenantPackageServiceImpl extends CrudBaseServiceImpl<SysTenantPackage,SysTenantPackageVo,SysTenantPackageSaveDto, SysTenantPackageMapper> implements SysTenantPackageService {




}
