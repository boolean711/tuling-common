package com.tuling.system.domain.dto;


import com.tuling.common.core.param.BaseSaveDto;
import lombok.Data;


@Data
public class SysRolePermissionRelSaveDto extends BaseSaveDto {


  private Long roleId;
  private Long permissionId;





}
