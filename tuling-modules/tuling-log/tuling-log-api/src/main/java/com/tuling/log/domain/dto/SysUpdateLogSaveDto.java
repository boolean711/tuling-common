package com.tuling.log.domain.dto;


import com.baomidou.mybatisplus.annotation.TableName;
import com.tuling.common.core.param.BaseEntity;
import com.tuling.common.core.param.BaseSaveDto;
import lombok.Data;

@Data

public class SysUpdateLogSaveDto extends BaseSaveDto {


  private String content;
  private String version;
  private Integer orderNum;

}
