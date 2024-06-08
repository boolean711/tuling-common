package com.tuling.log.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tuling.common.core.constants.PermissionConstants;
import com.tuling.common.core.param.ApiResponse;
import com.tuling.common.mybatis.param.ExpressionQueryDto;
import com.tuling.common.web.controller.CrudBaseController;
import com.tuling.log.domain.dto.SysUpdateLogSaveDto;
import com.tuling.log.domain.entity.SysUpdateLog;
import com.tuling.log.domain.vo.SysUpdateLogVo;
import com.tuling.log.service.SysUpdateLogService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/log/updateLog")
public class SysUpdateLogController extends CrudBaseController<SysUpdateLogService,SysUpdateLog, SysUpdateLogVo, SysUpdateLogSaveDto>   {

    @Override
    @PostMapping("/pageListByExpression")
    public ApiResponse<IPage<SysUpdateLogVo>> pageListByExpression(@RequestBody ExpressionQueryDto<SysUpdateLog> queryDto, HttpServletRequest servletRequest) {
        return super.pageListByExpression(queryDto, servletRequest);
    }

    @Override
    @SaCheckPermission(PermissionConstants.ADMIN)
    @GetMapping("/getInfoById/{id}")
    public ApiResponse<SysUpdateLogVo> getInfoById(@PathVariable  Long id) {
        return super.getInfoById(id);
    }

    @Override
    @SaCheckPermission(PermissionConstants.ADMIN)
    @PostMapping("/saveOrUpdate")
    public ApiResponse<Long> saveOrUpdate(@RequestBody @Validated SysUpdateLogSaveDto dto) {
        return super.saveOrUpdate(dto);
    }

    @Override
    @PostMapping("/removeByIds")
    @SaCheckPermission(PermissionConstants.ADMIN)
    public ApiResponse<Boolean> removeByIds(@RequestBody  List<Long> ids) {
        return super.removeByIds(ids);
    }
}
