package com.tuling.system.service.impl;


import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;

import com.tuling.common.core.exception.ServiceException;
import com.tuling.common.web.service.CrudBaseServiceImpl;
import com.tuling.system.constants.CommonConstants;
import com.tuling.system.domain.dto.SysUserSaveDto;
import com.tuling.system.domain.entity.SysUser;
import com.tuling.system.domain.entity.SysUserRoleRel;
import com.tuling.system.domain.vo.SysRoleVo;
import com.tuling.system.domain.vo.SysUserVo;
import com.tuling.system.mapper.SysUserMapper;
import com.tuling.system.service.SysRoleService;
import com.tuling.system.service.SysUserRoleRelService;
import com.tuling.system.service.SysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class SysUserServiceImpl
        extends CrudBaseServiceImpl<SysUser, SysUserVo, SysUserSaveDto, SysUserMapper>
        implements SysUserService {


    @Autowired
    private SysUserRoleRelService userRoleRelService;

    @Autowired
    private SysRoleService roleService;


    @Override
    public void beforeSave(SysUserSaveDto dto) {
        if (dto.getId() != null) {

            dto.setUsername(null);
            dto.setPassword(null);
            if (StrUtil.isBlank(dto.getUsername())) {
                dto.setUsername(dto.getPhoneNum());
            }

            userRoleRelService.removeByUserId(dto.getId());
        }
    }

    @Override
    public void afterSave(SysUserSaveDto dto, SysUser entity) {


        List<SysUserRoleRel> relList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(dto.getRoleIds())) {
            for (Long roleId : dto.getRoleIds()) {
                SysUserRoleRel rel = new SysUserRoleRel();
                rel.setUserId(entity.getId());
                rel.setRoleId(roleId);
                relList.add(rel);
            }

            userRoleRelService.saveBatch(relList);
        }
    }

    @Override
    public void afterPageListByExpression(List<SysUserVo> records) {

        syncInfo(records);

    }

    @Override
    public void afterGetInfoById(SysUserVo vo) {
        syncInfo(Collections.singletonList(vo));
    }



    @Override
    public SysUserVo getUserByUsername(String username) {
        if (StrUtil.isNotBlank(username)) {
            LambdaQueryWrapper<SysUser> lqw = new LambdaQueryWrapper<>();

            lqw.eq(SysUser::getPhoneNum, username);

            List<SysUser> list = this.list(lqw);

            if (CollectionUtils.isNotEmpty(list)) {
                SysUserVo vo = new SysUserVo();
                BeanUtils.copyProperties(list.get(0), vo);
                return vo;
            }
        }
        return null;
    }

    @Override
    @Transactional
    public void changeUserStatus(Long userId, String status) {

        SysUser sysUser = this.getById(userId);


        if (sysUser == null) {
            throw new ServiceException("未知用户");
        }

        sysUser.setStatus(status);

        this.updateById(sysUser);


    }

    private void syncInfo(List<SysUserVo> records) {
        List<Long> userIds = records.stream().map(SysUserVo::getId).collect(Collectors.toList());

        Map<Long, List<Long>> userRoleIdMap = userRoleRelService.getUserRoleIdMap(userIds);

        Map<Long, SysRoleVo> idVoMap = roleService.getIdVoMap(null);

        for (SysUserVo record : records) {

            List<SysRoleVo> roleVoList = new ArrayList<>();

            Long id = record.getId();
            List<Long> roleIds = userRoleIdMap.get(id);
            if (CollectionUtils.isNotEmpty(roleIds)) {

                for (Long roleId : roleIds) {
                    SysRoleVo sysRoleVo = idVoMap.get(roleId);
                    if (sysRoleVo != null) {
                        roleVoList.add(sysRoleVo);
                    }
                }
                record.setRoleList(roleVoList);
            }

        }
    }
}
