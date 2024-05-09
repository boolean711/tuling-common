package com.tuling.system.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.tuling.common.core.param.BaseEntity;
import lombok.Data;

@Data
@TableName("sys_role")
public class SysRole extends BaseEntity {


  private String roleName;
  private String roleKey;
  private Integer roleSort;
  private String dataScope;
  private String status;
  private String remark;




}
