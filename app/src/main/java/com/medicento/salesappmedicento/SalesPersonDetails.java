package com.medicento.salesappmedicento;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.medicento.salesappmedicento.helperData.Constants;

public class SalesPersonDetails extends AppCompatActivity {

    SharedPreferences mSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_person_details);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        TextView name = findViewById(R.id.about_me_name);
        TextView allocatedArea = findViewById(R.id.about_me_allocated_area);
        name.setText(mSharedPreferences.getString(Constants.SALE_PERSON_NAME, ""));
        Bundle b = getIntent().getExtras();
        if (b != null) {
            allocatedArea.setText(b.getString(Constants.SALE_PERSON_ALLOCATED_AREA_NAME, ""));
        } else {
            allocatedArea.setText("No area allocated");
        }
    }
}

