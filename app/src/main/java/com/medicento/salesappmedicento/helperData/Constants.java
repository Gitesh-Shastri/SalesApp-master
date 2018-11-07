package com.medicento.salesappmedicento.helperData;

public class Constants {
    public static final String USER_LOGIN_URL = "https://medicento-api.herokuapp.com/user/login";
    public static final String AREA_DATA_URL = "https://medicento-api.herokuapp.com/area";
    public static final String PHARMACY_DATA_URL = "https://medicento-api.herokuapp.com/pharma";
    public static final String MEDICINE_DATA_URL = "https://medicento-api.herokuapp.com/product/medimap";
    public static final String PLACE_ORDER_URL = "https://medicento-api.herokuapp.com/product/order";
    private static String RECENT_ORDER_URL = "https://medicento-api.herokuapp.com/product/recent_order/";

    public static final int SALES_AREA_LOADER_ID = 1;
    public static final int SALES_PHARMACY_LOADER = 2;
    public static final int MEDICINE_DATA_LOADER_ID = 3;
    public static final int LOG_IN_LOADER_ID = 4;
    public static final int PLACE_ORDER_LOADER_ID = 5;

    public static final int RC_SIGN_IN = 123;
    public static final int RC_CONFIRM_ORDER = 456;

    public static final String SALE_PERSON_EMAIL = "email";
    public static final String USER_PASSWORD = "password";
    public static final String SALE_PERSON_NAME = "salesPersonName";
    public static final String SALE_PERSON_TOTAL_SALES = "salesPersonTotalSales";
    public static final String SALE_PERSON_RETURNS = "salesPersonReturns";
    public static final String SALE_PERSON_EARNINGS = "salesPersonEarnings";
    public static final String SALE_PERSON_ID = "salesPersonId";
    public static final String SALE_PERSON_ALLOCATED_AREA_ID = "salesPersonAllocatedArea";
    public static final String SELECTED_PHARMACY = "selected_pharmacy";
    public static final String ORDER_TOTAL_COST = "orderTotalCost";
    public static final String SALE_PERSON_NO_OF_ORDERS = "salesPersonNoOfOrder";
    public static final String SALE_PERSON_ALLOCATED_AREA_NAME = "salesPersonAllocatedAreaName";
}
