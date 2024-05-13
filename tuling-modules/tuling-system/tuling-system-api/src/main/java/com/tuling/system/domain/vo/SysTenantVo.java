package com.tuling.system.domain.vo;

import com.tuling.common.core.param.BaseVo;
import lombok.Data;

import java.util.Date;

@Data
public class SysTenantVo extends BaseVo {

    private String name;
    private String phoneNum;
    private String tenantCode;
    private String address;
    private String iconUrl;
    private Integer orderNum;
    private Date validTime;

    private Long packageId;
    private Integer textMessageQty;
}
