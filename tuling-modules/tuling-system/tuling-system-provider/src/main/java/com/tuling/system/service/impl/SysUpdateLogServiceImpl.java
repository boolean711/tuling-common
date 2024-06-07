package com.tuling.system.service.impl;


import com.tuling.common.web.service.CrudBaseIService;
import com.tuling.common.web.service.CrudBaseServiceImpl;
import com.tuling.system.domain.dto.SysUpdateLogSaveDto;
import com.tuling.system.domain.entity.SysUpdateLog;
import com.tuling.system.domain.vo.SysUpdateLogVo;
import com.tuling.system.mapper.SysUpdateLogMapper;
import com.tuling.system.service.SysUpdateLogService;
import org.springframework.stereotype.Service;


@Service
public class SysUpdateLogServiceImpl extends CrudBaseServiceImpl<SysUpdateLog, SysUpdateLogVo, SysUpdateLogSaveDto, SysUpdateLogMapper> implements SysUpdateLogService {


}
