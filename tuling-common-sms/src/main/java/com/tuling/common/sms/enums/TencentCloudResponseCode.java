package com.tuling.common.sms.enums;

public enum TencentCloudResponseCode {

    SUCCESS("send success","发送成功"),

    ACTIONOFFLINE("ActionOffline", "接口已下线。"),

    AUTHFAILURE_INVALIDAUTHORIZATION("AuthFailure.InvalidAuthorization", "请求头部的 Authorization 不符合腾讯云标准。"),

    AUTHFAILURE_INVALIDSECRETID("AuthFailure.InvalidSecretId", "密钥非法（不是云 API 密钥类型）。"),

    AUTHFAILURE_MFAFAILURE("AuthFailure.MFAFailure", "MFA 错误。"),

    AUTHFAILURE_SECRETIDNOTFOUND("AuthFailure.SecretIdNotFound", "密钥不存在。请在 控制台 检查密钥是否已被删除或者禁用，如状态正常，请检查密钥是否填写正确，注意前后不得有空格。"),

    AUTHFAILURE_SIGNATUREEXPIRE("AuthFailure.SignatureExpire", "签名过期。Timestamp 和服务器时间相差不得超过五分钟，请检查本地时间是否和标准时间同步。"),

    AUTHFAILURE_SIGNATUREFAILURE("AuthFailure.SignatureFailure", "签名错误。签名计算错误，请对照调用方式中的签名方法文档检查签名计算过程。"),

    AUTHFAILURE_TOKENFAILURE("AuthFailure.TokenFailure", "token 错误。"),

    AUTHFAILURE_UNAUTHORIZEDOPERATION("AuthFailure.UnauthorizedOperation", "请求未授权。请参考 CAM 文档对鉴权的说明。"),

    DRYRUNOPERATION("DryRunOperation", "DryRun 操作，代表请求将会是成功的，只是多传了 DryRun 参数。"),

    FAILEDOPERATION("FailedOperation", "操作失败。"),

    INTERNALERROR("InternalError", "内部错误。"),

    INVALIDACTION("InvalidAction", "接口不存在。"),

    INVALIDPARAMETER("InvalidParameter", "参数错误（包括参数格式、类型等错误）。"),

    INVALIDPARAMETERVALUE("InvalidParameterValue", "参数取值错误。"),

    INVALIDREQUEST("InvalidRequest", "请求 body 的 multipart 格式错误。"),

    IPINBLACKLIST("IpInBlacklist", "IP地址在黑名单中。"),

    IPNOTINWHITELIST("IpNotInWhitelist", "IP地址不在白名单中。"),

    LIMITEXCEEDED("LimitExceeded", "超过配额限制。"),

    MISSINGPARAMETER("MissingParameter", "缺少参数。"),

    NOSUCHPRODUCT("NoSuchProduct", "产品不存在"),

    NOSUCHVERSION("NoSuchVersion", "接口版本不存在。"),

    REQUESTLIMITEXCEEDED("RequestLimitExceeded", "请求的次数超过了频率限制。"),

    REQUESTLIMITEXCEEDED_GLOBALREGIONUINLIMITEXCEEDED("RequestLimitExceeded.GlobalRegionUinLimitExceeded", "主账号超过频率限制。"),

    REQUESTLIMITEXCEEDED_IPLIMITEXCEEDED("RequestLimitExceeded.IPLimitExceeded", "IP限频。"),

    REQUESTLIMITEXCEEDED_UINLIMITEXCEEDED("RequestLimitExceeded.UinLimitExceeded", "主账号限频。"),

    REQUESTSIZELIMITEXCEEDED("RequestSizeLimitExceeded", "请求包超过限制大小。"),

    RESOURCEINUSE("ResourceInUse", "资源被占用。"),

    RESOURCEINSUFFICIENT("ResourceInsufficient", "资源不足。"),

    RESOURCENOTFOUND("ResourceNotFound", "资源不存在。"),

    RESOURCEUNAVAILABLE("ResourceUnavailable", "资源不可用。"),

    RESPONSESIZELIMITEXCEEDED("ResponseSizeLimitExceeded", "返回包超过限制大小。"),

