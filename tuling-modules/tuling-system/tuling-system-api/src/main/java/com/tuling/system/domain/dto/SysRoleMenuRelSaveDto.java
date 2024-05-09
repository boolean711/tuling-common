package com.tuling.system.domain.dto;


import com.baomidou.mybatisplus.annotation.TableName;
import com.tuling.common.core.param.BaseEntity;
import com.tuling.common.core.param.BaseSaveDto;
import lombok.Data;

@Data

public class SysRoleMenuRelSaveDto extends BaseSaveDto {


  private Long roleId;
  private Long menuId;

}
