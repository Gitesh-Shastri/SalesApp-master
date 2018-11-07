package com.medicento.salesappmedicento.helperData;

import com.medicento.salesappmedicento.networking.data.SalesArea;
import com.medicento.salesappmedicento.networking.data.SalesPharmacy;

public class SavedData {
    public static OrderedMedicineAdapter mOrderedMedicineAdapter;
    public static float mOverallCost;
    public static String mOrderId;
    public static String mDeliveryDate;
    public static SalesPharmacy mSelectedPharmacy;
    public static SalesArea mSelectedArea;

    public static void saveAdapter(OrderedMedicineAdapter adapter) {
        mOrderedMedicineAdapter = adapter;
    }

    public static void saveOrderDetails(float cost, String[] x) {
        mOverallCost = cost;
        mOrderId = x[0];
        mDeliveryDate = x[1];
    }

    public static void saveSelectedPharmacy(SalesPharmacy pharmacy) {
        mSelectedPharmacy = pharmacy;
    }

    public static void saveAreaAndPharmacy(SalesArea selectedArea, SalesPharmacy selectedPharmacy) {
        mSelectedPharmacy = selectedPharmacy;
        mSelectedArea = selectedArea;
    }
}
