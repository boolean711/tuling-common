package com.tuling.printer.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.tuling.printer.util.HashSignUtil;
import com.tuling.printer.util.HttpClientUtil;
import com.tuling.printer.vo.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 云打印相关接口封装类
 * @author JavaLyl
 */
@Service
public class PrintService {

    @Value("${xinye.secretId}")
    public  String USER_NAME = "xxxxxxxxxxxxxxx";
    /**
     * *必填*：开发者密钥 ：芯烨云后台注册账号后自动生成的开发者密钥，开发者用户注册成功之后，登录芯烨云后台，在【个人中心=》账号信息】下可查看开发者密钥
     *
     * 当前【xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx】只是样例，需修改再使用
     */
    @Value("${xinye.secretKey}")
    public   String USER_KEY;

    private static String BASE_URL = "https://open.xpyun.net/api/openapi";

    public  void createRequestHeader(RestRequest request) {
        //*必填*：芯烨云平台注册用户名（开发者 ID）
        request.setUser(USER_NAME);
        //*必填*：当前UNIX时间戳
        request.setTimestamp(System.currentTimeMillis() + "");
        //*必填*：对参数 user + UserKEY + timestamp 拼接后（+号表示连接符）进行SHA1加密得到签名，值为40位小写字符串，其中 UserKEY 为用户开发者密钥
        request.setSign(HashSignUtil.sign(request.getUser() + USER_KEY + request.getTimestamp()));

        //debug=1返回非json格式的数据，仅测试时候使用
        request.setDebug("0");
    }
    /**
     * 1.批量添加打印机
     * @param restRequest
     * @return
     */
    public ObjectRestResponse<PrinterResult> addPrinters(AddPrinterRequest restRequest) {
        String url = BASE_URL + "/xprinter/addPrinters";
        String jsonRequest = JSON.toJSONString(restRequest);
        String resp = HttpClientUtil.doPostJSON(url, jsonRequest);
        ObjectRestResponse<PrinterResult> result = JSON.parseObject(resp, new TypeReference<ObjectRestResponse<PrinterResult>>(){});
        return result;
    }

    /**
     * 2.设置打印机语音类型
     * @param restRequest
     * @return
     */
    public ObjectRestResponse<Boolean> setPrinterVoiceType(SetVoiceTypeRequest restRequest) {
        String url = BASE_URL + "/xprinter/setVoiceType";
        String jsonRequest = JSON.toJSONString(restRequest);
        String resp = HttpClientUtil.doPostJSON(url, jsonRequest);
        ObjectRestResponse<Boolean> result = JSON.parseObject(resp, new TypeReference<ObjectRestResponse<Boolean>>(){});
        return result;
    }

    /**
     * 3.打印小票订单
     * @param restRequest
     * @return
     */
    public ObjectRestResponse<String> print(PrintRequest restRequest) {
        String url = BASE_URL + "/xprinter/print";
        String jsonRequest = JSON.toJSONString(restRequest);
        String resp = HttpClientUtil.doPostJSON(url, jsonRequest);
        ObjectRestResponse<String> result = JSON.parseObject(resp, new TypeReference<ObjectRestResponse<String>>(){});
        return result;
    }

    /**
     * 4.打印标签订单
     * @param restRequest
     * @return
     */
    public ObjectRestResponse<String> printLabel(PrintRequest restRequest) {
        String url = BASE_URL + "/xprinter/printLabel";
        String jsonRequest = JSON.toJSONString(restRequest);
        String resp = HttpClientUtil.doPostJSON(url, jsonRequest);
        ObjectRestResponse<String> result = JSON.parseObject(resp, new TypeReference<ObjectRestResponse<String>>(){});
        return result;
    }

    /**
     * 5.批量删除打印机
     * @param restRequest
     * @return
     */
    public ObjectRestResponse<PrinterResult> delPrinters(DelPrinterRequest restRequest) {
        String url = BASE_URL + "/xprinter/delPrinters";
        String jsonRequest = JSON.toJSONString(restRequest);
        String resp = HttpClientUtil.doPostJSON(url, jsonRequest);
        ObjectRestResponse<PrinterResult> result = JSON.parseObject(resp, new TypeReference<ObjectRestResponse<PrinterResult>>(){});
        return result;
    }

    /**
     * 6.修改打印机信息
     * @param restRequest
     * @return
     */
    public ObjectRestResponse<Boolean> updPrinter(UpdPrinterRequest restRequest) {
        String url = BASE_URL + "/xprinter/updPrinter";
        String jsonRequest = JSON.toJSONString(restRequest);
        String resp = HttpClientUtil.doPostJSON(url, jsonRequest);
        ObjectRestResponse<Boolean> result = JSON.parseObject(resp, new TypeReference<ObjectRestResponse<Boolean>>(){});
        return result;
    }

