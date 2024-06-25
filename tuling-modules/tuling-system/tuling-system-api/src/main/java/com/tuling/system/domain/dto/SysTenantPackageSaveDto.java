package com.tuling.system.domain.dto;


import com.tuling.common.core.param.BaseEntity;
import com.tuling.common.core.param.BaseSaveDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SysTenantPackageSaveDto extends BaseSaveDto {
  @NotBlank
  private String packageName;
  private String menuIds;
  private String remark;
  private Boolean menuCheckStrictly;
  private Boolean status;
  @NotBlank
  private  String code;

}
