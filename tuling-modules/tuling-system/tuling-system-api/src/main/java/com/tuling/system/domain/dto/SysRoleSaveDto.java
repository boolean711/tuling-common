package com.tuling.system.domain.dto;


import com.tuling.common.core.param.BaseSaveDto;
import lombok.Data;

import java.util.List;

@Data
public class SysRoleSaveDto extends BaseSaveDto {


    private String roleName;
    private Integer roleSort;
    private Boolean status;
    private String remark;

    private Boolean menuCheckStrictly;

    private Long permissionId;

    private List<Long> menuIdList;

    private Long tenantId;

}
