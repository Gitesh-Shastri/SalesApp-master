package com.medicento.salesappmedicento.networking.data;

public class SalesPerson {
    private String mName;
    private float mTotalSales;
    private int mNoOfOrder;
    private int mReturn;
    private float mEarnings;
    private String mId;
    private String mAllocatedAreaId;

    public SalesPerson(String name, float totalSales, int noOfOrder, int returns, float earnings, String id, String allocatedAreaId) {
        mName = name;
        mTotalSales = totalSales;
        mNoOfOrder = noOfOrder;
        mReturn = returns;
        mEarnings = earnings;
        mId = id;
        mAllocatedAreaId = allocatedAreaId;
    }

    public String getName() {
        return mName;
    }

    public float getTotalSales() {
        return mTotalSales;
    }

    public int getNoOfOrder() {return mNoOfOrder;}

    public int getReturn() {
        return mReturn;
    }

    public float getEarnings() {
        return mEarnings;
    }

    public String getId() {
        return mId;
    }

    public String getAllocatedArea() {
        return mId;
    }
}
