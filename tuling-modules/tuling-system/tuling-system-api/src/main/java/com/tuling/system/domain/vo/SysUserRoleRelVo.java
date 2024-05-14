package com.tuling.system.domain.vo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tuling.common.core.param.BaseEntity;
import com.tuling.common.core.param.BaseVo;
import lombok.Data;

import java.util.Date;

@Data

public class SysUserRoleRelVo extends BaseVo {


  private Long userId;
  private Long roleId;


}
