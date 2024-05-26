package com.tuling.common.sms.service;

public interface SmsService  {


    String send(String[] phoneNumSet,String[] templateParamSet,String templateId );


    String sendPhoneNumCode(String phoneNum,String code,String templateId );


    boolean checkPhoneNumCode(String phoneNum,String code);
}
