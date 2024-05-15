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


    /**
     * 根据用户id返回用户id对应的角色id集合
     * @param userIds
     * @return
     */
    Map<Long, List<Long>> getUserRoleIdMap(List<Long> userIds);


    /**
     * 根据用户id删除关联关系
     * @param userId
     */
    void removeByUserId(Long userId);
}
