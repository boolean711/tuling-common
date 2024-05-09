package com.tuling.system.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.tuling.common.core.param.BaseEntity;
import lombok.Data;

@Data
@TableName("sys_role_menu_rel")
public class SysRoleMenuRel extends BaseEntity {


  private Long roleId;
  private Long menuId;

}
