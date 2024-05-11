package com.tuling.system.service;


import com.baomidou.mybatisplus.annotation.TableName;
import com.tuling.common.core.param.BaseEntity;
import com.tuling.common.web.service.CrudBaseIService;
import com.tuling.system.domain.dto.SysMenuSaveDto;
import com.tuling.system.domain.entity.SysMenu;
import com.tuling.system.domain.vo.SysMenuVo;
import com.tuling.system.domain.vo.TreeMenuVo;
import lombok.Data;

import java.util.List;


public interface SysMenuService extends CrudBaseIService<SysMenu, SysMenuVo, SysMenuSaveDto> {


    List<Long> selectMenuCheckIdList();

    List<TreeMenuVo> treeMenuSelect();
}
