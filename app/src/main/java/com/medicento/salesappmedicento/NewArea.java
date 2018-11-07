package com.medicento.salesappmedicento;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

public class NewArea extends AppCompatActivity {
    EditText area,city,state,pincode;
    String areas,citys,states,pincodes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_area);
        area = (EditText) findViewById(R.id.new_area_edit_tv);
        city = (EditText) findViewById(R.id.new_city_edit_tv);
        state = (EditText) findViewById(R.id.new_state_edit_tv);
        pincode = (EditText) findViewById(R.id.new_pincode_edit_tv);
    }
    public void submitArea(View view) {
        areas = area.getText().toString();
        citys = city.getText().toString();
        states = state.getText().toString();
        pincodes = pincode.getText().toString();
        area.onEditorAction(EditorInfo.IME_ACTION_DONE);
    }
}
