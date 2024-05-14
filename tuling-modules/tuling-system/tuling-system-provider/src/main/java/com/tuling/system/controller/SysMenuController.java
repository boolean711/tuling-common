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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
