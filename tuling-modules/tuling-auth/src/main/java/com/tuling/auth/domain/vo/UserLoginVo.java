package com.tuling.auth.domain.vo;

import com.aliyun.captcha20230305.models.VerifyIntelligentCaptchaResponseBody;
import lombok.Data;

@Data
public class UserLoginVo {


    private String token;

    private VerifyIntelligentCaptchaResponseBody  captchaResponseBody;
}
