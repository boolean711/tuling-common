package com.tuling.system.domain.dto;


import com.tuling.common.core.param.BaseSaveDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;


@Data
public class SysTenantSaveDto extends BaseSaveDto {


    @NotBlank(message = "租户名称不能为空")
    @Size(min = 1, max = 6, message = "租户名称长度必须在1到6个字符之间")
    private String name;
    @NotBlank(message = "手机号码不能为空")
    private String phoneNum;
    private String tenantCode;
    @NotBlank(message = "地址不能为空")
    private String address;
    private String iconUrl;
    private Integer orderNum;
    private Date validTime;

    private Long packageId;

    private Integer renewMonthNum;
    private Integer textMessageQty;
}
