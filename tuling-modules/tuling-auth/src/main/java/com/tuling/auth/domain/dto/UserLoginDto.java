package com.tuling.auth.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserLoginDto {
    private String username;
    private String password;

    @NotNull
    private Long tenantId;



    @NotBlank
    private  String captchaVerifyParam;

    private String sceneId="1gmllrc6";
}
