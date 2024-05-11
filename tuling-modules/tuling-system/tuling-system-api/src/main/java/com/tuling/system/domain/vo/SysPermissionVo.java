package com.tuling.system.domain.vo;

import com.tuling.common.core.param.BaseVo;
import lombok.Data;

@Data
public class SysPermissionVo extends BaseVo {


    private String permissionName;
    private String describeInfo;

    private String permissionCode;
}
