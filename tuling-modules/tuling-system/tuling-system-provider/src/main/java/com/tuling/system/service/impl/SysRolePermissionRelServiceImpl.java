package com.tuling.system.service.impl;

import com.tuling.common.web.service.CrudBaseIService;
import com.tuling.common.web.service.CrudBaseServiceImpl;
import com.tuling.system.domain.dto.SysRolePermissionRelSaveDto;
import com.tuling.system.domain.entity.SysRolePermissionRel;
import com.tuling.system.domain.vo.SysRolePermissionRelVo;
import com.tuling.system.mapper.SysRolePermissionRelMapper;
import com.tuling.system.service.SysRolePermissionRelService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SysRolePermissionRelServiceImpl extends CrudBaseServiceImpl<SysRolePermissionRel, SysRolePermissionRelVo, SysRolePermissionRelSaveDto, SysRolePermissionRelMapper>implements SysRolePermissionRelService {


    @Override
    public List<Long> getPermissionIdListByUserId(Long userId) {
        return null;
    }
}
