package com.tuling.common.sms.param.response;

import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import lombok.Data;

@Data
public class TencentSendStatusResponse implements BaseResponse {

   private SendSmsResponse sendSmsResponse;


}
