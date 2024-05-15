package com.tuling.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.tuling.common.web.service.CrudBaseIService;
import com.tuling.common.web.service.CrudBaseServiceImpl;
import com.tuling.system.domain.dto.SysRolePermissionRelSaveDto;
import com.tuling.system.domain.entity.SysRolePermissionRel;
import com.tuling.system.domain.vo.SysRolePermissionRelVo;
import com.tuling.system.mapper.SysRolePermissionRelMapper;
import com.tuling.system.service.SysRolePermissionRelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class SysRolePermissionRelServiceImpl extends CrudBaseServiceImpl<SysRolePermissionRel, SysRolePermissionRelVo, SysRolePermissionRelSaveDto, SysRolePermissionRelMapper> implements SysRolePermissionRelService {


    @Override
    public Map<Long, Long> getRolePermissionIdMap(List<Long> roleId) {

        LambdaQueryWrapper<SysRolePermissionRel> lqw = new LambdaQueryWrapper<>();

        lqw.in(SysRolePermissionRel::getRoleId, roleId);

        List<SysRolePermissionRel> list = this.list(lqw);


        if (CollectionUtils.isNotEmpty(list)) {
            return list.stream().collect(Collectors.toMap(SysRolePermissionRel::getRoleId, SysRolePermissionRel::getPermissionId));
        }
        return Collections.emptyMap();
    }

    @Override
    public Map<Long, Long> getPermissionRoleIdMap(List<Long> permissionIds) {
        LambdaQueryWrapper<SysRolePermissionRel> lqw = new LambdaQueryWrapper<>();

        lqw.in(SysRolePermissionRel::getPermissionId, permissionIds);

        List<SysRolePermissionRel> list = this.list(lqw);


        if (CollectionUtils.isNotEmpty(list)) {
            return list.stream().collect(Collectors.toMap(SysRolePermissionRel::getPermissionId, SysRolePermissionRel::getRoleId));
        }
        return Collections.emptyMap();

    }

    @Override
    @Transactional
    public void removeByRoleId(Long roleId) {

        LambdaQueryWrapper<SysRolePermissionRel> lqw = new LambdaQueryWrapper<>();

        lqw.eq(SysRolePermissionRel::getRoleId, roleId);

        this.remove(lqw);
    }
}
