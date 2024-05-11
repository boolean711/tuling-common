package com.tuling.system.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.tuling.common.core.param.BaseEntity;
import lombok.Data;

@TableName("sys_role_permission_rel")
@Data
public class SysRolePermissionRel extends BaseEntity {


  private Long roleId;
  private Long permissionId;





}
