package com.medicento.salesappmedicento.helperData;

public class OrderedMedicine {
    private String mMedicineName;
    private String mMedicineCompany;
    private int mQty;
    private String code;
    private float mRate;
    private float mCost;

    public OrderedMedicine(){

    }

    public OrderedMedicine(String name, String company, int qty, float rate, float cost, String code){
        mMedicineName = name;
        this.code = code;
        mMedicineCompany = company;
        mQty = qty;
        mRate = rate;
        mCost = cost;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMedicineName() {
        return mMedicineName;
    }

    public String getMedicineCompany() {
        return mMedicineCompany;
    }

    public int getQty() {
        return mQty;
    }

    public float getRate() {
        return mRate;
    }

    public float getCost(){return mCost;}

    public void setMedicineCompany(String company) {
        this.mMedicineCompany = company;
    }

    public void setMedicineName(String name) {
        this.mMedicineName = name;
    }

    public void setQty(int qty) {
        this.mQty = qty;
    }

    public void setRate(float rate) {this.mRate = rate;}

    public void setCost(float cost){
        this.mCost = cost;
    }
}
