package com.tuling.printer.service;

import com.tuling.printer.util.PrinterConfig;
import com.tuling.printer.vo.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LylJavas
 * @create 2021/2/26 15:17
 * @description 打印机管理 需要测试哪个用例，直接解注释运行对应的测试用例即可
 */
public class XpsdkOtherApiDemoTest {
    /**
     * 打印服务对象实例化
     */
    public PrintService service = new PrintService();

    /**
     * 1.批量地添加打印机
     */
    @Test
    public void addPrinters() {
        AddPrinterRequest request = new AddPrinterRequest();
        // 添加公共参数
        PrinterConfig.createRequestHeader(request);
        //打印机列表
        List<AddPrinterRequestItem> itemList = new ArrayList<>();

        AddPrinterRequestItem item = new AddPrinterRequestItem();

        //无效打印机编号,此处只是为了举例说明接口返回参数使用，在实际开发中，此处均用有效的打印机编号
        item.setSn("111");
        item.setName("X58A");
        itemList.add(item);

        item = new AddPrinterRequestItem();
        // 打印机编号，必须是真实的打印机编号，否在会导致后续api无法打印
        item.setSn(PrinterConfig.OK_PRINTER_SN);
        //打印机名称
        item.setName("X58BTest");
        itemList.add(item);

        //*必填*：items:数组元素为 json 对象：
        //{"name":"打印机名称","sn":"打印机编号"}
        //其中打印机编号 sn 和名称 name 字段为必填项，每次最多添加50台
        AddPrinterRequestItem[] items = new AddPrinterRequestItem[itemList.size()];
        itemList.toArray(items);
        request.setItems(items);
        try {
            ObjectRestResponse<PrinterResult> resp = service.addPrinters(request);
            //resp.data:返回1个 json 对象，包含成功和失败的信息，详看https://www.xpyun.net/open/index.html示例
            System.out.println(resp);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * 2.设置打印机语音类型
     * 声音类型： 0真人语音（大） 1真人语音（中） 2真人语音（小） 3 嘀嘀声  4 静音
     */
    @Test
    public void setVoiceType() {
        SetVoiceTypeRequest request = new SetVoiceTypeRequest();
        PrinterConfig.createRequestHeader(request);
        //*必填*：打印机编号
        request.setSn(PrinterConfig.OK_PRINTER_SN);
        //*必填*：声音类型： 0真人语音（大） 1真人语音（中） 2真人语音（小） 3 嘀嘀声  4 静音
        request.setVoiceType(0);
        ObjectRestResponse<Boolean> resp = service.setPrinterVoiceType(request);
        //resp.data:返回布尔类型：true 表示设置成功 false 表示设置失败
        System.out.println(resp);
    }

//    /**
//     * 5.批量删除打印机
//     */
//    @Test
//    public void delPrinters() {
//        DelPrinterRequest request = new DelPrinterRequest();
//        Config.createRequestHeader(request);
//        //*必填*：打印机编号集合，类型为字符串数组
//        String[] snlist = new String[1];
//        //*必填*：打印机编号
//        snlist[0] = Config.OK_PRINTER_SN;
//        request.setSnlist(snlist);
//        ObjectRestResponse<PrinterResult> resp = service.delPrinters(request);
//        //resp.data:返回1个 json 对象，包含成功和失败的信息，详看https://www.xpyun.net/open/index.html示例
//        System.out.println(resp);
//    }
//
//    /**
//     * 6.修改打印机信息
//     */
//    @Test
//    public void updPrinter() {
//        UpdPrinterRequest request = new UpdPrinterRequest();
//        Config.createRequestHeader(request);
//        //*必填*：打印机编号
//        request.setSn(Config.OK_PRINTER_SN);
//        //*必填*：打印机名称
//        request.setName("X58C1");
//
//        ObjectRestResponse<Boolean> resp = service.updPrinter(request);
//        //resp.data:返回布尔类型：true 表示成功 false 表示失败
//        System.out.println(resp);
//    }

    /**
     * 7.清空待打印队列
     */
    @Test
    public void delPrinterQueue() {
        PrinterRequest request = new PrinterRequest();
        PrinterConfig.createRequestHeader(request);
        //*必填*：打印机编号
        request.setSn(PrinterConfig.OK_PRINTER_SN);
        ObjectRestResponse<Boolean> resp = service.delPrinterQueue(request);
        //resp.data:返回布尔类型：true 表示成功 false 表示失败
        System.out.println(resp);
    }

    /**
     * 8.查询订单是否打印成功
     */
    @Test
    public void queryOrderState() {
        QueryOrderStateRequest request = new QueryOrderStateRequest();
        PrinterConfig.createRequestHeader(request);
        // *必填*：订单编号，由“打印订单”接口返回
        request.setOrderId("OM19111303591262210878");
        ObjectRestResponse<Boolean> resp = service.queryOrderState(request);
        //resp.data:返回布尔类型,已打印返回true,未打印返回false
        System.out.println(resp);
    }

    /**
     * 9.查询指定打印机某天的订单统计数
     */
    @Test
    public void queryOrderStatis() {
        QueryOrderStatisRequest request = new QueryOrderStatisRequest();
        PrinterConfig.createRequestHeader(request);
        //*必填*：打印机编号
        request.setSn(PrinterConfig.OK_PRINTER_SN);
        //*必填*：查询日期，格式yyyy-MM-dd，如：2020-09-09
        request.setDate("2020-09-09");
        ObjectRestResponse<OrderStatisResult> resp = service.queryOrderStatis(request);
        //resp.data:json对象，返回已打印订单数和等待打印订单数，如：{"printed": 2, "waiting": 0}
        System.out.println(resp);
    }

    /**
     * 10.获取指定打印机状态
     */
    @Test
    public void queryPrinterStatus() {
        PrinterRequest request = new PrinterRequest();
        PrinterConfig.createRequestHeader(request);
        //*必填*：打印机编号
        request.setSn(PrinterConfig.OK_PRINTER_SN);
        ObjectRestResponse<Integer> resp = service.queryPrinterStatus(request);
        //resp.data:返回打印机状态值，共三种：
        //0 表示离线
        //1 表示在线正常
        //2 表示在线缺纸
        //备注：离线的判断是打印机与服务器失去联系超过 30 秒
        System.out.println(resp);
    }
    /**
     * 10.获取指定打印机状态
     */
    @Test
    public void queryPrintersStatus() {
        PrintersRequest request = new PrintersRequest();
        PrinterConfig.createRequestHeader(request);
        //*必填*：打印机编号
        List<String> snList=new ArrayList<>();
        // 添加打印机设备编号
        snList.add("xxxxxxxxxxxxxx");
        snList.add("xxxxxxxxxxxxxx");
        snList.add("xxxxxxxxxxxxxx");
        snList.add("xxxxxxxxxxxxxx");
        request.setSnlist(snList);
        ObjectRestResponse<List<Integer>> resp = service.queryPrintersStatus(request);
        //resp.data:返回打印机状态值，共三种：
        //0 表示离线
        //1 表示在线正常
        //2 表示在线缺纸
        //备注：离线的判断是打印机与服务器失去联系超过 30 秒
        System.out.println(resp);
    }
    /**
     * 11.金额播报
     */
    @Test
    public void playVoice() {
        VoiceRequest request = new VoiceRequest();
        PrinterConfig.createRequestHeader(request);
        //*必填*：打印机编号
        request.setSn(PrinterConfig.OK_PRINTER_SN);
        //支付方式：
        //取值范围41~55：
        //支付宝 41、微信 42、云支付 43、银联刷卡 44、银联支付 45、会员卡消费 46、会员卡充值 47、翼支付 48、成功收款 49、嘉联支付 50、壹钱包 51、京东支付 52、快钱支付 53、威支付 54、享钱支付 55
        //仅用于支持金额播报的芯烨云打印机。
        request.setPayType(41);
        //支付与否：
        //取值范围59~61：
        //退款 59 到账 60 消费 61。
        //仅用于支持金额播报的芯烨云打印机。
        request.setPayMode(59);
        //支付金额：
        //最多允许保留2位小数。
        //仅用于支持金额播报的芯烨云打印机。
        request.setMoney(24.15);
        ObjectRestResponse<String> resp = service.playVoice(request);
        //resp.data:正确返回0
        System.out.println(resp);
    }
}
