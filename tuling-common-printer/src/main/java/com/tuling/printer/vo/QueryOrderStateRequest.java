package com.tuling.printer.vo;

/**
 * 查询订单状态请求参数
 *
 * @author RabyGao
 * @date Aug 7, 2019
 */
public class QueryOrderStateRequest extends RestRequest {

    /**
     * 订单编号
     */
    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
