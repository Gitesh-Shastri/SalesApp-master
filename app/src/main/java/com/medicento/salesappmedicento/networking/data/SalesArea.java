package com.medicento.salesappmedicento.networking.data;

import java.io.Serializable;

public class SalesArea implements Serializable {
    private String mAreaName;
    private String mCity;
    private String mState;
    private int mPincode;
    private String mId;

    public SalesArea(String areaName, String city, String state, int pincode, String id) {
        mAreaName = areaName;
        mCity = city;
        mState = state;
        mPincode = pincode;
        mId = id;
    }

    public String getAreaName() {
        return mAreaName;
    }

    public String getCity() {
        return mCity;
    }

    public String getState() {
        return mState;
    }

    public String getId() {
        return mId;
    }

    public int getPincode() {
        return mPincode;
    }
}
