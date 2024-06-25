package com.tuling.system.domain.vo;


import com.tuling.common.core.param.BaseSaveDto;
import com.tuling.common.core.param.BaseVo;
import lombok.Data;

@Data
public class SysTenantPackageVo extends BaseVo {

  private String packageName;
  private String menuIds;
  private String remark;
  private Boolean menuCheckStrictly;
  private Boolean status;
  private  String code;

}
