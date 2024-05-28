package com.tuling.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.tuling.common.web.service.CrudBaseIService;
import com.tuling.common.web.service.CrudBaseServiceImpl;
import com.tuling.system.domain.dto.SysRoleMenuRelSaveDto;
import com.tuling.system.domain.entity.SysRoleMenuRel;
import com.tuling.system.domain.entity.SysRolePermissionRel;
import com.tuling.system.domain.vo.SysRoleMenuRelVo;
import com.tuling.system.mapper.SysRoleMenuRelMapper;
import com.tuling.system.service.SysRoleMenuRelService;
import kotlin.jvm.internal.Lambda;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class SysRoleMenuRelServiceImpl extends CrudBaseServiceImpl<SysRoleMenuRel, SysRoleMenuRelVo, SysRoleMenuRelSaveDto, SysRoleMenuRelMapper> implements SysRoleMenuRelService {


    @Override
    public List<Long> getMenuIdListByRoleIds(List<Long> roleIds) {


        if (CollectionUtils.isNotEmpty(roleIds)) {
            LambdaQueryWrapper<SysRoleMenuRel> lqw = new LambdaQueryWrapper<>();
            lqw.in(SysRoleMenuRel::getRoleId, roleIds);

            List<SysRoleMenuRel> list = this.list(lqw);

            if (CollectionUtils.isNotEmpty(list)) {
                return list.stream().map(SysRoleMenuRel::getMenuId).collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    @Override
    @Transactional
    public void removeByRoleId(List<Long> roleIds) {

        LambdaQueryWrapper<SysRoleMenuRel> lqw = new LambdaQueryWrapper<>();

        lqw.in(SysRoleMenuRel::getRoleId, roleIds);

        this.remove(lqw);
    }
}
