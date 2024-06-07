package com.tuling.system.service;


import com.baomidou.mybatisplus.annotation.TableName;
import com.tuling.common.core.param.BaseEntity;
import com.tuling.common.web.service.CrudBaseIService;
import com.tuling.system.domain.dto.SysUpdateLogSaveDto;
import com.tuling.system.domain.entity.SysUpdateLog;
import com.tuling.system.domain.vo.SysUpdateLogVo;
import lombok.Data;


public interface SysUpdateLogService extends CrudBaseIService<SysUpdateLog, SysUpdateLogVo, SysUpdateLogSaveDto> {


}
