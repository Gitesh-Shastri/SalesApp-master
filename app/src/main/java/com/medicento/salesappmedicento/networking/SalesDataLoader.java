package com.medicento.salesappmedicento.networking;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.medicento.salesappmedicento.helperData.OrderedMedicine;
import com.medicento.salesappmedicento.R;
import com.medicento.salesappmedicento.networking.util.SalesDataExtractor;

import java.util.ArrayList;

public class SalesDataLoader extends AsyncTaskLoader {

    private String mUrl;
    private String mAction;
    private Context mContext;
    private String mPharmaId;
    private ArrayList<OrderedMedicine> mOrderItems;
    public SalesDataLoader(@NonNull Context context, String url, String action) {
        super(context);
        mUrl = url;
        mAction = action;
        mContext = context;
    }

    public SalesDataLoader(Context context, String url, String action, ArrayList<OrderedMedicine> orderItems, String pharmaId) {
        super(context);
        mUrl = url;
        mAction = action;
        mContext = context;
        mOrderItems = orderItems;
        mPharmaId = pharmaId;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public Object loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        //TODO: change mContext to getContext()
        if (mAction.equals(mContext.getString(R.string.login_action))) {
            return SalesDataExtractor.initiateConnection(mUrl, mAction, mContext, null, null);
        } else if (mAction.equals(mContext.getString(R.string.place_order_action))) {
            return SalesDataExtractor.initiateConnection(mUrl, mAction, mContext, mOrderItems, mPharmaId);
        }
        return null;
    }
}