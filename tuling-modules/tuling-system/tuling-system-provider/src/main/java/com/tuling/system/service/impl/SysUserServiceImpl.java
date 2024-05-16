package com.tuling.system.service.impl;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.tuling.common.core.constants.PermissionConstants;
import com.tuling.common.core.exception.ServiceException;
import com.tuling.common.satoken.utils.LoginHelper;
import com.tuling.common.utils.BeanListUtils;
import com.tuling.common.web.service.CrudBaseServiceImpl;
import com.tuling.system.constants.CodeRuleConstants;
import com.tuling.system.constants.CommonConstants;
import com.tuling.system.constants.RedisPrefixKey;
import com.tuling.system.domain.dto.SysUserSaveDto;
import com.tuling.system.domain.entity.SysUser;
import com.tuling.system.domain.entity.SysUserRoleRel;
import com.tuling.system.domain.vo.SysRoleVo;
import com.tuling.system.domain.vo.SysUserVo;
import com.tuling.system.mapper.SysUserMapper;
import com.tuling.system.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class SysUserServiceImpl
        extends CrudBaseServiceImpl<SysUser, SysUserVo, SysUserSaveDto, SysUserMapper>
        implements SysUserService {


    @Autowired
    private SysUserRoleRelService userRoleRelService;

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysCodeRuleService codeRuleService;

    @Autowired
    private SysPermissionService permissionService;
    @Autowired
    @Qualifier("jacksonRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public void beforeSave(SysUserSaveDto dto) {

        checkTenant(dto.getTenantId());
        checkRole(dto.getRoleIds());
        checkUserNamePassword(dto);
        if (dto.getId() == null) {
            dto.setCode(codeRuleService.generateCode(CodeRuleConstants.USER_CODE_PREFIX));
            dto.setPassword(BCrypt.hashpw(dto.getPassword()));
        }

        userRoleRelService.removeByUserId(dto.getId());


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
        //用户没有租户隔离 需要手动隔离

        records.removeIf(item -> {
            if (LoginHelper.isAdmin()) {
                return false;
            }
            Long currentTenantId = LoginHelper.getCurrentTenantId();
            return !Objects.equals(item.getTenantId(), currentTenantId);

        });
        syncInfo(records);


    }

    @Override
    public void afterGetInfoById(SysUserVo vo) {
        syncInfo(Collections.singletonList(vo));
    }

    @Override
    public void beforeRemove(List<SysUser> entityList) {
        for (SysUser user : entityList) {
            checkTenant(user.getTenantId());
            checkRemoveUser(user.getId());
        }
    }

    @Override
    public void afterRemove(List<SysUser> entityList) {
        for (SysUser user : entityList) {
            userRoleRelService.removeByUserId(user.getId());
        }

    }

    @Override
    public List<SysUserVo> getUserByUsername(String username, Long tenantId) {
        if (StrUtil.isNotBlank(username)) {
            LambdaQueryWrapper<SysUser> lqw = new LambdaQueryWrapper<>();

            lqw.eq(SysUser::getUsername, username);

            if (tenantId != null) {
                lqw.eq(SysUser::getTenantId, tenantId);
            }
            List<SysUser> list = this.list(lqw);

            if (CollectionUtils.isNotEmpty(list)) {
                return BeanListUtils.copyList(list, SysUserVo.class);

            }
        }
        return Collections.emptyList();
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

    @Override
    public boolean resetPassword(Long id) {
        SysUser sysUser = this.getById(id);

        checkTenant(sysUser.getTenantId());

        sysUser.setPassword(BCrypt.hashpw(CommonConstants.DEFAULT_PASSWORD));

        this.updateById(sysUser);

        StpUtil.logout(sysUser.getId());

        redisTemplate.delete(String.format(RedisPrefixKey.INCORRECT_LOGIN_COUNT_PREFIX, sysUser.getUsername(), sysUser.getTenantId()));

        return true;


    }

    private void checkRemoveUser(Long id) {
        List<SysRoleVo> roleListByUserId = roleService.getRoleListByUserId(id);
        boolean isAdmin = LoginHelper.isAdmin();


        for (SysRoleVo roleVo : roleListByUserId) {
            if (!isAdmin) {
                //非超级管理员不能删除超级管理员和租户管理员
                if (permissionService.isGivenPermissionByRoleId(roleVo.getId(), Arrays.asList(PermissionConstants.ADMIN, PermissionConstants.TENANT_ADMIN))) {
                    throw new ServiceException("无法删除管理员用户");
                }
            } else {
                //超级管理员不能删除超级管理员
                if (permissionService.isGivenPermissionByRoleId(roleVo.getId(), Collections.singletonList(PermissionConstants.ADMIN))) {
                    throw new ServiceException("无法删除超级管理员用户");
                }
            }

        }
    }


    private void checkRole(List<Long> roleIds) {

        if (!LoginHelper.isAdmin()) {
            for (Long roleId : roleIds) {
                if (permissionService.isGivenPermissionByRoleId(roleId, Collections.singletonList(PermissionConstants.ADMIN))) {
                    throw new ServiceException("异常角色绑定");
                }
            }
        }


    }

    private void checkTenant(Long tenantId) {
        if (!LoginHelper.isAdmin()) {
            Long currentTenantId = LoginHelper.getCurrentTenantId();

            if (!Objects.equals(tenantId, currentTenantId)) {
                throw new ServiceException("数据异常，请联系管理员");
            }
        }
    }

    private void checkUserNamePassword(SysUserSaveDto dto) {

        if (dto.getId() == null) {
            if (StrUtil.isBlank(dto.getUsername()) || StrUtil.isBlank(dto.getPassword())) {
                throw new ServiceException("账号密码为空");
            }
        } else {
            if (StrUtil.isNotBlank(dto.getUsername()) || StrUtil.isNotBlank(dto.getPassword())) {
                throw new ServiceException("不允许修改账号密码");
            }
        }

        LambdaQueryWrapper<SysUser> lqw = new LambdaQueryWrapper<>();

        lqw.eq(SysUser::getUsername, dto.getUsername());
        lqw.eq(SysUser::getTenantId, dto.getTenantId());

        if (this.count(lqw) > 0) {
            throw new ServiceException("该账号已存在");
        }

    }

    private void syncInfo(List<SysUserVo> records) {
        List<Long> userIds = records.stream().map(SysUserVo::getId).collect(Collectors.toList());

        Map<Long, List<Long>> userRoleIdMap = userRoleRelService.getUserRoleIdMap(userIds);

        Map<Long, SysRoleVo> idVoMap = roleService.getIdVoMap(null);

        for (SysUserVo record : records) {

            List<SysRoleVo> roleVoList = new ArrayList<>();

            record.setPassword(null);
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
