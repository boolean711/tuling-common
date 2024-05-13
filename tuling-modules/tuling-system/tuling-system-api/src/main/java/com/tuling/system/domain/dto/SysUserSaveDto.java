package com.tuling.system.domain.dto;


import com.tuling.common.core.param.BaseSaveDto;
import lombok.Data;

import java.util.Date;


@Data
public class SysUserSaveDto extends BaseSaveDto {


  private String name;
  private String password;
  private String phoneNum;
  private String avatarUrl;
  private Date lastLoginTime;

  private String username;


}
