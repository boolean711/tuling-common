package com.tuling.system.domain.vo;


import com.tuling.common.core.param.BaseVo;
import lombok.Data;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.util.Date;

@Data
public class SysRoleVo extends BaseVo {


  private String roleName;
  private Long permissionId;
  private String permissionName;
  private Integer roleSort;
  private Boolean status;
  private String remark;

  private Date createTime;
  private Boolean menuCheckStrictly;
}
