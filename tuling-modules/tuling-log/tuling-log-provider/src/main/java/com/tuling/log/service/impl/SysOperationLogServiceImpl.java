package com.tuling.log.service.impl;


import com.tuling.common.web.service.CrudBaseIService;
import com.tuling.common.web.service.CrudBaseServiceImpl;
import com.tuling.log.domain.dto.SysOperationLogSaveDto;
import com.tuling.log.domain.entity.SysOperationLog;
import com.tuling.log.domain.vo.SysOperationLogVo;
import com.tuling.log.mapper.SysOperationLogMapper;
import com.tuling.log.service.SysOperationLogService;
import org.springframework.stereotype.Service;

@Service
public class SysOperationLogServiceImpl extends CrudBaseServiceImpl<SysOperationLog, SysOperationLogVo, SysOperationLogSaveDto, SysOperationLogMapper> implements SysOperationLogService {




}
