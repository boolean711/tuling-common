package com.tuling.common.sms.service;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.tuling.common.core.exception.ServiceException;
import com.tuling.common.utils.SpringUtils;
import com.tuling.common.utils.ValidatorUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public interface SmsService {

    int SEND_MAX_TIMES_ONE_DAY = 5;

    String PHONE_NUM_TIMES_REDIS_PREFIX = "phoneNumTimes:%s";


    String send(String[] phoneNumSet, String[] templateParamSet, String templateId);

    default String sendPhoneNumCode(String codeKey, String phoneNum, String code, String templateId, long timeoutMinutes) {
        if (!ValidatorUtils.isValidChinesePhoneNumber(phoneNum)) {
            throw new ServiceException("手机号码格式不正确");

        }
        RedisTemplate<String, Object> redisTemplate = SpringUtils.getBean("jacksonRedisTemplate");

        if (Boolean.TRUE.equals(redisTemplate.hasKey(codeKey))) {
            throw new ServiceException("验证码未到期,请稍后重试");
        }
        String timesKeys = String.format(PHONE_NUM_TIMES_REDIS_PREFIX, phoneNum);
        // Check how many times the code has been sent today
        Integer sendCount = (Integer) redisTemplate.opsForValue().get(timesKeys);
        if (sendCount == null) {
            sendCount = 0;
        }

        if (sendCount >= SEND_MAX_TIMES_ONE_DAY) {
            throw new ServiceException(String.format("手机号码：%s当天发送次数过多", phoneNum));
        }

        // Send the SMS code
        String result = this.send(new String[]{phoneNum}, new String[]{code}, templateId);

        // Increment the count in Redis
        sendCount++;
        Date now = DateUtil.date();
        Date endOfDay = DateUtil.endOfDay(now);
        long secondsUntilEndOfDay = DateUtil.between(now, endOfDay, DateUnit.SECOND);

        redisTemplate.opsForValue().set(timesKeys, sendCount, secondsUntilEndOfDay, TimeUnit.SECONDS);

        redisTemplate.opsForValue().set(codeKey, code, timeoutMinutes, TimeUnit.MINUTES);

        return result;
    }


    default String sendPhoneNumCode(String codeKey, String phoneNum, String code, String templateId) {

        return sendPhoneNumCode(codeKey, phoneNum, code, templateId, 1);
    }


    default boolean checkPhoneNumCode(String codeKey, String code) {
        RedisTemplate<String, Object> redisTemplate = SpringUtils.getBean("jacksonRedisTemplate");

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
