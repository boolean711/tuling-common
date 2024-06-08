package com.tuling.log.service;


import com.tuling.common.web.service.CrudBaseIService;
import com.tuling.log.domain.dto.SysUpdateLogSaveDto;
import com.tuling.log.domain.entity.SysUpdateLog;
import com.tuling.log.domain.vo.SysUpdateLogVo;

import java.util.List;


public interface SysUpdateLogService extends CrudBaseIService<SysUpdateLog, SysUpdateLogVo, SysUpdateLogSaveDto> {



    List<SysUpdateLogVo> getListByVersion(String version);
}
