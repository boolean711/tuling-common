package com.tuling.system.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.tuling.common.core.param.BaseEntity;
import lombok.Data;

@Data
@TableName("sys_role")
public class SysRole extends BaseEntity {


  private String roleName;
  private Integer roleSort;

  private Boolean status;
  private String remark;
  private Boolean menuCheckStrictly;



}