    SERVICEUNAVAILABLE("ServiceUnavailable", "当前服务暂时不可用。"),

    UNAUTHORIZEDOPERATION("UnauthorizedOperation", "未授权操作。"),

    UNKNOWNPARAMETER("UnknownParameter", "未知参数错误，用户多传未定义的参数会导致错误。"),

    UNSUPPORTEDOPERATION("UnsupportedOperation", "操作不支持。"),

    UNSUPPORTEDPROTOCOL("UnsupportedProtocol", "http(s) 请求协议错误，只支持 GET 和 POST 请求。"),

    UNSUPPORTEDREGION("UnsupportedRegion", "接口不支持所传地域。"),

    FAILEDOPERATION_CONTAINSENSITIVEWORD("FailedOperation.ContainSensitiveWord", "短信内容中含有敏感词，请联系 腾讯云短信小助手。"),

    FAILEDOPERATION_FAILRESOLVEPACKET("FailedOperation.FailResolvePacket", "请求包解析失败，通常情况下是由于没有遵守 API 接口说明规范导致的，请参考 请求包体解析1004错误详解。"),

    FAILEDOPERATION_FORBIDADDMARKETINGTEMPLATES("FailedOperation.ForbidAddMarketingTemplates", "个人用户不能申请营销短信。"),

    FAILEDOPERATION_INSUFFICIENTBALANCEINSMSPACKAGE("FailedOperation.InsufficientBalanceInSmsPackage", "套餐包余量不足，请 购买套餐包。"),

    FAILEDOPERATION_JSONPARSEFAIL("FailedOperation.JsonParseFail", "解析请求包体时候失败。"),

    FAILEDOPERATION_MARKETINGSENDTIMECONSTRAINT("FailedOperation.MarketingSendTimeConstraint", "营销短信发送时间限制，为避免骚扰用户，营销短信只允许在8点到22点发送。"),

    FAILEDOPERATION_MISSINGSIGNATURE("FailedOperation.MissingSignature", "没有申请签名之前，无法申请模板，请根据 创建签名 申请完成之后再次申请。"),

    FAILEDOPERATION_MISSINGSIGNATURELIST("FailedOperation.MissingSignatureList", "无法识别签名，请确认是否已有签名通过申请，一般是签名未通过申请，可以查看 签名审核。"),

    FAILEDOPERATION_MISSINGSIGNATURETOMODIFY("FailedOperation.MissingSignatureToModify", "此签名 ID 未提交申请或不存在，不能进行修改操作，请检查您的 SignId 是否填写正确。"),

    FAILEDOPERATION_MISSINGTEMPLATELIST("FailedOperation.MissingTemplateList", "无法识别模板，请确认是否已有模板通过申请，一般是模板未通过申请，可以查看 模板审核。"),

    FAILEDOPERATION_MISSINGTEMPLATETOMODIFY("FailedOperation.MissingTemplateToModify", "此模板 ID 未提交申请或不存在，不能进行修改操作，请检查您的 TemplateId是否填写正确。"),

    FAILEDOPERATION_NOTENTERPRISECERTIFICATION("FailedOperation.NotEnterpriseCertification", "非企业认证无法使用签名及模板相关接口，您可以 变更实名认证模式，变更为企业认证用户后，约1小时左右生效。"),

    FAILEDOPERATION_OTHERERROR("FailedOperation.OtherError", "其他错误，一般是由于参数携带不符合要求导致，请参考API接口说明，如有需要请联系 腾讯云短信小助手。"),

    FAILEDOPERATION_PARAMETERSOTHERERROR("FailedOperation.ParametersOtherError", "未知错误，如有需要请联系 腾讯云短信小助手。"),

    FAILEDOPERATION_PHONENUMBERINBLACKLIST("FailedOperation.PhoneNumberInBlacklist", "手机号在免打扰名单库中，通常是用户退订或者命中运营商免打扰名单导致的，可联系 腾讯云短信小助手 解决。"),

    FAILEDOPERATION_PHONENUMBERPARSEFAIL("FailedOperation.PhoneNumberParseFail", "号码解析失败，请检查号码是否符合 E.164 标准。"),

