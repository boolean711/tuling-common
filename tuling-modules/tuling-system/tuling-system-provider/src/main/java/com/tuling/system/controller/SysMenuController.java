package com.tuling.system.controller;


import com.tuling.common.core.param.ApiResponse;
import com.tuling.common.web.controller.CrudBaseController;
import com.tuling.common.web.service.CrudBaseServiceImpl;
import com.tuling.system.domain.dto.SysMenuSaveDto;
import com.tuling.system.domain.entity.SysMenu;
import com.tuling.system.domain.vo.SysMenuVo;
import com.tuling.system.domain.vo.TreeMenuVo;
import com.tuling.system.mapper.SysMenuMapper;
import com.tuling.system.service.SysMenuService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class SysMenuController extends CrudBaseController<SysMenuService,SysMenu, SysMenuVo, SysMenuSaveDto> {

//    @GetMapping("/treeSelect")
//    public ApiResponse<List<TreeMenuVo>> treeSelect(SysMenu menu)
//    {
//
//        return ApiResponse.success(menuService.buildMenuTreeSelect(menus));
//    }



}
