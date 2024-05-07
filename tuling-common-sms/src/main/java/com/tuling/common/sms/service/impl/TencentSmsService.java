package com.tuling.common.sms.service.impl;


import cn.hutool.json.JSONUtil;
import com.tuling.common.core.properties.TencentCloudProperties;
import com.tuling.common.sms.service.SmsService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class TencentSmsService implements SmsService {


    @Autowired
    private TencentCloudProperties tencentCloudProperties;


    private SmsClient client;

    private SendSmsRequest req;


    @PostConstruct
    public void init() {
        Credential cred = new Credential(tencentCloudProperties.getSecretId(), tencentCloudProperties.getSecretKey());

        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("sms.tencentcloudapi.com");

        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setSignMethod("HmacSHA256");
        clientProfile.setHttpProfile(httpProfile);

        this.client = new SmsClient(cred, "ap-guangzhou", clientProfile);
        req = new SendSmsRequest();
        req.setSmsSdkAppId(tencentCloudProperties.getSdkAppId());
        req.setSignName(tencentCloudProperties.getSignName());

    }

    @Override
    public String send(String[] phoneNumSet, String[] templateParamSet, String templateId) {
        req.setPhoneNumberSet(phoneNumSet);
        req.setTemplateParamSet(templateParamSet);
        req.setTemplateId(templateId);
        try {

            SendSmsResponse sendSmsResponse = client.SendSms(req);

            return JSONUtil.toJsonStr(sendSmsResponse);
        } catch (TencentCloudSDKException e) {
            log.error("SendSms:", e);
            return e.getMessage();
        }
    }


}
