package com.tuling.system.domain.vo;


import com.tuling.common.core.param.BaseSaveDto;
import com.tuling.common.core.param.BaseVo;
import lombok.Data;


@Data
public class SysRolePermissionRelVo extends BaseVo {


  private Long roleId;
  private Long permissionId;





}
