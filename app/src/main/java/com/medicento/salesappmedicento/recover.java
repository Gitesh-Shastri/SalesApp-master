package com.medicento.salesappmedicento;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.medicento.salesappmedicento.fragments.PlaceOrderFragment;
import com.medicento.salesappmedicento.helperData.AreaSpinnerCustomAdapter;
import com.medicento.salesappmedicento.helperData.Constants;
import com.medicento.salesappmedicento.helperData.PhramaSpinnerCustomAdapter;
import com.medicento.salesappmedicento.networking.SalesDataLoader;
import com.medicento.salesappmedicento.networking.data.Medicine;
import com.medicento.salesappmedicento.networking.data.SalesArea;
import com.medicento.salesappmedicento.networking.data.SalesPharmacy;

import java.util.ArrayList;

public class recover extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Object> {


    ArrayList<SalesArea> mSalesAreaDetails;
    ArrayList<SalesPharmacy> mSalesPharmacyDetails;
    Spinner mSelectAreaSpinner, mSelectPharmacySpinner;
    SalesPharmacy mSelectedPharmacy;
    AreaSpinnerCustomAdapter mAreaAdapter;
    PhramaSpinnerCustomAdapter mPharmaAdapter;
    SalesArea mSelectedArea;
    PlaceOrderFragment mPlaceOrder;
    TextView tv ;
    boolean calledOnAreaLoadingFinished, calledOnPharmacyLoadingFinished;
    boolean pharmaLoadFlag, medicineLoadFlag;
    PlaceOrderFragment.OnPlaceOrderChangeListener mPlaceOrderChangeListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover);
        mSelectAreaSpinner = findViewById(R.id.area);
        mSelectPharmacySpinner = findViewById(R.id.pharmacy);
        android.app.LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(Constants.SALES_AREA_LOADER_ID, null, this);
        getLoaderManager().initLoader(Constants.MEDICINE_DATA_LOADER_ID, null, this);
        tv = findViewById(R.id.textView3);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(recover.this,pharmacyRegister.class);
                startActivity(i);
            }
        });

        mSelectAreaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // if (!calledOnAreaLoadingFinished) {
                mPlaceOrderChangeListener.onAreaSelected(mAreaAdapter.getItem(position));
                /*} else {
                    calledOnAreaLoadingFinished = false;
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSelectPharmacySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // if (!calledOnPharmacyLoadingFinished) {
               /* } else {
                    calledOnPharmacyLoadingFinished = false;
                }*/
                mSelectedPharmacy = mPharmaAdapter.getItem(position);
                mPlaceOrderChangeListener.onPharmaSelected(mPharmaAdapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    @Override
    public android.content.Loader<Object> onCreateLoader(int i, Bundle bundle) {
        if (i == Constants.SALES_AREA_LOADER_ID) {
            Uri baseUri = Uri.parse(Constants.AREA_DATA_URL);
            Uri.Builder builder = baseUri.buildUpon();
            return new SalesDataLoader(this, builder.toString(), getString(R.string.fetch_area_action));
        } else if (i == Constants.SALES_PHARMACY_LOADER) {
            Uri baseUri = Uri.parse(Constants.PHARMACY_DATA_URL);
            Uri.Builder builder = baseUri.buildUpon();
            return new SalesDataLoader(this, builder.toString(), getString(R.string.fetch_pharmacy_action));
        }
        return null;
    }

    @Override
    public void onLoadFinished(android.content.Loader<Object> loader, Object data) {
        switch (loader.getId()) {
            case Constants.SALES_AREA_LOADER_ID:
                mSalesAreaDetails = (ArrayList<SalesArea>) data;
                calledOnAreaLoadingFinished = true;
                if (mSalesAreaDetails.isEmpty()) {
                    //mNoNetworkImage.setVisibility(View.VISIBLE);
                    //mNoNetworkInfo.setVisibility(View.VISIBLE);
                    pharmaLoadFlag = true;
                    medicineLoadFlag = true;
                    dismissLoadingIndicators();
                    return;
                }

                getLoaderManager().initLoader(Constants.SALES_PHARMACY_LOADER, null, this);
                break;
            case Constants.SALES_PHARMACY_LOADER:
                calledOnPharmacyLoadingFinished = true;
                mSalesPharmacyDetails = (ArrayList<SalesPharmacy>) data;
                ArrayList<SalesPharmacy> pharmacyList = new ArrayList<>();
                for (SalesPharmacy salesPharmacy : mSalesPharmacyDetails) {
                    if (mSalesAreaDetails.get(0).getId().equals(salesPharmacy.getAreaId())) {
                        pharmacyList.add(salesPharmacy);
                    }
                }

                pharmaLoadFlag = true;
                dismissLoadingIndicators();
                break;
        }

    }
        private void dismissLoadingIndicators() {

        }

    @Override
    public void onLoaderReset(android.content.Loader<Object> loader) {

    }
}
