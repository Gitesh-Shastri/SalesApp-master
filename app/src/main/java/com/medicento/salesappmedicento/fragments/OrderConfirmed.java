package com.medicento.salesappmedicento.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.medicento.salesappmedicento.PlaceOrdersActivity;
import com.medicento.salesappmedicento.R;
import com.medicento.salesappmedicento.helperData.OrderedMedicineAdapter;
import com.medicento.salesappmedicento.networking.data.SalesPharmacy;

public class OrderConfirmed extends Fragment {

    TextView mOrderIdTv, mSelectedPharmacyTv, mDeliveryDateTv, mTotalCostTv;
    SalesPharmacy mSelectedPharmacy;
    RecyclerView mRecyclerView;
    BottomSheetBehavior mBottomSheetBehavior;
    OrderedMedicineAdapter mAdapter;
    Button mShareBtn,mNewOrder;
    ListView listview = null;
    PlaceOrderFragment placeOrderFragment;

    String mOrderId, mDeliveryDate;
    public OrderConfirmed() {
        // Required empty public constructor
    }


    public void setSelectedPharmacy(SalesPharmacy salesPharmacy) {
        mSelectedPharmacy = salesPharmacy;
    }

    public void setAdapter(OrderedMedicineAdapter adapter) {
        mAdapter = adapter;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_confirmed, container, false);
        mOrderIdTv = view.findViewById(R.id.order_id_tv);
        mSelectedPharmacyTv = view.findViewById(R.id.selected_pharmacy_tv);
        mDeliveryDateTv = view.findViewById(R.id.delivery_date_tv);
        mTotalCostTv = view.findViewById(R.id.total_cost_tv);
        View bottomView = view.findViewById(R.id.bottom_sheet);


        mRecyclerView = view.findViewById(R.id.order_confirmed_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        mOrderIdTv.setText(mOrderId);
        mDeliveryDateTv.setText(mDeliveryDate);
        mSelectedPharmacyTv.setText(mSelectedPharmacy.getPharmacyName());
        mTotalCostTv.setText(String.valueOf(mAdapter.getOverallCost()));
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomView);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        mBottomSheetBehavior.setPeekHeight(100);
        final String orderShareDetails = "*Medicento Sales Order Summary*" +
                "\n*Order id*: " + mOrderId +
                "\n*Total Cost*: " + "Rs. " + "*" + mAdapter.getOverallCost() + "*" +
                "\n*Delivery schedule*: " + mDeliveryDate;

        mNewOrder = view.findViewById(R.id.neworder);
        mNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), PlaceOrdersActivity.class);
                getActivity().finish();
                startActivity(i);

            }
        });

        mShareBtn = view.findViewById(R.id.share_order_btn);
        mShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(Intent.EXTRA_SUBJECT, "Medicento Order details");
                intent.putExtra(Intent.EXTRA_TEXT, orderShareDetails);
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                startActivity(intent);
            }
        });

        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
     }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setIdAndDeliveryDate(String[] idAndDeliveryDate) {
        mOrderId = idAndDeliveryDate[0];
        mDeliveryDate = idAndDeliveryDate[1];
    }
}