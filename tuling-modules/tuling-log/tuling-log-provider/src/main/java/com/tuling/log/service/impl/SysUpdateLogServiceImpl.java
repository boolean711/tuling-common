package com.tuling.log.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tuling.common.utils.BeanListUtils;
import com.tuling.common.web.service.CrudBaseServiceImpl;
import com.tuling.log.domain.dto.SysUpdateLogSaveDto;
import com.tuling.log.domain.entity.SysUpdateLog;
import com.tuling.log.domain.vo.SysUpdateLogVo;
import com.tuling.log.mapper.SysUpdateLogMapper;
import com.tuling.log.service.SysUpdateLogService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SysUpdateLogServiceImpl extends CrudBaseServiceImpl<SysUpdateLog, SysUpdateLogVo, SysUpdateLogSaveDto, SysUpdateLogMapper> implements SysUpdateLogService {


    @Override
    public List<SysUpdateLogVo> getListByVersion(String version) {
        LambdaQueryWrapper<SysUpdateLog> lqw = new LambdaQueryWrapper<>();

        lqw.eq(SysUpdateLog::getVersion, version);

        return BeanListUtils.copyList(list(lqw), SysUpdateLogVo.class);
    }
}
