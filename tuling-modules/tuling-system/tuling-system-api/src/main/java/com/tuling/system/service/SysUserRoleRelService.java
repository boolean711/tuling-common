package com.tuling.system.service;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tuling.common.core.param.BaseEntity;
import com.tuling.common.web.service.CrudBaseIService;
import com.tuling.system.domain.dto.SysUserRoleRelSaveDto;
import com.tuling.system.domain.entity.SysUserRoleRel;
import com.tuling.system.domain.vo.SysUserRoleRelVo;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;


public interface SysUserRoleRelService extends CrudBaseIService<SysUserRoleRel, SysUserRoleRelVo, SysUserRoleRelSaveDto> {



    Map<Long, List<Long>> getUserRoleIdMap(List<Long> userIds);
    void removeByUserId(Long userId);
}
