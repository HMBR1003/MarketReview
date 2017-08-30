package org.baseballbaedal.baseballbaedal.Order;

/**
 * Created by Administrator on 2017-08-30.
 */

public class OrderAdapterItem {
    private OrderData orderData;
    private String orderId;

    public OrderAdapterItem(OrderData orderData,String orderId){
        this.orderData = orderData;
        this.orderId = orderId;
    }
}
