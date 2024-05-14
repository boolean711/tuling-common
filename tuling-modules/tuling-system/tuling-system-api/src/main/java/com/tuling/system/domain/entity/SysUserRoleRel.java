package com.tuling.system.domain.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tuling.common.core.param.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_user_role_rel")
public class SysUserRoleRel extends BaseEntity {


  private Long userId;
  private Long roleId;

  @TableField(exist = false)
  private Long updateId;
  @TableField(exist = false)
  private String updateName;
  @TableField(exist = false)
  private Date updateTime;
  @TableField(exist = false)
  private Boolean deleteFlag;
  @TableField(exist = false)
  private Long tenantId;
}
