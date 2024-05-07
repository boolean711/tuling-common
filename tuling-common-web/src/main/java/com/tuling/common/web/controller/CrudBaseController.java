package com.tuling.common.web.controller;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tuling.common.core.param.ApiResponse;
import com.tuling.common.core.param.BaseEntity;
import com.tuling.common.core.param.BaseSaveDto;
import com.tuling.common.core.param.BaseVo;
import com.tuling.common.mybatis.param.ExpressionQueryDto;
import com.tuling.common.web.service.CrudBaseIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public  class CrudBaseController
        <S extends CrudBaseIService<E, VO, SAVE>,
        E extends BaseEntity,
        VO extends BaseVo,
        SAVE extends BaseSaveDto> {


    @Autowired
    public S service;


    @PostMapping("/pageListByExpression")
    public ApiResponse<IPage<VO>> pageListByExpression(@RequestBody ExpressionQueryDto<E> queryDto, HttpServletRequest servletRequest) {
        return ApiResponse.success(service.pageListByExpression(queryDto));
    }

    @GetMapping("/getInfoById/{id}")
    public ApiResponse<VO> getInfoById(@PathVariable Long id) {

        return ApiResponse.success(service.getInfoById(id));
    }

    @PostMapping("/saveOrUpdate")
    public ApiResponse<Long> saveOrUpdate(@Validated @RequestBody SAVE dto) {
        return ApiResponse.success(service.saveOrUpdate(dto)).setShowMessage(true).setMessage("操作成功");
    }

    @PostMapping("/removeByIds")
    public ApiResponse<Boolean> removeByIds(@RequestBody List<Long> ids) {
        return ApiResponse.success(service.removeByIds(ids)).setShowMessage(true).setMessage("删除成功");
    }

}
