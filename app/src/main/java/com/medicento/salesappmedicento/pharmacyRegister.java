package com.medicento.salesappmedicento;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class pharmacyRegister extends AppCompatActivity {
    EditText pharmacy,add,id,no,licenseNo,gstNo;
    String pharmacys,adds,ids,nos,licenseNos,gstNos;
    Spinner state,city,area;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_register);
        pharmacy = findViewById(R.id.pharname);
        add = findViewById(R.id.address);
        id = findViewById(R.id.email);
        no = findViewById(R.id.number);
        licenseNo = findViewById(R.id.licenseno);
        gstNo = findViewById(R.id.gstno);
    }
    public void Submit(View view)
    {
        pharmacys = pharmacy.getText().toString();
        adds = add.getText().toString();
        ids = id.getText().toString();
        nos = no.getText().toString();
        licenseNos = licenseNo.getText().toString();
        gstNos = gstNo.getText().toString();
    }
}
