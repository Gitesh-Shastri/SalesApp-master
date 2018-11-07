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
import com.medicento.salesappmedicento.networking.data.SalesArea;

import java.util.ArrayList;
import java.util.List;

public class AreaSpinnerCustomAdapter extends ArrayAdapter<SalesArea> {

    Context mContext;
    int mResource;
    ArrayList<SalesArea> mList;
    LayoutInflater mInflater;

    public AreaSpinnerCustomAdapter(@NonNull Context context, int resource, @NonNull List<SalesArea> objects) {
        super(context, resource, objects);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mList = (ArrayList<SalesArea>) objects;
        mResource = resource;
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
            convertView = mInflater.inflate(mResource, parent, false);
        }
        TextView itemNameTv = convertView.findViewById(R.id.spinner_item_name);
        itemNameTv.setText(mList.get(position).getAreaName());
        return convertView;
    }

    public int getItemPosition(SalesArea salesArea) {
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getId().equals(salesArea.getId())) {
                return i;
            }
        }
        return -1;
     }
}
