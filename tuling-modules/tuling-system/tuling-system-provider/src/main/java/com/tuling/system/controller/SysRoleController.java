package com.tuling.system.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tuling.common.core.constants.PermissionConstants;
import com.tuling.common.core.param.ApiResponse;
import com.tuling.common.mybatis.param.ExpressionQueryDto;
import com.tuling.common.web.controller.CrudBaseController;
import com.tuling.common.web.service.CrudBaseServiceImpl;
import com.tuling.system.domain.dto.SysRoleSaveDto;
import com.tuling.system.domain.entity.SysRole;
import com.tuling.system.domain.vo.SysRoleVo;
import com.tuling.system.mapper.SysRoleMapper;
import com.tuling.system.service.SysRoleService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/system/role")
@SaCheckPermission(value = {PermissionConstants.ADMIN, PermissionConstants.TENANT_ADMIN}, mode = SaMode.OR)
public class SysRoleController extends CrudBaseController<SysRoleService, SysRole, SysRoleVo, SysRoleSaveDto> {


}
