package com.tuling.system.domain.vo;

import com.tuling.common.core.param.BaseVo;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SysUserVo extends BaseVo {

    private String nickName;
    private String password;
    private String phoneNum;
    private String avatarUrl;
    private Date lastLoginTime;
    private String code;

    private String username;
    private String lastLoginIp;
    private String status;


    private List<SysRoleVo> roleList;


    private List<String> permissionList;

    private SysTenantVo tenantVo;

    private String gender;
    private String email;

    private String version;

    private String remark;


    private Date createTime;

    private String createName;
    private Long tenantId;


}
