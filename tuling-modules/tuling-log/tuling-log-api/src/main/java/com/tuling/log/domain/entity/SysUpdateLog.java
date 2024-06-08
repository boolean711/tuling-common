package com.tuling.log.domain.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tuling.common.core.param.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@TableName("sys_update_log")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class SysUpdateLog extends BaseEntity {

  @EqualsAndHashCode.Include
  private String content;
  @EqualsAndHashCode.Include
  private String version;
  @EqualsAndHashCode.Include
  private Integer orderNum;
  @TableField(exist = false)
  private Long createId;
  @TableField(exist = false)
  private String createName;

  @TableField(exist = false)
  private Long updateId;
  @TableField(exist = false)
  private String updateName;
  @TableField(exist = false)
  private Date updateTime;
  @TableField(exist = false)
  private Long tenantId;
}
