package com.tuling.system.mapper;


import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.tuling.common.web.mapper.CrudBaseMapper;
import com.tuling.common.web.service.CrudBaseIService;
import com.tuling.system.domain.dto.SysRoleSaveDto;
import com.tuling.system.domain.entity.SysRole;
import com.tuling.system.domain.vo.SysRoleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface SysRoleMapper extends CrudBaseMapper<SysRole> {


    @InterceptorIgnore(tenantLine = "true")
    List<SysRoleVo> listByIdsIgnoreTenant(@Param(("roleIds")) List<Long> roleIds);

    /**
     * 根据租户套餐id查出对应租户管理员的角色id
     * @param packageId
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    List<Long> selectTenantAdminRoleIdByPackageId(@Param("packageId") Long packageId);

}
