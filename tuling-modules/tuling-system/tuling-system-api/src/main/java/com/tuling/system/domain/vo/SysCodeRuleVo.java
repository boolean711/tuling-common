package com.tuling.system.domain.vo;

import com.tuling.common.core.param.BaseVo;
import lombok.Data;

@Data

public class SysCodeRuleVo extends BaseVo {

  private String rulesCode;
  private String prefix;
  private String itemName;
  private Integer initValue;
  private Integer currentValue;
  private Integer itemLength;

}
