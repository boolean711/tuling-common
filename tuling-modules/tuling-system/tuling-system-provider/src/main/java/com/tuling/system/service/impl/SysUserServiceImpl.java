package com.tuling.system.service.impl;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;

import com.tuling.common.web.service.CrudBaseServiceImpl;
import com.tuling.system.domain.dto.SysUserSaveDto;
import com.tuling.system.domain.entity.SysUser;
import com.tuling.system.domain.vo.SysUserVo;
import com.tuling.system.mapper.SysUserMapper;
import com.tuling.system.service.SysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SysUserServiceImpl
        extends CrudBaseServiceImpl<SysUser, SysUserVo, SysUserSaveDto, SysUserMapper>
        implements SysUserService {


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
}
