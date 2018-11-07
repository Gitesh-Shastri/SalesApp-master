package com.medicento.salesappmedicento.fragments;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.medicento.salesappmedicento.PlaceOrdersActivity;
import com.medicento.salesappmedicento.R;
import com.medicento.salesappmedicento.helperData.AreaSpinnerCustomAdapter;
import com.medicento.salesappmedicento.helperData.Constants;
import com.medicento.salesappmedicento.helperData.OrderedMedicine;
import com.medicento.salesappmedicento.helperData.OrderedMedicineAdapter;
import com.medicento.salesappmedicento.helperData.PhramaSpinnerCustomAdapter;
import com.medicento.salesappmedicento.networking.SalesDataLoader;
import com.medicento.salesappmedicento.networking.data.Medicine;
import com.medicento.salesappmedicento.networking.data.SalesArea;
import com.medicento.salesappmedicento.networking.data.SalesPharmacy;

import java.util.ArrayList;


public class PlaceOrderFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Object>,
        OrderedMedicineAdapter.OverallCostChangeListener  {

    SharedPreferences sharedPreferences;
    AreaSpinnerCustomAdapter mAreaAdapter;
    PhramaSpinnerCustomAdapter mPharmaAdapter;
    ArrayAdapter<String> mMedicineAdapter;
    RecyclerView mOrderedMedicinesListView;
    public static OrderedMedicineAdapter mOrderedMedicineAdapter;
    AutoCompleteTextView mSelectMedicineTv;
    InputMethodManager im;
    RelativeLayout mCostLayout;
    TextView mTotalTv;
    Toolbar mtoolbar;
    LinearLayout mSpinnerLayout;
    Button add;
    Animation mAnimation;
    ImageView mMedicentoLogo;
    Spinner mSelectAreaSpinner, mSelectPharmacySpinner;
    Context mContext;
    PlaceOrderFragment placeOrderFragment;

    SalesPharmacy mSelectedPharmacy;
    boolean confirmStatus = false;
    OnPlaceOrderChangeListener mPlaceOrderChangeListener;
    LinearLayout ll;
    public PlaceOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = inflater.getContext();
        View view = inflater.inflate(R.layout.fragment_place_order, container, false);

        add = view.findViewById(R.id.addmore);
        mSpinnerLayout = view.findViewById(R.id.spinner_layout);
        mCostLayout = view.findViewById(R.id.cost_layout);
        mSelectAreaSpinner = view.findViewById(R.id.area_edit_tv);
        mSelectPharmacySpinner = view.findViewById(R.id.pharmacy_edit_tv);
        ll = view.findViewById(R.id.linear);
        mTotalTv = view.findViewById(R.id.total_cost);
        mMedicentoLogo = view.findViewById(R.id.medicento_logo);
        mSelectMedicineTv = view.findViewById(R.id.medicine_edit_tv);
        mOrderedMedicinesListView = view.findViewById(R.id.ordered_medicines_list);
        mOrderedMedicinesListView.setLayoutManager(new LinearLayoutManager(mContext));
        mOrderedMedicinesListView.setHasFixedSize(true);

        im = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ll.setVisibility(View.GONE);
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

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isInOrderMode();
                transformIntoPlaceOrder();

            }
        });

        mOrderedMedicineAdapter = new OrderedMedicineAdapter(new ArrayList<OrderedMedicine>());
        mOrderedMedicineAdapter.setOverallCostChangeListener(this);
        mOrderedMedicinesListView.setAdapter(mOrderedMedicineAdapter);

        mSelectMedicineTv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectMedicineTv.setText("");
                im.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                Medicine medicine = null;
                for (Medicine med : PlaceOrdersActivity.mMedicineDataList) {
                    if (med.getMedicentoName().equals(mMedicineAdapter.getItem(position))) {
                        medicine = med;
                        break;
                    }
                }
                mOrderedMedicineAdapter.add(new OrderedMedicine(medicine.getMedicentoName(),
                        medicine.getCompanyName(),
                        1,
                        medicine.getPrice(),
                        medicine.getPrice(),
                        medicine.getCode()));
                mOrderedMedicinesListView.smoothScrollToPosition(0);
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = (int) viewHolder.itemView.getTag();
                mOrderedMedicineAdapter.remove(pos);
            }
        }).attachToRecyclerView(mOrderedMedicinesListView);
        mAnimation = new AlphaAnimation(1, 0);
        mAnimation.setDuration(200);
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.REVERSE);


        return view;
    }


    public void setAreaPharmaSelectListener (OnPlaceOrderChangeListener listener) {
        mPlaceOrderChangeListener = listener;
    }

    public void setAreaAdapter(AreaSpinnerCustomAdapter adapter) {
        mAreaAdapter = adapter;
        mSelectAreaSpinner.setAdapter(mAreaAdapter);
    }

    public void setPharmaAdapter(PhramaSpinnerCustomAdapter adapter) {
        mPharmaAdapter = adapter;
        mSelectPharmacySpinner.setAdapter(mPharmaAdapter);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setMedicineAdapter(ArrayAdapter<String> medicineAdapter) {
        mSelectMedicineTv.setEnabled(true);
        mMedicineAdapter = medicineAdapter;
        mSelectMedicineTv.setAdapter(mMedicineAdapter);
        mSelectMedicineTv.setThreshold(0);
    }

    public void transformIntoConfirmOrder() {
        if (canOrderBeConfirmed()) {
            mSelectMedicineTv.setVisibility(View.GONE);
            mSpinnerLayout.setVisibility(View.GONE);
            ll.setVisibility(View.VISIBLE);
            mCostLayout.setVisibility(View.VISIBLE);
            mTotalTv.setText(String.valueOf(mOrderedMedicineAdapter.getOverallCost()));
            confirmStatus = true;
        }
    }


    /*This method confirms that the any pharmacy is selected
     *and that we have ordered something as well
     */
    private boolean canOrderBeConfirmed() {
        if (mSelectPharmacySpinner.getSelectedItem() == null) {
            Toast.makeText(mContext, "No Pharmacy selected", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mOrderedMedicineAdapter.mMedicinesList.size() == 0) {
            Toast.makeText(mContext, "Please select some medicines first", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public boolean isInOrderMode() {
        if (confirmStatus) {
            confirmStatus = false;
            return confirmStatus;
        } else {
            return true;
        }
    }

    public void transformIntoPlaceOrder() {
        mSpinnerLayout.setVisibility(View.VISIBLE);
        mSelectMedicineTv.setVisibility(View.VISIBLE);
        ll.setVisibility(View.GONE);
        mCostLayout.setVisibility(View.GONE);
    }

    public void placeOrder() {
        getActivity().getLoaderManager().initLoader(Constants.PLACE_ORDER_LOADER_ID , null, this);
    }

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        mMedicentoLogo.setVisibility(View.VISIBLE);
        mMedicentoLogo.startAnimation(mAnimation);
        Log.i("orderPlaced", mOrderedMedicineAdapter.getList().get(0).getCode());
        return new SalesDataLoader(mContext, Constants.PLACE_ORDER_URL, getString(R.string.place_order_action), mOrderedMedicineAdapter.getList(), mSelectedPharmacy.getId());
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        mPlaceOrderChangeListener.onOrderPlaced(mOrderedMedicineAdapter, (String[]) data);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onCostChanged(float newCost) {
        mTotalTv.setText(newCost + "");
    }

    public interface OnPlaceOrderChangeListener {
        void onAreaSelected(SalesArea salesArea);
        void onPharmaSelected(SalesPharmacy salesPharmacy);
        void onOrderPlaced(OrderedMedicineAdapter adapter, String[] output);
    }
}