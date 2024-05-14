package com.tuling.system.domain.dto;


import com.tuling.common.core.param.BaseEntity;
import com.tuling.common.core.param.BaseSaveDto;
import lombok.Data;

@Data
public class SysTenantPackageSaveDto extends BaseSaveDto {

  private String packageName;
  private String menuIds;
  private String remark;
  private Boolean menuCheckStrictly;
  private Boolean status;


}