    /**
     * 7.清空待打印队列
     * @param restRequest
     * @return
     */
    public ObjectRestResponse<Boolean> delPrinterQueue(PrinterRequest restRequest) {
        String url = BASE_URL + "/xprinter/delPrinterQueue";
        String jsonRequest = JSON.toJSONString(restRequest);
        String resp = HttpClientUtil.doPostJSON(url, jsonRequest);
        ObjectRestResponse<Boolean> result = JSON.parseObject(resp, new TypeReference<ObjectRestResponse<Boolean>>(){});
        return result;
    }

    /**
     * 8.查询订单是否打印成功
     * @param restRequest
     * @return
     */
    public ObjectRestResponse<Boolean> queryOrderState(QueryOrderStateRequest restRequest) {
        String url = BASE_URL + "/xprinter/queryOrderState";
        String jsonRequest = JSON.toJSONString(restRequest);
        String resp = HttpClientUtil.doPostJSON(url, jsonRequest);
        ObjectRestResponse<Boolean> result = JSON.parseObject(resp, new TypeReference<ObjectRestResponse<Boolean>>(){});
        return result;
    }

    /**
     * 9.查询打印机某天的订单统计数
     * @param restRequest
     * @return
     */
    public ObjectRestResponse<OrderStatisResult> queryOrderStatis(QueryOrderStatisRequest restRequest) {
        String url = BASE_URL + "/xprinter/queryOrderStatis";
        String jsonRequest = JSON.toJSONString(restRequest);
        String resp = HttpClientUtil.doPostJSON(url, jsonRequest);
        ObjectRestResponse<OrderStatisResult> result = JSON.parseObject(resp, new TypeReference<ObjectRestResponse<OrderStatisResult>>(){});
        return result;
    }

    /**
     * 10.查询打印机状态
     *
     * 0、离线 1、在线正常 2、在线不正常
     * 备注：异常一般是无纸，离线的判断是打印机与服务器失去联系超过30秒
     * @param restRequest
     * @return
     */
    public ObjectRestResponse<Integer> queryPrinterStatus(PrinterRequest restRequest) {
        String url = BASE_URL + "/xprinter/queryPrinterStatus";
        String jsonRequest = JSON.toJSONString(restRequest);
        String resp = HttpClientUtil.doPostJSON(url, jsonRequest);
        ObjectRestResponse<Integer> result = JSON.parseObject(resp, new TypeReference<ObjectRestResponse<Integer>>(){});
        return result;
    }

    /**
     * 10.批量查询打印机状态
     *
     * 0、离线 1、在线正常 2、在线不正常
     * 备注：异常一般是无纸，离线的判断是打印机与服务器失去联系超过30秒
     * @param restRequest
     * @return
     */
    public ObjectRestResponse<List<Integer>> queryPrintersStatus(PrintersRequest restRequest) {
        String url = BASE_URL + "/xprinter/queryPrintersStatus";
        String jsonRequest = JSON.toJSONString(restRequest);
        String resp = HttpClientUtil.doPostJSON(url, jsonRequest);
        ObjectRestResponse<List<Integer>> result = JSON.parseObject(resp, new TypeReference<ObjectRestResponse<List<Integer>>>(){});
        return result;
    }

    /**
     * 11.云喇叭播放语音
     * @param restRequest
     * @return
     */
    public ObjectRestResponse<String> playVoice(VoiceRequest restRequest) {
        String url = BASE_URL + "/xprinter/playVoice";
        String jsonRequest = JSON.toJSONString(restRequest);
        String resp = HttpClientUtil.doPostJSON(url, jsonRequest);
        ObjectRestResponse<String> result = JSON.parseObject(resp, new TypeReference<ObjectRestResponse<String>>(){});
        return result;
    }

    /**
     * 12.POS指令
     * @param restRequest
     * @return
     */
    public ObjectRestResponse<String> pos(PrintRequest restRequest) {
        String url = BASE_URL + "/xprinter/pos";
        String jsonRequest = JSON.toJSONString(restRequest);
        String resp = HttpClientUtil.doPostJSON(url, jsonRequest);
        ObjectRestResponse<String> result = JSON.parseObject(resp, new TypeReference<ObjectRestResponse<String>>(){});
        return result;
    }
    /**
     * 13.钱箱控制
     * @param restRequest
     * @return
     */
    public ObjectRestResponse<String> controlBox(PrintRequest restRequest) {
        String url = BASE_URL + "/xprinter/controlBox";
        String jsonRequest = JSON.toJSONString(restRequest);
        String resp = HttpClientUtil.doPostJSON(url, jsonRequest);
        ObjectRestResponse<String> result = JSON.parseObject(resp, new TypeReference<ObjectRestResponse<String>>(){});
        return result;
    }

}