    FAILEDOPERATION_PROHIBITSUBACCOUNTUSE("FailedOperation.ProhibitSubAccountUse", "非主账号无法使用拉取模板列表功能。您可以使用主账号下云 API 密钥来调用接口。"),

    FAILEDOPERATION_SIGNIDNOTEXIST("FailedOperation.SignIdNotExist", "签名 ID 不存在。"),

    FAILEDOPERATION_SIGNNUMBERLIMIT("FailedOperation.SignNumberLimit", "签名个数达到最大值。"),

    FAILEDOPERATION_SIGNATUREINCORRECTORUNAPPROVED("FailedOperation.SignatureIncorrectOrUnapproved", "签名未审批或格式错误。（1）可登录 短信控制台，核查签名是否已审批并且审批通过；（2）核查是否符合格式规范，签名只能由中英文、数字组成，要求2 - 12个字，若存在疑问可联系 腾讯云短信小助手。"),

    FAILEDOPERATION_TEMPLATEALREADYPASSEDCHECK("FailedOperation.TemplateAlreadyPassedCheck", "此模板已经通过审核，无法再次进行修改。"),

    FAILEDOPERATION_TEMPLATEIDNOTEXIST("FailedOperation.TemplateIdNotExist", "模板 ID 不存在。"),

    FAILEDOPERATION_TEMPLATEINCORRECTORUNAPPROVED("FailedOperation.TemplateIncorrectOrUnapproved", "模板未审批或内容不匹配。（1）可登录 短信控制台，核查模板是否已审批并审批通过；（2）核查是否符合 格式规范，若存在疑问可联系 腾讯云短信小助手。"),

    FAILEDOPERATION_TEMPLATENUMBERLIMIT("FailedOperation.TemplateNumberLimit", "模板个数达到最大值。"),

    FAILEDOPERATION_TEMPLATEPARAMSETNOTMATCHAPPROVEDTEMPLATE("FailedOperation.TemplateParamSetNotMatchApprovedTemplate", "请求内容与审核通过的模板内容不匹配。请检查请求中模板参数的个数是否与申请的模板一致。若存在疑问可联系 腾讯云短信小助手。"),

    FAILEDOPERATION_TEMPLATEUNAPPROVEDORNOTEXIST("FailedOperation.TemplateUnapprovedOrNotExist", "模板未审批或不存在。可登录 短信控制台，核查模板是否已审批并审批通过。若存在疑问可联系 腾讯云短信小助手。"),

    INTERNALERROR_JSONPARSEFAIL("InternalError.JsonParseFail", "解析用户参数失败，可联系 腾讯云短信小助手。"),

    INTERNALERROR_OTHERERROR("InternalError.OtherError", "其他错误，请联系 腾讯云短信小助手 并提供失败手机号。"),

    INTERNALERROR_PARSEBACKENDRESPONSEFAIL("InternalError.ParseBackendResponseFail", "解析运营商包体失败，可联系 sms helper 。"),

    INTERNALERROR_REQUESTTIMEEXCEPTION("InternalError.RequestTimeException", "请求发起时间不正常，通常是由于您的服务器时间与腾讯云服务器时间差异超过10分钟导致的，请核对服务器时间及 API 接口中的时间字段是否正常。"),

    INTERNALERROR_RESTAPIINTERFACENOTEXIST("InternalError.RestApiInterfaceNotExist", "不存在该 RESTAPI 接口，请核查 REST API 接口说明。"),

    INTERNALERROR_SENDANDRECVFAIL("InternalError.SendAndRecvFail", "接口超时或短信收发包超时，请检查您的网络是否有波动，或联系 腾讯云短信小助手 解决。"),

    INTERNALERROR_SIGFIELDMISSING("InternalError.SigFieldMissing", "后端包体中请求包体没有 Sig 字段或 Sig 为空。"),

    INTERNALERROR_SIGVERIFICATIONFAIL("InternalError.SigVerificationFail", "后端校验 Sig 失败。"),

    INTERNALERROR_TIMEOUT("InternalError.Timeout", "请求下发短信超时，请参考 60008错误详解。"),

