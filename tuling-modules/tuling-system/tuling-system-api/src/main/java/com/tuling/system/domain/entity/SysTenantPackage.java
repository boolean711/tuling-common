package com.tuling.system.domain.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tuling.common.core.param.BaseEntity;
import lombok.Data;

@Data
@TableName("sys_tenant_package")
public class SysTenantPackage extends BaseEntity {

  private String packageName;
  private String menuIds;
  private String remark;
  private Boolean menuCheckStrictly;
  private Boolean status;
  @TableField(exist = false)
  private Long tenantId;

  private  String code;

}
