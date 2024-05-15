package com.tuling.system.service;


import com.baomidou.mybatisplus.annotation.TableName;
import com.tuling.common.core.param.BaseEntity;
import com.tuling.common.web.service.CrudBaseIService;
import com.tuling.system.domain.dto.SysRoleMenuRelSaveDto;
import com.tuling.system.domain.entity.SysRoleMenuRel;
import com.tuling.system.domain.vo.SysRoleMenuRelVo;

import java.util.List;


public interface SysRoleMenuRelService extends CrudBaseIService<SysRoleMenuRel, SysRoleMenuRelVo, SysRoleMenuRelSaveDto> {



    List<Long> getMenuIdListByRoleIds(List<Long> roleIds);

    void removeByRoleId(Long roleId);
}
