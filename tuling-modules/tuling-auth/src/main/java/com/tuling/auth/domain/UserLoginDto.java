package com.tuling.auth.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserLoginDto {
    private String username;
    private String password;

    @NotNull
    private Long tenantId;

}
