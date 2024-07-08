package com.tuling.wxmq.controller;

import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@AllArgsConstructor
@RestController
@RequestMapping("/wx/jsapi/{appid}")
public class WxJsapiController {
    @Autowired
    private final WxMpService wxService;

    @GetMapping("/getJsapiTicket")
    public String getJsapiTicket(@PathVariable String appid) throws WxErrorException {
        final WxJsapiSignature jsapiSignature = this.wxService.switchoverTo(appid).createJsapiSignature("111");
        System.out.println(jsapiSignature);
        return this.wxService.getJsapiTicket(true);
    }
}
