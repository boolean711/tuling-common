package com.tuling.log.service.impl;


import com.tuling.common.web.service.CrudBaseServiceImpl;
import com.tuling.log.domain.dto.SysUpdateLogSaveDto;
import com.tuling.log.domain.entity.SysUpdateLog;
import com.tuling.log.domain.vo.SysUpdateLogVo;
import com.tuling.log.mapper.SysUpdateLogMapper;
import com.tuling.log.service.SysUpdateLogService;
import org.springframework.stereotype.Service;


@Service
public class SysUpdateLogServiceImpl extends CrudBaseServiceImpl<SysUpdateLog, SysUpdateLogVo, SysUpdateLogSaveDto, SysUpdateLogMapper> implements SysUpdateLogService {


}
