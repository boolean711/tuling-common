package com.tuling.system.controller;


import com.tuling.common.web.controller.CrudBaseController;
import com.tuling.common.web.service.CrudBaseServiceImpl;
import com.tuling.system.domain.dto.SysUpdateLogSaveDto;
import com.tuling.system.domain.entity.SysUpdateLog;
import com.tuling.system.domain.vo.SysUpdateLogVo;
import com.tuling.system.mapper.SysUpdateLogMapper;
import com.tuling.system.service.SysUpdateLogService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/system/updateLog")
public class SysUpdateLogController extends CrudBaseController<SysUpdateLogService,SysUpdateLog, SysUpdateLogVo, SysUpdateLogSaveDto>   {


}