    INTERNALERROR_UNKNOWNERROR("InternalError.UnknownError", "未知错误类型。"),

    INVALIDPARAMETER_APPIDANDBIZID("InvalidParameter.AppidAndBizId", "账号与应用id不匹配。"),

    INVALIDPARAMETER_DIRTYWORDFOUND("InvalidParameter.DirtyWordFound", "存在敏感词。"),

    INVALIDPARAMETER_INVALIDPARAMETERS("InvalidParameter.InvalidParameters", "参数有误，如有需要请联系 腾讯云短信小助手。"),

    INVALIDPARAMETERVALUE_BEGINTIMEVERIFYFAIL("InvalidParameterValue.BeginTimeVerifyFail", "参数 BeginTime 校验失败。"),

    INVALIDPARAMETERVALUE_CONTENTLENGTHLIMIT("InvalidParameterValue.ContentLengthLimit", "请求的短信内容太长，短信长度规则请参考 国内短信内容长度计算规则。"),

    INVALIDPARAMETERVALUE_ENDTIMEVERIFYFAIL("InvalidParameterValue.EndTimeVerifyFail", "参数 EndTime 校验失败。"),

    INVALIDPARAMETERVALUE_IMAGEINVALID("InvalidParameterValue.ImageInvalid", "上传的转码图片格式错误，请参照 API 接口说明中对该字段的说明，如有需要请联系 腾讯云短信小助手。"),

    INVALIDPARAMETERVALUE_INCORRECTPHONENUMBER("InvalidParameterValue.IncorrectPhoneNumber", "手机号格式错误。"),

    INVALIDPARAMETERVALUE_INVALIDDOCUMENTTYPE("InvalidParameterValue.InvalidDocumentType", "DocumentType 字段校验错误，请参照 API 接口说明中对该字段的说明，如有需要请联系 腾讯云短信小助手。"),

    INVALIDPARAMETERVALUE_INVALIDINTERNATIONAL("InvalidParameterValue.InvalidInternational", "International 字段校验错误，请参照 API 接口说明中对该字段的说明，如有需要请联系 腾讯云短信小助手。"),

    INVALIDPARAMETERVALUE_INVALIDSIGNPURPOSE("InvalidParameterValue.InvalidSignPurpose", "SignPurpose 字段校验错误，请参照 API 接口说明中对该字段的说明，如有需要请联系 腾讯云短信小助手。"),

    INVALIDPARAMETERVALUE_INVALIDSTARTTIME("InvalidParameterValue.InvalidStartTime", "无效的拉取起始/截止时间，具体原因可能是请求的 SendDateTime 大于 EndDateTime。"),

    INVALIDPARAMETERVALUE_INVALIDTEMPLATEFORMAT("InvalidParameterValue.InvalidTemplateFormat", "模板格式错误，请参考正文模板审核标准。"),

    INVALIDPARAMETERVALUE_INVALIDUSEDMETHOD("InvalidParameterValue.InvalidUsedMethod", "UsedMethod 字段校验错误，请参照 API 接口说明中对该字段的说明，如有需要请联系 腾讯云短信小助手。"),

    INVALIDPARAMETERVALUE_LIMITVERIFYFAIL("InvalidParameterValue.LimitVerifyFail", "参数 Limit 校验失败。"),

    INVALIDPARAMETERVALUE_MARKETINGTEMPLATEWITHOUTUNSUBSCRIBE("InvalidParameterValue.MarketingTemplateWithoutUnsubscribe", "营销短信必须包含退订方式，请在短信模板尾部添加“拒收请回复R”后提交。可参考 关于营销短信退订标识修改的公告。"),

    INVALIDPARAMETERVALUE_OFFSETVERIFYFAIL("InvalidParameterValue.OffsetVerifyFail", "参数 Offset 校验失败。"),

    INVALIDPARAMETERVALUE_PROHIBITEDUSEURLINTEMPLATEPARAMETER("InvalidParameterValue.ProhibitedUseUrlInTemplateParameter", "禁止在模板变量中使用 URL。"),

    INVALIDPARAMETERVALUE_SDKAPPIDNOTEXIST("InvalidParameterValue.SdkAppIdNotExist", "SdkAppId 不存在。"),

