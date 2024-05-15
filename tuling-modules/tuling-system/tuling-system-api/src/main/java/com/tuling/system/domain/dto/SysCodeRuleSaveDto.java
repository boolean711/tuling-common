package com.tuling.system.domain.dto;


import com.tuling.common.core.param.BaseSaveDto;
import lombok.Data;

@Data

public class SysCodeRuleSaveDto extends BaseSaveDto {

  private String rulesCode;
  private String prefix;
  private String itemName;
  private Integer initValue;
  private Integer currentValue;
  private Integer itemLength;

}
