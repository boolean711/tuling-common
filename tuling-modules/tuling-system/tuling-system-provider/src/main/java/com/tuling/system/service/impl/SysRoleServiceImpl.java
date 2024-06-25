package com.tuling.system.service.impl;


import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.tuling.common.core.constants.PermissionConstants;
import com.tuling.common.core.exception.ServiceException;
import com.tuling.common.satoken.utils.LoginHelper;
import com.tuling.common.web.service.CrudBaseServiceImpl;
import com.tuling.system.domain.dto.SysRoleSaveDto;
import com.tuling.system.domain.entity.SysRole;
import com.tuling.system.domain.entity.SysRoleMenuRel;
import com.tuling.system.domain.entity.SysRolePermissionRel;
import com.tuling.system.domain.entity.SysTenant;
import com.tuling.system.domain.vo.SysPermissionVo;
import com.tuling.system.domain.vo.SysRoleVo;
import com.tuling.system.domain.vo.SysTenantVo;
import com.tuling.system.mapper.SysRoleMapper;
import com.tuling.system.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class SysRoleServiceImpl extends CrudBaseServiceImpl<SysRole, SysRoleVo, SysRoleSaveDto, SysRoleMapper> implements SysRoleService {


    @Autowired
    private SysRolePermissionRelService rolePermissionRelService;

    @Autowired
    private SysPermissionService permissionService;


    @Autowired
    private SysRoleMenuRelService roleMenuRelService;


    @Autowired
    private SysUserRoleRelService userRoleRelService;


    @Autowired
    @Lazy
    private SysTenantService tenantService;

    @Override
    public void beforeSave(SysRoleSaveDto dto) {
        if (dto.getId() != null) {
            if (!LoginHelper.isAdmin()&&permissionService.isGivenPermissionByRoleId(dto.getId(), Arrays.asList(PermissionConstants.TENANT_ADMIN,PermissionConstants.ADMIN))) {
                throw new ServiceException("管理员角色无法修改");
            }
            roleMenuRelService.removeByRoleId(Collections.singletonList(dto.getId()));
            rolePermissionRelService.removeByRoleId(Collections.singletonList(dto.getId()));
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
        if (!LoginHelper.isAdmin()){
            records.removeIf(item->permissionService.isGivenPermissionByRoleId(item.getId(), Collections.singletonList(PermissionConstants.TENANT_ADMIN)));
        }
        syncInfo(records);
    }


    @Override
    public void afterGetInfoById(SysRoleVo vo) {
        syncInfo(Collections.singletonList(vo));
    }

    @Override
    public void beforeRemove(List<SysRole> entityList) {
        for (SysRole role : entityList) {
            if (permissionService.isGivenPermissionByRoleId(role.getId(), Arrays.asList(PermissionConstants.TENANT_ADMIN,PermissionConstants.ADMIN))) {
                throw new ServiceException("管理员角色无法删除");
            }

        }
    }

    @Override
    public void afterRemove(List<SysRole> entityList) {

        List<Long> collect = entityList.stream().map(SysRole::getId).collect(Collectors.toList());

        roleMenuRelService.removeByRoleId(collect);
        rolePermissionRelService.removeByRoleId(collect);


    }
    @Override
    public Long getRoleIdByPermissionCode(String permissionCode) {

        SysPermissionVo permissionByCode = permissionService.getPermissionByCode(permissionCode);

        if (permissionByCode!=null){
            Long permissionId = permissionByCode.getId();

            Map<Long, Long> permissionRoleIdMap = rolePermissionRelService.getPermissionRoleIdMap(Collections.singletonList(permissionId));

            return  permissionRoleIdMap.get(permissionId);

        }

        return null;
    }


    @Override
    public List<String> getPermissionCodeByRoleIds(List<Long> roleIds) {


        Map<Long, Long> rolePermissionIdMap = rolePermissionRelService.getRolePermissionIdMap(roleIds);
        Map<Long, SysPermissionVo> idVoMap = permissionService.getIdVoMap(null);

        List<String> res=new ArrayList<>();

        for (Map.Entry<Long, Long> entry : rolePermissionIdMap.entrySet()) {
            SysPermissionVo sysPermissionVo = idVoMap.get(entry.getValue());
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

    private void syncInfo(List<SysRoleVo> records) {
        if (CollectionUtils.isNotEmpty(records)){
            Map<Long, SysPermissionVo> idVoMap = permissionService.getIdVoMap(null);


            List<Long> roleIdList = records.stream().map(SysRoleVo::getId).collect(Collectors.toList());
            log.info("roleIdList：{}",roleIdList);

            Map<Long, Long> rolePermissionIdMap = rolePermissionRelService.getRolePermissionIdMap(roleIdList);


            Map<Long, SysTenantVo> tenantVoMap = tenantService.getIdVoMap(null);


            for (SysRoleVo record : records) {
                Long permissionId = rolePermissionIdMap.get(record.getId());

                SysPermissionVo sysPermissionVo = idVoMap.get(permissionId);

                if (sysPermissionVo != null) {
                    record.setPermissionName(sysPermissionVo.getPermissionName());
                    record.setPermissionId(permissionId);
                    record.setPermissionCode(sysPermissionVo.getPermissionCode());
                }
                SysTenantVo tenantVo = tenantVoMap.get(record.getTenantId());



                if (tenantVo!=null){
                    record.setTenantName(tenantVo.getName());
                }


            }
        }

    }
}
