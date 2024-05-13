package com.tuling.common.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tuling.common.core.param.*;
import com.tuling.common.mybatis.param.ExpressionQueryDto;


import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public interface CrudBaseIService<E extends BaseEntity, VO extends BaseVo, SAVE extends BaseSaveDto> extends IService<E> {

    Long saveOrUpdate(SAVE dto);


    Boolean removeByIds(List<Long> ids);

    VO getInfoById(Long id);

    IPage<VO> pageListByExpression(ExpressionQueryDto<E> queryDto);


    Map<Long, VO> getIdVoMap(Predicate<E> predicate);

   <T extends BaseTreeVo>List<T> buildTree(Class<T> prototypeClass);

}
