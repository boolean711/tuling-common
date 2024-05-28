package com.tuling.log.service;


import com.baomidou.mybatisplus.annotation.TableName;
import com.tuling.common.core.param.BaseEntity;
import com.tuling.common.web.service.CrudBaseIService;
import com.tuling.log.domain.dto.SysOperationLogSaveDto;
import com.tuling.log.domain.entity.SysOperationLog;
import com.tuling.log.domain.vo.SysOperationLogVo;
import lombok.Data;

import java.util.Date;

public interface SysOperationLogService extends CrudBaseIService<SysOperationLog, SysOperationLogVo, SysOperationLogSaveDto> {




}