    INVALIDPARAMETERVALUE_SIGNALREADYPASSEDCHECK("InvalidParameterValue.SignAlreadyPassedCheck", "此签名已经通过审核，无法再次进行修改。"),

    INVALIDPARAMETERVALUE_SIGNEXISTANDUNAPPROVED("InvalidParameterValue.SignExistAndUnapproved", "已存在相同的待审核签名。"),

    INVALIDPARAMETERVALUE_SIGNNAMELENGTHTOOLONG("InvalidParameterValue.SignNameLengthTooLong", "签名内容长度过长。"),

    INVALIDPARAMETERVALUE_TEMPLATEPARAMETERFORMATERROR("InvalidParameterValue.TemplateParameterFormatError", "验证码模板参数格式错误，验证码类模板，模板变量只能传入0 - 6位（包括6位）纯数字。"),

    INVALIDPARAMETERVALUE_TEMPLATEPARAMETERLENGTHLIMIT("InvalidParameterValue.TemplateParameterLengthLimit", "单个模板变量字符数超过规定的限制数。您可以参考 正文模板审核标准下变量规范中关于长度的描述，对于长期未使用的账号及2024年1月25日后开通新账号默认最长支持6个字符，您的账号具体可支持字符长度以控制台显示为准。更多疑问可联系 腾讯云短信小助手 。"),

    INVALIDPARAMETERVALUE_TEMPLATEWITHDIRTYWORDS("InvalidParameterValue.TemplateWithDirtyWords", "模板内容存在敏感词，请参考正文模板审核标准。"),

    LIMITEXCEEDED_APPCOUNTRYORREGIONDAILYLIMIT("LimitExceeded.AppCountryOrRegionDailyLimit", "业务短信国家/地区日下发条数超过设定的上限，可自行到控制台应用管理>基础配置下调整国际港澳台短信发送限制。"),

    LIMITEXCEEDED_APPCOUNTRYORREGIONINBLACKLIST("LimitExceeded.AppCountryOrRegionInBlacklist", "业务短信国家/地区不在国际港澳台短信发送限制设置的列表中而禁发，可自行到控制台应用管理>基础配置下调整国际港澳台短信发送限制。"),

    LIMITEXCEEDED_APPDAILYLIMIT("LimitExceeded.AppDailyLimit", "业务短信日下发条数超过设定的上限 ，可自行到控制台调整短信频率限制策略。"),

    LIMITEXCEEDED_APPGLOBALDAILYLIMIT("LimitExceeded.AppGlobalDailyLimit", "业务短信国际/港澳台日下发条数超过设定的上限，可自行到控制台应用管理>基础配置下调整发送总量阈值。"),

    LIMITEXCEEDED_APPMAINLANDCHINADAILYLIMIT("LimitExceeded.AppMainlandChinaDailyLimit", "业务短信中国大陆日下发条数超过设定的上限，可自行到控制台应用管理>基础配置下调整发送总量阈值。"),

    LIMITEXCEEDED_DAILYLIMIT("LimitExceeded.DailyLimit", "短信日下发条数超过设定的上限 (国际/港澳台)，如需调整限制，可联系 腾讯云短信小助手。"),

    LIMITEXCEEDED_DELIVERYFREQUENCYLIMIT("LimitExceeded.DeliveryFrequencyLimit", "下发短信命中了频率限制策略，可自行到控制台调整短信频率限制策略，如有其他需求请联系 腾讯云短信小助手。"),

    LIMITEXCEEDED_PHONENUMBERCOUNTLIMIT("LimitExceeded.PhoneNumberCountLimit", "调用接口单次提交的手机号个数超过200个，请遵守 API 接口输入参数 PhoneNumberSet 描述。"),

    LIMITEXCEEDED_PHONENUMBERDAILYLIMIT("LimitExceeded.PhoneNumberDailyLimit", "单个手机号日下发短信条数超过设定的上限，可自行到控制台调整短信频率限制策略。"),

    LIMITEXCEEDED_PHONENUMBERONEHOURLIMIT("LimitExceeded.PhoneNumberOneHourLimit", "单个手机号1小时内下发短信条数超过设定的上限，可自行到控制台调整短信频率限制策略。"),

