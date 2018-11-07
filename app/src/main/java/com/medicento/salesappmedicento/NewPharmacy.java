package com.medicento.salesappmedicento;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class NewPharmacy extends AppCompatActivity {
    TextView area,city,state,pincode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pharmacy);
        area = (TextView) findViewById(R.id.area);
        city = (TextView) findViewById(R.id.city);
        state = (TextView) findViewById(R.id.state);
        pincode = (TextView) findViewById(R.id.pincode);
        Intent intent = getIntent();
        area.setText(intent.getStringExtra("area"));
        city.setText(intent.getStringExtra("city"));
        state.setText(intent.getStringExtra("state"));
        pincode.setText(intent.getStringExtra("pincode"));
    }
}