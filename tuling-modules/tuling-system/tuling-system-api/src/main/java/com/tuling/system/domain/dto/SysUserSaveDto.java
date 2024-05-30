package com.tuling.system.domain.dto;


import com.tuling.common.core.param.BaseSaveDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


@Data
public class SysUserSaveDto extends BaseSaveDto {


    private String code;
    @NotBlank
    private String nickName;


    private String password;
    @NotBlank
    private String phoneNum;
    private String avatarUrl;


    @NotBlank
    private String username;
    private String status;


    private List<Long> roleIds;

    private String gender;

    private String remark;
    @NotNull
    private Long tenantId;

    //自身修改
    private boolean updateSelf=false;

    private String email;
}
