package com.tuling.system.domain.dto;


import com.tuling.common.core.param.BaseSaveDto;
import lombok.Data;

@Data
public class SysRoleSaveDto extends BaseSaveDto {


  private String roleName;
  private String roleKey;
  private Integer roleSort;
  private String dataScope;
  private String status;
  private String remark;




}
