package com.tuling.common.sms.service;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.tuling.common.core.exception.ServiceException;
import com.tuling.common.utils.SpringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public interface SmsService  {

     int SEND_MAX_TIMES_ONE_DAY=5;

     String PHONE_NUM_TIMES_REDIS_PREFIX="phoneNumTimes:%s";

     String PHONE_NUM_CODE_REDIS_PREFIX="phoneNumCode:%s";

    String send(String[] phoneNumSet,String[] templateParamSet,String templateId );


   default String sendPhoneNumCode(String phoneNum,String code,String templateId ){
       RedisTemplate<String, Object> redisTemplate = SpringUtils.getBean("jacksonRedisTemplate");

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
       String result = this.send(new String[]{phoneNum}, new String[]{code}, templateId);

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


   default boolean checkPhoneNumCode(String phoneNum,String code){
       RedisTemplate<String, Object> redisTemplate = SpringUtils.getBean("jacksonRedisTemplate");
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
