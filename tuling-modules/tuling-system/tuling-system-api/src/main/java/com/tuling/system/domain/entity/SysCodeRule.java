package com.tuling.system.domain.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tuling.common.core.param.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data

@TableName("sys_code_rule")
public class SysCodeRule extends BaseEntity {

  private String rulesCode;
  private String prefix;
  private String itemName;
  private Integer initValue;
  private Integer currentValue;
  private Integer itemLength;
  @TableField(exist = false)
  private Long createId;
  @TableField(exist = false)
  private String createName;
  @TableField(exist = false)
  private Date createTime;
}
