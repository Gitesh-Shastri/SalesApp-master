package com.medicento.salesappmedicento.networking.data;

public class RecentOrder {
    private String pOrderId;
    private String pDate;
    private int Total;

    public RecentOrder(String orderId, String date, int total) {
        pOrderId = orderId;
        pDate = date;
        Total = total;
    }

    public String getOrderId() {
        return pOrderId;
    }

    public String getDate() {
        return pDate;
    }

    public int getTotal() {
        return Total;
    }
}
