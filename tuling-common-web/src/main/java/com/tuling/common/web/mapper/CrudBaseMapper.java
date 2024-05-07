package com.tuling.common.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tuling.common.core.param.BaseEntity;

public interface CrudBaseMapper<E extends BaseEntity> extends BaseMapper<E> {
}
