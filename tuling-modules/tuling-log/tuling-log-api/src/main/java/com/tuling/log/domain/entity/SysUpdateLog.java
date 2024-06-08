package com.tuling.log.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.tuling.common.core.param.BaseEntity;
import lombok.Data;

@Data
@TableName("sys_update_log")
public class SysUpdateLog extends BaseEntity {


  private String content;
  private String version;
  private Integer orderNum;

}
