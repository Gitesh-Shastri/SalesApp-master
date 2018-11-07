package com.medicento.salesappmedicento;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medicento.salesappmedicento.networking.data.RecentOrder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Recentorder extends AppCompatActivity  {
    ProgressDialog progressDialog;
    private static ArrayList<RecentOrder> mRecentOrder;
    TextView deli_id,deli_date;
    RelativeLayout rl,r2,r3;
    Button b1,b2,b3;
    SharedPreferences mSharedPreferences;
    String mOrderId, mDeliveryDate;
    private static String url = "https://medicento-api.herokuapp.com/product/recent_order/";

    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recent_order);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //Realtive Layout
        rl = findViewById(R.id.first);
        r2 = findViewById(R.id.second);
        r3 = findViewById(R.id.third);
        //Button
        b1 = findViewById(R.id.r1);
        b2 = findViewById(R.id.r2);
        b3 = findViewById(R.id.r3);

        deli_id = findViewById(R.id.order_id_deli);
        deli_date = findViewById(R.id.date_del);

        deli_id.setText(mOrderId);
        deli_date.setText(mDeliveryDate);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Recentorder.this);
                View view1 = getLayoutInflater().inflate(R.layout.fragment_order_details,null);
                builder.setView(view1);
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        });
        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Recentorder.this);
                View view1 = getLayoutInflater().inflate(R.layout.fragment_order_details,null);
                builder.setView(view1);
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        });
        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Recentorder.this);
                View view1 = getLayoutInflater().inflate(R.layout.fragment_order_details,null);
                builder.setView(view1);
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        });

    }
    public class GetOrder extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            JsonParser sh = new JsonParser();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);
            Log.e("Gitesh", "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    JSONArray jsonArray = jsonObj.getJSONArray("orders");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject order = jsonArray.getJSONObject(i);
                        mRecentOrder.add(new RecentOrder(order.getString("_id"),
                                order.getString("created_at"),
                                order.getInt("grand_total")
                        ));

                    }
                } catch (final JSONException e) {
                    Toast.makeText(Recentorder.this, "Json parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Gitesh", "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e("Gitesh", "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

}
