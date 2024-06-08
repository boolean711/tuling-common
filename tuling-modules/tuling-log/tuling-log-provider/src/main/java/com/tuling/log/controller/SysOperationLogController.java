package com.tuling.log.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.tuling.common.core.constants.PermissionConstants;
import com.tuling.common.web.controller.CrudBaseController;
import com.tuling.common.web.service.CrudBaseServiceImpl;
import com.tuling.log.domain.dto.SysOperationLogSaveDto;
import com.tuling.log.domain.entity.SysOperationLog;
import com.tuling.log.domain.vo.SysOperationLogVo;
import com.tuling.log.mapper.SysOperationLogMapper;
import com.tuling.log.service.SysOperationLogService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log/operation")
@SaCheckPermission(PermissionConstants.ADMIN)
public class SysOperationLogController extends CrudBaseController<SysOperationLogService,SysOperationLog, SysOperationLogVo, SysOperationLogSaveDto> {




}
