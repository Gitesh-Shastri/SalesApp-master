package com.medicento.salesappmedicento.helperData;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medicento.salesappmedicento.R;

import java.util.ArrayList;

public class OrderedMedicineAdapter extends RecyclerView.Adapter<OrderedMedicineAdapter.MedicineViewHolder> {

    public ArrayList<OrderedMedicine> mMedicinesList;
    public float mOverallCost = 0;
    OverallCostChangeListener mOverallCostChangeListener;

    public OrderedMedicineAdapter(ArrayList<OrderedMedicine> list) {
        mMedicinesList = list;
    }

    public void setOverallCostChangeListener(OverallCostChangeListener listener) {
        this.mOverallCostChangeListener = listener;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_order_medicine, parent, false);
        MedicineViewHolder viewHolder = new MedicineViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        holder.bind(position);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mMedicinesList.size();
    }

    public void add(OrderedMedicine medicine) {
        for (OrderedMedicine med: mMedicinesList) {
            if (medicine.getMedicineName().equals(med.getMedicineName())) {
                if (medicine.getMedicineCompany().equals(med.getMedicineCompany())) {
                    int qty = med.getQty();
                    qty++;
                    float cost = med.getRate()*qty;
                    med.setQty(qty);
                    med.setCost(cost);
                    mOverallCost += medicine.getCost();
                    notifyDataSetChanged();
                    return;
                }
            }
        }
        mMedicinesList.add(0, medicine);
        mOverallCost += medicine.getCost();
        notifyItemInserted(0);
    }

    public void remove(int pos) {
        float cost = mMedicinesList.get(pos).getCost();
        mOverallCost -= cost;
        notifyDataSetChanged();
        notifyDataSetChanged();
        if (mOverallCostChangeListener != null) mOverallCostChangeListener.onCostChanged(mOverallCost);
        mMedicinesList.remove(pos);
        notifyDataSetChanged();
    }

    public void reset() {
        mMedicinesList.clear();
        mOverallCost = 0;
        notifyDataSetChanged();
    }

    public ArrayList<OrderedMedicine> getList() {
        return mMedicinesList;
    }

    public class MedicineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView MedName, MedCompany, MedQty, MedCost, incQty, decQty, MedRate;

        public MedicineViewHolder(View itemView) {
            super(itemView);
            MedName = itemView.findViewById(R.id.medicine_name);
            MedCompany = itemView.findViewById(R.id.medicine_company);
            MedRate = itemView.findViewById(R.id.medicine_rate);
            MedQty = itemView.findViewById(R.id.medicine_qty);
            MedCost = itemView.findViewById(R.id.medicine_cost);
            incQty = itemView.findViewById(R.id.inc_qty);
            incQty.setOnClickListener(this);
            decQty = itemView.findViewById(R.id.dec_qty);
            decQty.setOnClickListener(this);
        }

        public void bind(int pos) {
            OrderedMedicine medicine = mMedicinesList.get(pos);
            MedName.setText(medicine.getMedicineName());
            MedCompany.setText(medicine.getMedicineCompany());
            MedRate.setText("Billing Rate : ");
            MedCost.setText(medicine.getCost() + "");
            MedQty.setText(medicine.getQty() + "");
        }

        @Override
        public void onClick(View v) {
           int pos = getAdapterPosition();
           if (v.getId() == R.id.inc_qty) {
               for (int i = 0; i < mMedicinesList.size(); i++) {
                   OrderedMedicine med = mMedicinesList.get(i);
                   if (med.getMedicineName().equals(mMedicinesList.get(pos).getMedicineName())) {
                       int qty = med.getQty();
                       qty++;
                       float cost = med.getRate()*qty;
                       med.setQty(qty);
                       med.setCost(cost);
                       mMedicinesList.set(i, med);
                       mOverallCost += med.getRate();
                       if (mOverallCostChangeListener != null) mOverallCostChangeListener.onCostChanged(mOverallCost);
                       notifyDataSetChanged();
                       return;
                   }
               }
           } else {
               for (int i = 0; i < mMedicinesList.size(); i++) {
                   OrderedMedicine med = mMedicinesList.get(i);
                   if (med.getMedicineName().equals(mMedicinesList.get(pos).getMedicineName())) {
                       int qty = med.getQty();
                       qty--;
                       mOverallCost -= med.getRate();
                       if (mOverallCostChangeListener != null) mOverallCostChangeListener.onCostChanged(mOverallCost);
                       if (qty == 0) {
                           mMedicinesList.remove(i);
                           notifyDataSetChanged();
                           return;
                       }

                       float cost = med.getRate()*qty;
                       med.setQty(qty);
                       med.setCost(cost);
                       mMedicinesList.set(i, med);
                       notifyDataSetChanged();
                       return;
                   }
               }
           }
        }
    }


    public float getOverallCost() {
        return mOverallCost;
    }

    public interface OverallCostChangeListener {
        void onCostChanged(float newCost);
    }
}