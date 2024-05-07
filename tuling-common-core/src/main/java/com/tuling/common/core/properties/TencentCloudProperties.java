package com.tuling.common.core.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "tencent")
@Data
public class TencentCloudProperties {


    private String secretKey;
    private String secretId;
    private String sdkAppId;
    private String signName;
    private Map<String, String> templateId;

}
