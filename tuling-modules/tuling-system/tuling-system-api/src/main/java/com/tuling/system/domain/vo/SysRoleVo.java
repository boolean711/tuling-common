package com.tuling.system.domain.vo;


import com.tuling.common.core.param.BaseVo;
import lombok.Data;

import java.util.Date;

@Data
public class SysRoleVo extends BaseVo {


  private String roleName;
  private String roleKey;
  private Integer roleSort;
  private String dataScope;
  private String status;
  private String remark;

  private Date createTime;

}
