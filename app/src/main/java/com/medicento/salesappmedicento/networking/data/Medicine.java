package com.medicento.salesappmedicento.networking.data;

import java.io.Serializable;

public class Medicine implements Serializable {
    private String mMedicentoName;
    private String mCompanyName;
    private int mPrice;
    private String code;
    private String mId;

    public Medicine (String medicentoName, String companyName, int price, String id, String code) {
        mMedicentoName = medicentoName;
        mCompanyName = companyName;
        mPrice = price;
        mId = id;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getMedicentoName() {
        return mMedicentoName;
    }

    public String getCompanyName() {
        return mCompanyName;
    }

    public int getPrice() {
        return mPrice;
    }

    public String getId() {
        return mId;
    }
}
