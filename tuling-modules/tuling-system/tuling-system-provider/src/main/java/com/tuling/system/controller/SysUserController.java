package com.tuling.system.controller;



import com.tuling.common.web.controller.CrudBaseController;
import com.tuling.system.domain.dto.SysUserSaveDto;
import com.tuling.system.domain.entity.SysUser;
import com.tuling.system.domain.vo.SysUserVo;
import com.tuling.system.service.SysUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




@RestController
@RequestMapping("/system/user")
public class SysUserController extends CrudBaseController<SysUserService, SysUser, SysUserVo, SysUserSaveDto> {


}
