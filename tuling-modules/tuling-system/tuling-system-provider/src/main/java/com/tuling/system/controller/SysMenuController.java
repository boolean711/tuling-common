package com.tuling.system.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.tuling.common.core.constants.PermissionConstants;
import com.tuling.common.core.param.ApiResponse;
import com.tuling.common.web.controller.CrudBaseController;
import com.tuling.common.web.service.CrudBaseServiceImpl;
import com.tuling.system.constants.CommonConstants;
import com.tuling.system.domain.dto.SysMenuSaveDto;
import com.tuling.system.domain.entity.SysMenu;
import com.tuling.system.domain.vo.SysMenuVo;
import com.tuling.system.domain.vo.TreeMenuVo;
import com.tuling.system.mapper.SysMenuMapper;
import com.tuling.system.service.SysMenuService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system/menu")
public class SysMenuController extends CrudBaseController<SysMenuService, SysMenu, SysMenuVo, SysMenuSaveDto> {

    @GetMapping("/getRouters")
    public ApiResponse<List<SysMenuVo>> getRouters() {
        return ApiResponse.success(service.getRouters());
    }

    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("/treeSelect/{isFindChecked}")
    public ApiResponse<Map<String, Object>> treeSelect(@PathVariable boolean isFindChecked) {
        Map<String, Object> res = new HashMap<>();
        if (isFindChecked) {
            res.put("checkedKeys", service.selectMenuCheckIdList());
        }

        res.put("menus", service.treeMenuSelect());
        return ApiResponse.success(res);
    }

    @Override
    @SaCheckPermission(PermissionConstants.ADMIN)
    @PostMapping("/saveOrUpdate")
    public ApiResponse<Long> saveOrUpdate(@Validated @RequestBody SysMenuSaveDto dto) {
        return super.saveOrUpdate(dto);
    }

    @Override
    @SaCheckPermission(PermissionConstants.ADMIN)
    public ApiResponse<Boolean> removeByIds(List<Long> ids) {
        return super.removeByIds(ids);
    }
}
