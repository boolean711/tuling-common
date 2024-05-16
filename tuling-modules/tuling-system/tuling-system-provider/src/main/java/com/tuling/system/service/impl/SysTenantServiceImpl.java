package com.tuling.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;

import com.tuling.common.core.exception.ServiceException;
import com.tuling.common.utils.BeanListUtils;
import com.tuling.common.web.service.CrudBaseServiceImpl;
import com.tuling.system.constants.CodeRuleConstants;
import com.tuling.system.constants.CommonConstants;
import com.tuling.system.constants.StatusConstants;
import com.tuling.system.domain.dto.SysRoleSaveDto;
import com.tuling.system.domain.dto.SysTenantSaveDto;
import com.tuling.system.domain.dto.SysUserSaveDto;
import com.tuling.system.domain.entity.SysTenant;
import com.tuling.system.domain.entity.SysTenantPackage;
import com.tuling.system.domain.entity.SysUser;
import com.tuling.system.domain.vo.SysPermissionVo;
import com.tuling.system.domain.vo.SysTenantVo;
import com.tuling.system.domain.vo.SysUserVo;
import com.tuling.system.event.AfterSaveTenantEvent;
import com.tuling.system.mapper.SysTenantMapper;
import com.tuling.system.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class SysTenantServiceImpl
        extends CrudBaseServiceImpl<SysTenant, SysTenantVo, SysTenantSaveDto, SysTenantMapper>
        implements SysTenantService {


    @Autowired
    private SysUserService userService;
    //    @Autowired
//    private SysTenantRenewRecordService tenantRenewRecordService;


    @Autowired
    private SysCodeRuleService codeRuleService;
    @Autowired
    private SysTenantPackageService tenantPackageService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public void beforeSave(SysTenantSaveDto dto) {

       if (dto.getId()==null|| StrUtil.isBlank(dto.getTenantCode())){
           dto.setTenantCode(codeRuleService.generateCode(CodeRuleConstants.TENANT_INFO_CODE_PREFIX));
       }
    }

    @Override
    public void afterSave(SysTenantSaveDto dto, SysTenant entity) {
        if (dto.getId() == null) {
            Long roleId = initTenantAdmin(entity);
            initLoginUser(roleId, entity);
            eventPublisher.publishEvent(new AfterSaveTenantEvent(this,entity.getId()));
        }


//        if (dto.getRenewMonthNum() != null && dto.getRenewMonthNum() > 0) {
//            insertRecord(dto, entity.getId(), TypeConstants.RECORD_TYPE_RENEW, "月");
//        }
//        if (dto.getTextMessageQty() != null && dto.getTextMessageQty() > 0) {
//            insertRecord(dto, entity.getId(), TypeConstants.RECORD_TYPE_MESSAGE, "条");
//        }


    }

    private Long initTenantAdmin(SysTenant entity) {


        SysTenantPackage tenantPackage = tenantPackageService.getById(entity.getPackageId());

        if (tenantPackage == null) {
            throw new ServiceException("未知租户套餐");
        }

        String menuIds = tenantPackage.getMenuIds();

        SysPermissionVo permissionByCode = permissionService.getPermissionByCode("system::tenant");

        SysRoleSaveDto roleSaveDto = new SysRoleSaveDto();

        roleSaveDto.setPermissionId(permissionByCode.getId());
        roleSaveDto.setRoleSort(1);
        roleSaveDto.setRoleName("租户管理员");
        roleSaveDto.setStatus(true);
        roleSaveDto.setMenuIdList(Arrays.stream(menuIds.split(",")).map(Long::parseLong).collect(Collectors.toList()));
        roleSaveDto.setTenantId(entity.getId());
        return roleService.saveOrUpdate(roleSaveDto);

    }


//    private void insertRecord(SysTenantSaveDto dto, Long id, Integer type, String unit) {
//
//
//        SysTenantCostRecord tenantRenewRecord = new SysTenantCostRecord();
//
//        tenantRenewRecord.setName(dto.getName());
//        tenantRenewRecord.setTenantCode(dto.getTenantCode());
//        tenantRenewRecord.setRenewUnit(unit);
//        if (type.equals(TypeConstants.RECORD_TYPE_RENEW)) {
//            tenantRenewRecord.setRenewNum(dto.getRenewMonthNum());
//        } else if (type.equals(TypeConstants.RECORD_TYPE_MESSAGE)) {
//            tenantRenewRecord.setRenewNum(dto.getTextMessageQty());
//        }
//
//        tenantRenewRecord.setPhoneNum(dto.getPhoneNum());
//        tenantRenewRecord.setType(type);
//        tenantRenewRecord.setTenantId(id);
//        tenantRenewRecordService.saveOrUpdate(tenantRenewRecord);
//
//
//    }

    private void initLoginUser(Long roleId, SysTenant entity) {
        SysUserSaveDto sysUser = new SysUserSaveDto();

        sysUser.setNickName(entity.getName() + "管理员");
        sysUser.setCode(codeRuleService.generateCode(CodeRuleConstants.USER_CODE_PREFIX));
        sysUser.setPassword(BCrypt.hashpw(CommonConstants.DEFAULT_PASSWORD));
        sysUser.setPhoneNum(entity.getPhoneNum());
        //暂为手机号码
        sysUser.setUsername(entity.getPhoneNum());
        sysUser.setGender("男");
        sysUser.setRemark("管理员角色账号，无法编辑和删除");
        /**
         *   user查询时不拼租户,新增时手动设置租户信息
         */
        sysUser.setTenantId(entity.getId());
        sysUser.setRoleIds(Collections.singletonList(roleId));
        sysUser.setStatus(StatusConstants.USER_ENABLE);

        userService.saveOrUpdate(sysUser);


    }


    @Override
    public List<SysTenantVo> getTenantByUserName(String userName) {

        List<SysUserVo> userByUsername = userService.getUserByUsername(userName,null);

        if (CollectionUtils.isNotEmpty(userByUsername)) {
            List<Long> tenantIds = userByUsername.stream().map(SysUserVo::getTenantId).collect(Collectors.toList());

            List<SysTenant> sysTenants = this.listByIds(tenantIds);

            List<SysTenantVo> tenantVoList = BeanListUtils.copyList(sysTenants, SysTenantVo.class);

            if (tenantIds.contains(-1L)) {
                SysTenantVo admin = new SysTenantVo();
                admin.setName("超级管理员");
                admin.setId(-1L);
                tenantVoList.add(admin);
            }

            return tenantVoList;


        }


        return Collections.emptyList();
    }


    @Override
    @Transactional
    public void deductionTextMessageQty(Integer qty, Long tenantId) {

        if (tenantId == null) {
            throw new ServiceException("未知租户数据");
        }
        SysTenant tenant = this.getById(tenantId);

        if (tenant == null) {
            throw new ServiceException("未知租户数据");
        }

        if (qty > 0) {
            tenant.setTextMessageQty(tenant.getTextMessageQty() - qty);
            this.updateById(tenant);

        }


    }


}
