package com.medicento.salesappmedicento.helperData;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.medicento.salesappmedicento.R;
import com.medicento.salesappmedicento.networking.data.SalesPharmacy;

import java.util.ArrayList;
import java.util.List;

public class PhramaSpinnerCustomAdapter extends ArrayAdapter<SalesPharmacy> {

    Context mContext;
    int mResource;
    ArrayList<SalesPharmacy> mList;
    LayoutInflater mInflater;

    public PhramaSpinnerCustomAdapter(@NonNull Context context, int resource, @NonNull List<SalesPharmacy> objects) {
        super(context, resource, objects);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        mList = (ArrayList<SalesPharmacy>) objects;
    }

    public void changeList(ArrayList<SalesPharmacy> list) {
        mList = list;
        notifyDataSetChanged();
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.spinner_item_layout, parent, false);
        }
        TextView mItemNameTv = convertView.findViewById(R.id.spinner_item_name);
        mItemNameTv.setText(mList.get(position).getPharmacyName());
        return mItemNameTv;
    }

    public int getItemPosition(SalesPharmacy salesPharmacy) {
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getId().equals(salesPharmacy.getId())) {
                return i;
            }
        }
        return -1;
    }
}