    LIMITEXCEEDED_PHONENUMBERSAMECONTENTDAILYLIMIT("LimitExceeded.PhoneNumberSameContentDailyLimit", "单个手机号下发相同内容超过设定的上限，可自行到控制台调整短信频率限制策略。"),

    LIMITEXCEEDED_PHONENUMBERTHIRTYSECONDLIMIT("LimitExceeded.PhoneNumberThirtySecondLimit", "单个手机号30秒内下发短信条数超过设定的上限，可自行到控制台调整短信频率限制策略。"),

    MISSINGPARAMETER_EMPTYPHONENUMBERSET("MissingParameter.EmptyPhoneNumberSet", "传入的号码列表为空，请确认您的参数中是否传入号码。"),

    UNAUTHORIZEDOPERATION_INDIVIDUALUSERMARKETINGSMSPERMISSIONDENY("UnauthorizedOperation.IndividualUserMarketingSmsPermissionDeny", "个人用户没有发营销短信的权限，请参考 权益区别。"),

    UNAUTHORIZEDOPERATION_REQUESTIPNOTINWHITELIST("UnauthorizedOperation.RequestIpNotInWhitelist", "请求 IP 不在白名单中，您配置了校验请求来源 IP，但是检测到当前请求 IP 不在配置列表中，如有需要请联系 腾讯云短信小助手。"),

    UNAUTHORIZEDOPERATION_REQUESTPERMISSIONDENY("UnauthorizedOperation.RequestPermissionDeny", "请求没有权限，请联系 腾讯云短信小助手。"),

    UNAUTHORIZEDOPERATION_SDKAPPIDISDISABLED("UnauthorizedOperation.SdkAppIdIsDisabled", "此 SdkAppId 禁止提供服务，如有需要请联系 腾讯云短信小助手。"),

    UNAUTHORIZEDOPERATION_SERVICESUSPENDDUETOARREARS("UnauthorizedOperation.ServiceSuspendDueToArrears", "欠费被停止服务，可自行登录腾讯云充值来缴清欠款。"),

    UNAUTHORIZEDOPERATION_SMSSDKAPPIDVERIFYFAIL("UnauthorizedOperation.SmsSdkAppIdVerifyFail", "SmsSdkAppId 校验失败，请检查 SmsSdkAppId 是否属于 云API密钥 的关联账户。"),

    UNSUPPORTEDOPERATION_("UnsupportedOperation.", "不支持该请求。"),

    UNSUPPORTEDOPERATION_CHINESEMAINLANDTEMPLATETOGLOBALPHONE("UnsupportedOperation.ChineseMainlandTemplateToGlobalPhone", "国内短信模板不支持发送国际/港澳台手机号。发送国际/港澳台手机号请使用国际/港澳台短信正文模板。"),

    UNSUPPORTEDOPERATION_CONTAINDOMESTICANDINTERNATIONALPHONENUMBER("UnsupportedOperation.ContainDomesticAndInternationalPhoneNumber", "群发请求里既有国内手机号也有国际手机号。请排查是否存在（1）使用国内签名或模板却发送短信到国际手机号；（2）使用国际签名或模板却发送短信到国内手机号。"),

    UNSUPPORTEDOPERATION_GLOBALTEMPLATETOCHINESEMAINLANDPHONE("UnsupportedOperation.GlobalTemplateToChineseMainlandPhone", "国际/港澳台短信模板不支持发送国内手机号。发送国内手机号请使用国内短信正文模板。"),

    UNSUPPORTEDOPERATION_UNSUPPORTEDREGION("UnsupportedOperation.UnsupportedRegion", "不支持该地区短信下发。")
    ;

    private final String code;
    private final String message;

    TencentCloudResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


    // 根据code获取message的静态方法
    public static String getMessageByCode(String code) {
        for (TencentCloudResponseCode value : TencentCloudResponseCode.values()) {
            if (value.getCode().equals(code)) {
                return value.getMessage();
            }
        }
        return "未知消息"; // 或者可以返回一个默认消息，比如 "Unknown code"
    }

}
