package com.tuling.system.domain.dto;


import com.tuling.common.core.param.BaseSaveDto;
import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class SysUserSaveDto extends BaseSaveDto {

    private String code;
    private String nickName;
    private String password;
    private String phoneNum;
    private String avatarUrl;
    private Date lastLoginTime;

    private String username;
    private String status;


    private List<Long> roleIds;
    private String gender;

    private String remark;

    private Long tenantId;

}
