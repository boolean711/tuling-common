package com.tuling.system.service.impl;


import com.tuling.common.web.service.CrudBaseIService;
import com.tuling.common.web.service.CrudBaseServiceImpl;
import com.tuling.system.domain.dto.SysMenuSaveDto;
import com.tuling.system.domain.entity.SysMenu;
import com.tuling.system.domain.vo.SysMenuVo;
import com.tuling.system.mapper.SysMenuMapper;
import com.tuling.system.service.SysMenuService;
import org.springframework.stereotype.Service;


@Service
public class SysMenuServiceImpl extends CrudBaseServiceImpl<SysMenu, SysMenuVo, SysMenuSaveDto, SysMenuMapper> implements SysMenuService {




}
