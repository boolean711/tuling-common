package com.tuling.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.tuling.common.web.service.CrudBaseIService;
import com.tuling.common.web.service.CrudBaseServiceImpl;
import com.tuling.system.domain.dto.SysUserRoleRelSaveDto;
import com.tuling.system.domain.entity.SysRolePermissionRel;
import com.tuling.system.domain.entity.SysUserRoleRel;
import com.tuling.system.domain.vo.SysUserRoleRelVo;
import com.tuling.system.mapper.SysUserRoleRelMapper;
import com.tuling.system.service.SysUserRoleRelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class SysUserRoleRelServiceImpl extends CrudBaseServiceImpl<SysUserRoleRel, SysUserRoleRelVo, SysUserRoleRelSaveDto, SysUserRoleRelMapper> implements SysUserRoleRelService {


    @Override
    public Map<Long, List<Long>> getUserRoleIdMap(List<Long> userIds) {
        LambdaQueryWrapper<SysUserRoleRel> lqw = new LambdaQueryWrapper<>();

        if (CollectionUtils.isNotEmpty(userIds)){
            lqw.in(SysUserRoleRel::getUserId, userIds);
        }


        List<SysUserRoleRel> list = this.list(lqw);


        if (CollectionUtils.isNotEmpty(list)) {

            return list.stream()
                    .collect(Collectors.groupingBy(SysUserRoleRel::getUserId, Collectors.mapping(SysUserRoleRel::getRoleId, Collectors.toList())));

        }
        return Collections.emptyMap();
    }

    @Override
    @Transactional
    public void removeByUserId(Long userId) {

        LambdaQueryWrapper<SysUserRoleRel> lqw = new LambdaQueryWrapper<>();

        lqw.eq(SysUserRoleRel::getUserId, userId);

        this.remove(lqw);
    }
}
