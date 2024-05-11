package com.tuling.system.domain.dto;


import com.tuling.common.core.param.BaseSaveDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class SysPermissionSaveDto extends BaseSaveDto {


    @NotBlank
    private String permissionName;
    private String describeInfo;
    @NotBlank
    private String permissionCode;


}
