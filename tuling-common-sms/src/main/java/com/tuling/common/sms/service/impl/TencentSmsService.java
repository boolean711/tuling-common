package com.tuling.common.sms.service.impl;


import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.tuling.common.core.exception.ServiceException;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class TencentSmsService implements SmsService {


    @Autowired
    private TencentCloudProperties tencentCloudProperties;


    @Autowired
    @Qualifier("jacksonRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    private static  final int SEND_MAX_TIMES_ONE_DAY=5;

    private static  String PHONE_NUM_TIMES_REDIS_PREFIX="phoneNumTimes:%s";

    private static  String PHONE_NUM_CODE_REDIS_PREFIX="phoneNumCode:%s";

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

    @Override
    public String sendPhoneNumCode(String phoneNum, String code, String templateId) {
        // Redis key for the phone number
        String redisKey = String.format(PHONE_NUM_TIMES_REDIS_PREFIX, phoneNum);

        // Check how many times the code has been sent today
        Integer sendCount = (Integer) redisTemplate.opsForValue().get(redisKey);
        if (sendCount == null) {
            sendCount = 0;
        }

        if (sendCount >= SEND_MAX_TIMES_ONE_DAY) {
            throw new ServiceException("当天发送次数过多");
        }

        // Send the SMS code
        String result = this.send(new String[]{phoneNum}, new String[]{code,"1"}, templateId);

        // Increment the count in Redis
        sendCount++;
        Date now = DateUtil.date();
        Date endOfDay = DateUtil.endOfDay(now);
        long secondsUntilEndOfDay = DateUtil.between(now, endOfDay, DateUnit.SECOND);
        redisTemplate.opsForValue().set(redisKey, sendCount, secondsUntilEndOfDay, TimeUnit.SECONDS);

        // Store the verification code with a 1-minute expiry
        String codeKey = String.format(PHONE_NUM_CODE_REDIS_PREFIX,phoneNum);
        redisTemplate.opsForValue().set(codeKey, code, 1, TimeUnit.MINUTES);

        return result;
    }

    @Override
    public boolean checkPhoneNumCode(String phoneNum, String code) {
        // Redis key for the verification code
        String codeKey = String.format(PHONE_NUM_CODE_REDIS_PREFIX, phoneNum);

        // Get the stored code from Redis
        String storedCode = (String) redisTemplate.opsForValue().get(codeKey);

        // Verify the code
        if (storedCode != null && storedCode.equals(code)) {
            // If the code matches, delete it from Redis to prevent reuse
            redisTemplate.delete(codeKey);
            return true;
        }

        // If the code does not match or is not found, return false
        return false;
    }



}
