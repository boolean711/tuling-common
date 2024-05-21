package com.tuling.printer.vo;

/**
 * 订单状态
 *
 * @author RabyGao
 * @date Aug 8, 2019
 */
public enum OrderStatusType {

    /**
     * 处理中
     */
    Processing(0),
    /**
     * 完成
     */
    Completed(1),
    /**
     * 失败
     */
    Failed(2);

    private final int val;

    public int getVal() {
        return val;
    }

    OrderStatusType(int num) {
        this.val = num;
    }

    public static OrderStatusType getOrderStatusType(int val) {
        for (OrderStatusType type : OrderStatusType.values()) {
            if (type.getVal() == val) {
                return type;
            }
        }
        return Processing;
    }

}
