package com.tuling.system.domain.dto;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tuling.common.core.param.BaseEntity;
import com.tuling.common.core.param.BaseSaveDto;
import lombok.Data;

import java.util.Date;

@Data
public class SysUserRoleRelSaveDto extends BaseSaveDto {


  private Long userId;
  private Long roleId;


}
