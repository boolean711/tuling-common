package com.tuling.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;

import com.tuling.common.core.exception.ServiceException;
import com.tuling.common.web.service.CrudBaseServiceImpl;
import com.tuling.system.constants.CommonConstants;
import com.tuling.system.domain.dto.SysTenantSaveDto;
import com.tuling.system.domain.entity.SysTenant;
import com.tuling.system.domain.entity.SysUser;
import com.tuling.system.domain.vo.SysTenantVo;
import com.tuling.system.mapper.SysTenantMapper;
import com.tuling.system.service.SysTenantService;
import com.tuling.system.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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
    @Qualifier("jacksonRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    @Override
    public void afterSave(SysTenantSaveDto dto, SysTenant entity) {
        initLoginUser(dto, entity);

//        if (dto.getRenewMonthNum() != null && dto.getRenewMonthNum() > 0) {
//            insertRecord(dto, entity.getId(), TypeConstants.RECORD_TYPE_RENEW, "月");
//        }
//        if (dto.getTextMessageQty() != null && dto.getTextMessageQty() > 0) {
//            insertRecord(dto, entity.getId(), TypeConstants.RECORD_TYPE_MESSAGE, "条");
//        }
//        if (dto.getId() == null) {
//            applicationEventPublisher.publishEvent(new AfterSaveTenantEvent(this, entity.getId()));
//        }

    }


    @Override
    public void beforeSave(SysTenantSaveDto dto) {

        LambdaQueryWrapper<SysTenant> lqw = new LambdaQueryWrapper<>();

        lqw.eq(SysTenant::getPhoneNum, dto.getPhoneNum());
        if (dto.getId() != null) {
            lqw.ne(SysTenant::getId, dto.getId());
        }
        if (this.count(lqw) > 0) {
            throw new ServiceException("该手机号码已绑定租户");
        }
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

    private void initLoginUser(SysTenantSaveDto dto, SysTenant entity) {
        SysUser sysUser = null;

        if (dto.getId() == null) {
            sysUser = new SysUser();
            sysUser.setPassword(BCrypt.hashpw(CommonConstants.DEFAULT_PASSWORD));
        } else {
            LambdaQueryWrapper<SysUser> lqw = new LambdaQueryWrapper<>();

            lqw.eq(SysUser::getTenantId, entity.getId());
            List<SysUser> list = userService.list(lqw);

            if (CollectionUtils.isNotEmpty(list)) {
                sysUser = list.get(0);
            } else {
                sysUser = new SysUser();
                sysUser.setPassword(BCrypt.hashpw(CommonConstants.DEFAULT_PASSWORD));
            }

        }

        sysUser.setName(entity.getName());
        sysUser.setPhoneNum(entity.getPhoneNum());
        //暂停为手机号码
        sysUser.setUsername(entity.getPhoneNum());


        /**
         *   user查询时不拼租户,新增时手动设置租户信息
         */

        sysUser.setTenantId(entity.getId());
        userService.saveOrUpdate(sysUser);
        if (dto.getRenewMonthNum()>0){
            StpUtil.logout(sysUser.getId());
        }

    }


    @Override
    @Transactional
    public boolean resetPassword(Long tenantId) {
//        if (tenantId == null) {
//            throw new ServiceException("租户信息为空");
//        }
//
//        LambdaQueryWrapper<SysUser> lqw = new LambdaQueryWrapper<>();
//
//        lqw.eq(SysUser::getTenantId, tenantId);
//
//        List<SysUser> list = userService.list(lqw);
//
//        if (CollectionUtils.isNotEmpty(list)) {
//
//            SysUser sysUser = list.get(0);
//
//            sysUser.setPassword(BCrypt.hashpw(CommonConstants.DEFAULT_PASSWORD));
//
//            userService.updateById(sysUser);
//
//            StpUtil.logout(sysUser.getId());
//            redisTemplate.delete(RedisCommonPrefixKey.INCORRECT_LOGIN_COUNT_PREFIX + sysUser.getPhoneNum());
//
//            return true;
//
//        }
//

        throw new ServiceException("该租户未找到登录用户数据");
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

        if (qty>0){
            tenant.setTextMessageQty(tenant.getTextMessageQty() - qty);
            this.updateById(tenant);

        }


    }


}
