package com.tuling.system.service.impl;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.tuling.common.web.service.CrudBaseServiceImpl;
import com.tuling.system.domain.dto.SysRoleSaveDto;
import com.tuling.system.domain.entity.SysRole;
import com.tuling.system.domain.entity.SysRoleMenuRel;
import com.tuling.system.domain.entity.SysRolePermissionRel;
import com.tuling.system.domain.vo.SysPermissionVo;
import com.tuling.system.domain.vo.SysRoleVo;
import com.tuling.system.mapper.SysRoleMapper;
import com.tuling.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class SysRoleServiceImpl extends CrudBaseServiceImpl<SysRole, SysRoleVo, SysRoleSaveDto, SysRoleMapper> implements SysRoleService {


    @Autowired
    private SysRolePermissionRelService rolePermissionRelService;

    @Autowired
    private SysPermissionService permissionService;


    @Autowired
    private SysRoleMenuRelService roleMenuRelService;


    @Autowired
    private SysUserRoleRelService userRoleRelService;

    @Override
    public void beforeSave(SysRoleSaveDto dto) {
        if (dto.getId() != null) {
            roleMenuRelService.removeByRoleId(dto.getId());
            rolePermissionRelService.removeByRoleId(dto.getId());
        }
    }

    @Override
    public void afterSave(SysRoleSaveDto dto, SysRole entity) {

        Long id = entity.getId();
        SysRolePermissionRel permissionRel = new SysRolePermissionRel();
        permissionRel.setRoleId(id);
        permissionRel.setPermissionId(dto.getPermissionId());

        List<SysRoleMenuRel> roleMenuRelList = new ArrayList<>();

        for (Long aLong : dto.getMenuIdList()) {
            SysRoleMenuRel sysRoleMenuRel = new SysRoleMenuRel();
            sysRoleMenuRel.setRoleId(id);
            sysRoleMenuRel.setMenuId(aLong);
            roleMenuRelList.add(sysRoleMenuRel);
        }
        rolePermissionRelService.save(permissionRel);

        roleMenuRelService.saveBatch(roleMenuRelList);


    }

    @Override
    public void afterPageListByExpression(List<SysRoleVo> records) {

        syncInfo(records);


    }


    @Override
    public void afterGetInfoById(SysRoleVo vo) {
        syncInfo(Collections.singletonList(vo));
    }


    private void syncInfo(List<SysRoleVo> records) {
        Map<Long, SysPermissionVo> idVoMap = permissionService.getIdVoMap(null);


        List<Long> roleIdList = records.stream().map(SysRoleVo::getId).collect(Collectors.toList());

        Map<Long, Long> rolePermissionIdMap = rolePermissionRelService.getRolePermissionIdMap(roleIdList);

        for (SysRoleVo record : records) {
            Long permissionId = rolePermissionIdMap.get(record.getId());

            SysPermissionVo sysPermissionVo = idVoMap.get(permissionId);

            if (sysPermissionVo != null) {
                record.setPermissionName(sysPermissionVo.getPermissionName());
                record.setPermissionId(permissionId);
            }


        }
    }

    @Override
    public List<String> getPermissionCodeByRoleIds(List<Long> roleIds) {


        Map<Long, Long> rolePermissionIdMap = rolePermissionRelService.getRolePermissionIdMap(roleIds);
        Map<Long, SysPermissionVo> idVoMap = permissionService.getIdVoMap(null);

        List<String> res=new ArrayList<>();

        for (Map.Entry<Long, Long> entry : rolePermissionIdMap.entrySet()) {
            SysPermissionVo sysPermissionVo = idVoMap.get(entry.getKey());
            if (sysPermissionVo!=null){
                res.add(sysPermissionVo.getPermissionCode());
            }
        }
        return res;
    }

    @Override
    public List<SysRoleVo> getRoleListByUserId(Long userId) {

        Map<Long, List<Long>> userRoleIdMap = userRoleRelService.getUserRoleIdMap(Collections.singletonList(userId));

        if (CollectionUtils.isNotEmpty(userRoleIdMap)){

            List<Long> roleIds = userRoleIdMap.get(userId);

            return this.baseMapper.listByIdsIgnoreTenant(roleIds);


        }
        return Collections.emptyList();
    }
}
