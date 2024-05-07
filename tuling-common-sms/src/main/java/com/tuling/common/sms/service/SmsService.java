package com.tuling.common.sms.service;

public interface SmsService  {


    String send(String[] phoneNumSet,String[] templateParamSet,String templateId );
}
