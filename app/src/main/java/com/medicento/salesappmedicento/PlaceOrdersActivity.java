package com.medicento.salesappmedicento;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.medicento.salesappmedicento.fragments.OrderConfirmed;
import com.medicento.salesappmedicento.fragments.PlaceOrderFragment;
import com.medicento.salesappmedicento.helperData.AreaSpinnerCustomAdapter;
import com.medicento.salesappmedicento.helperData.Constants;
import com.medicento.salesappmedicento.helperData.OrderedMedicineAdapter;
import com.medicento.salesappmedicento.helperData.PhramaSpinnerCustomAdapter;
import com.medicento.salesappmedicento.helperData.SavedData;
import com.medicento.salesappmedicento.networking.SalesDataLoader;
import com.medicento.salesappmedicento.networking.data.Medicine;
import com.medicento.salesappmedicento.networking.data.SalesArea;
import com.medicento.salesappmedicento.networking.data.SalesPerson;
import com.medicento.salesappmedicento.networking.data.SalesPharmacy;
import com.medicento.salesappmedicento.networking.util.SalesDataExtractor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PlaceOrdersActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,PlaceOrderFragment.OnPlaceOrderChangeListener {

    public static ArrayList<Medicine> mMedicineDataList;
    ArrayList<SalesArea> mSalesAreaDetails;
    ArrayList<SalesPharmacy> mSalesPharmacyDetails;

    public static int code;
    FragmentManager mFragmentManager;
    PlaceOrderFragment mPlaceOrder;
    CoordinatorLayout coordinatorLayout;
    OrderConfirmed mOrderConfirmed;


    ImageView mNoNetworkImage;
    InputMethodManager im;



    NavigationView mNavigationView;
    OrderedMedicineAdapter mOrderedMedicineAdapter;
    ProgressBar mProgressBar;

    Spinner mSelectAreaSpinner, mSelectPharmacySpinner;

    SharedPreferences mSharedPreferences;
    Snackbar mSnackbar;

    TextView mNoNetworkInfo;
    Toolbar mToolbar;
    View mLoadingWaitView;

    int Count;

    boolean pharmaLoadFlag, medicineLoadFlag;

    SalesPharmacy mSelectedPharmacy;
    SalesArea mSelectedArea;

    String[] salesPersonDetailsLabel = {
            "Total Sales        :",
            "No of Orders     :",
            "Returns              :",
            "Earnings            :"
    };
    String strcode,versionUpdate;
    FirebaseAuth mAuth;
    boolean calledOnAreaLoadingFinished, calledOnPharmacyLoadingFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_orders);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mProgressBar = findViewById(R.id.area_pharma_fetch_progress);
        mLoadingWaitView = findViewById(R.id.loading_wait_view);
        coordinatorLayout = findViewById(R.id.coordinator_layout);
        mSnackbar = Snackbar.make(coordinatorLayout, "Please wait while the data is being loaded...", Snackbar.LENGTH_INDEFINITE);
        //mNoNetworkImage = findViewById(R.id.no_network_icon);
        //mNoNetworkInfo = findViewById(R.id.no_network_info);

        addSalesPersonDetailsToNavDrawer();

        mAuth = FirebaseAuth.getInstance();
        Count = 0;
        mPlaceOrder = new PlaceOrderFragment();
        mPlaceOrder.setAreaPharmaSelectListener(this);

        mFragmentManager = getSupportFragmentManager();

        mFragmentManager.beginTransaction().add(R.id.main_container, mPlaceOrder).commit();
        mSnackbar.show();
        mProgressBar.setVisibility(View.VISIBLE);
        Gson gson = new Gson();
        String json = mSharedPreferences.getString("savedArea", null);
        String jsonP = mSharedPreferences.getString("savedPharma", null);
        String jsonM = mSharedPreferences.getString("savedMedicine", null);
        Type type = new TypeToken<ArrayList<SalesArea>>() {}.getType();
        Type type1 = new TypeToken<ArrayList<SalesPharmacy>>() {}.getType();
        Type type2 = new TypeToken<ArrayList<Medicine>>() {}.getType();
        mSalesAreaDetails = gson.fromJson(json, type);
        mSalesPharmacyDetails = gson.fromJson(jsonP, type1);
        mMedicineDataList = gson.fromJson(jsonM, type2);
        new GetNames().execute();
        new GetMedicine().execute();
    }

    /*This method adds totalSales,orders, returns, earnings to
     *the nav menu of the app
     */
    private void addSalesPersonDetailsToNavDrawer() {
        View headerView = mNavigationView.getHeaderView(0);
        TextView navHeaderSalesmanName = headerView.findViewById(R.id.username_header);
        TextView navHeaderSalesmanEmail = headerView.findViewById(R.id.user_email_header);

        navHeaderSalesmanName.setText(mSharedPreferences.getString(Constants.SALE_PERSON_NAME, ""));
        navHeaderSalesmanEmail.setText(mSharedPreferences.getString(Constants.SALE_PERSON_EMAIL, ""));

        Menu menu = mNavigationView.getMenu();
        if (mSharedPreferences.getFloat(Constants.SALE_PERSON_TOTAL_SALES, -1) == -1) {
            return;
        }
        menu.getItem(1).setTitle(salesPersonDetailsLabel[0] + mSharedPreferences.getFloat(Constants.SALE_PERSON_TOTAL_SALES, -1));
        menu.getItem(2).setTitle(salesPersonDetailsLabel[1] + mSharedPreferences.getInt(Constants.SALE_PERSON_NO_OF_ORDERS, 0));
        menu.getItem(3).setTitle(salesPersonDetailsLabel[2] + mSharedPreferences.getInt(Constants.SALE_PERSON_RETURNS, -1));
        menu.getItem(4).setTitle(salesPersonDetailsLabel[3] + mSharedPreferences.getFloat(Constants.SALE_PERSON_EARNINGS, -1));
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Welcome!!", Toast.LENGTH_SHORT).show();
                mNavigationView = findViewById(R.id.nav_view);
                addSalesPersonDetailsToNavDrawer();
            } else {
                Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.place_orders, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_proceed) {
            if (mPlaceOrder.isInOrderMode()) {
                mPlaceOrder.transformIntoConfirmOrder();
                mToolbar.setTitle("Confirm Order!");
                return true;
            }else {
                mPlaceOrder.placeOrder();
                mToolbar.setTitle("Placing Order...");
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetNames extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            JsonParser sh = new JsonParser();
            if(mSalesPharmacyDetails == null || mSalesAreaDetails == null) {
                    String url = "https://medicento-api.herokuapp.com/pharma/area/area";
                    // Making a request to url and getting response
                    String jsonStr = sh.makeServiceCall(url);
                    Log.e("Gitesh", "Response from url: " + jsonStr);
                    if (jsonStr != null) {
                        try {
                            JSONObject baseObject = new JSONObject(jsonStr);
                            if(mSalesAreaDetails == null) {
                                mSalesAreaDetails = new ArrayList<>();
                                JSONArray areasArray = baseObject.getJSONArray("areas");
                                for (int i = 0; i < areasArray.length(); i++) {
                                    JSONObject areaObject = areasArray.optJSONObject(i);
                                    mSalesAreaDetails.add(new SalesArea(
                                            areaObject.getString("area_name"),
                                            areaObject.getString("area_city"),
                                            areaObject.getString("area_state"),
                                            areaObject.getInt("area_pincode"),
                                            areaObject.getString("_id")
                                    ));
                                }
                            }
                            if(mSalesPharmacyDetails == null) {
                                mSalesPharmacyDetails = new ArrayList<>();
                                JSONArray pharmaArray = baseObject.getJSONArray("pharmas");
                                for (int i = 0; i < pharmaArray.length(); i++) {
                                    JSONObject areaObject = pharmaArray.optJSONObject(i);
                                    mSalesPharmacyDetails.add(new SalesPharmacy(
                                            areaObject.getString("pharma_name"),
                                            areaObject.getString("pharma_address"),
                                            areaObject.getString("_id"),
                                            areaObject.getString("area")
                                    ));
                                }
                            }
                        } catch (final JSONException e) {
                            Toast.makeText(PlaceOrdersActivity.this, "Json parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Gson gson = new Gson();
            String json = gson.toJson(mSalesAreaDetails);
            String json1 = gson.toJson(mSalesPharmacyDetails);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString("savedArea", json);
            editor.putString("savedPharma", json1);
            editor.apply();
            ArrayList<SalesPharmacy> pharmacyList = new ArrayList<>();
            for (SalesPharmacy salesPharmacy : mSalesPharmacyDetails) {
                if (mSalesAreaDetails.get(0).getId().equals(salesPharmacy.getAreaId())) {
                    pharmacyList.add(salesPharmacy);
                }
            }
            mPlaceOrder.setAreaAdapter(new AreaSpinnerCustomAdapter(PlaceOrdersActivity.this, R.layout.spinner_item_layout, mSalesAreaDetails));
            mPlaceOrder.setPharmaAdapter(new PhramaSpinnerCustomAdapter(PlaceOrdersActivity.this, R.layout.spinner_item_layout, pharmacyList));
        }
    }

    private class GetMedicine extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            JsonParser sh = new JsonParser();

            String url = Constants.MEDICINE_DATA_URL;
            if(mMedicineDataList == null) {
                mMedicineDataList = new ArrayList<>();
                // Making a request to url and getting response
                String jsonStr = sh.makeServiceCall(url);
                Log.e("Gitesh", "Response from url: " + jsonStr);
                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);

                        // Getting JSON Array node
                        JSONArray medicine = jsonObj.getJSONArray("products");
                        mMedicineDataList = new ArrayList<>();
                        // looping through All Contacts
                        for (int i = 0; i < medicine.length(); i++) {
                            JSONObject c = medicine.getJSONObject(i);
                            mMedicineDataList.add(new Medicine(
                                    c.getString("medicento_name"),
                                    c.getString("company_name"),
                                    c.getInt("price"),
                                    c.getString("_id"),
                                    c.getString("item_code")
                            ));
                        }
                    } catch (final JSONException e) {
                        Toast.makeText(PlaceOrdersActivity.this, "Json parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Gson gson = new Gson();
                ArrayList<String> medicineList = new ArrayList<>();
                for (Medicine medicine : mMedicineDataList) {
                    medicineList.add(medicine.getMedicentoName());
                }
                mPlaceOrder.setMedicineAdapter(new ArrayAdapter<String>(PlaceOrdersActivity.this, android.R.layout.simple_list_item_1, medicineList));
            String json = gson.toJson(mMedicineDataList);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString("savedMedicine", json);
            editor.apply();
            mProgressBar.setVisibility(View.GONE);
            mLoadingWaitView.setVisibility(View.GONE);
            mSnackbar.dismiss();
        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sign_out) {
            mAuth.signOut();
            clearUserDetails();
            Intent intent = new Intent(this, SignInActivity.class);
            startActivityForResult(intent, Constants.RC_SIGN_IN);
        } else if (id == R.id.total_sales) {
            return true;
        } else if (id == R.id.no_of_orders) {
            return true;
        } else if (id == R.id.returns) {
            return true;
        } else if (id == R.id.add_new_pharmacy) {
            Intent intent = new Intent(this, NewPharmacy.class);
            startActivity(intent);
        } else if(id == R.id.recentorders){
            Intent intent = new Intent(this,Recentorder.class);
            startActivity(intent);
        } else if (id == R.id.add_new_area) {
            Intent intent = new Intent(this, NewArea.class);
            startActivity(intent);
        }else if (id == R.id.about_me) {
            Intent intent = new Intent(this, SalesPersonDetails.class);
            for (SalesArea salesArea : mSalesAreaDetails) {
                if (mSharedPreferences.getString(Constants.SALE_PERSON_ALLOCATED_AREA_ID, "").equals(salesArea.getId())) {
                    intent.putExtra(Constants.SALE_PERSON_ALLOCATED_AREA_NAME, salesArea.getAreaName());
                }
            }
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*This method deletes all the details of the
     *sales person when he sign out of the app
     */

    private void clearUserDetails() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.SALE_PERSON_EMAIL, "");
        editor.putString(Constants.SALE_PERSON_NAME, "");
        editor.putFloat(Constants.SALE_PERSON_TOTAL_SALES, 0);
        editor.putInt(Constants.SALE_PERSON_RETURNS, 0);
        editor.putFloat(Constants.SALE_PERSON_EARNINGS, 0);
        editor.putString(Constants.SALE_PERSON_ID, "");
        editor.putString(Constants.SALE_PERSON_ALLOCATED_AREA_ID, "");
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (mPlaceOrder.isInOrderMode()) {
            super.onBackPressed();
        } else {
            mToolbar.setTitle("Place Order");
            mPlaceOrder.transformIntoPlaceOrder();
        }
    }

    private void repopulateThePharmacyList() {
//        mOrderedMedicineAdapter.reset();
        ArrayList<SalesPharmacy> pharmacyList = new ArrayList<>();
        if (mSalesPharmacyDetails != null) {
            for (SalesPharmacy salesPharmacy : mSalesPharmacyDetails) {
                if (mSelectedArea.getId().equals(salesPharmacy.getAreaId())) {
                    pharmacyList.add(salesPharmacy);
                }
            }
            if (pharmacyList.size() == 0) {
                Toast.makeText(this, "No Pharmacy available for this area", Toast.LENGTH_SHORT).show();
            }
            mPlaceOrder.setPharmaAdapter(new PhramaSpinnerCustomAdapter(this, R.layout.spinner_item_layout, pharmacyList));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        final int[] count1 = new int[1];
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://medicento-api.herokuapp.com/pharma/updateSalesApp";
        StringRequest str = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("Code1", response.toString());
                            strcode = response.toString();
                            JSONObject spo = new JSONObject(response.toString());
                            JSONArray version = spo.getJSONArray("Version");
                            for(int i=0;i<version.length();i++){
                                JSONObject v = version.getJSONObject(i);
                                versionUpdate = v.getString("version");
                            }
                            String versionName = BuildConfig.VERSION_NAME;
                            if(versionUpdate.equals(versionName)) {

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(PlaceOrdersActivity.this);
                                builder.setTitle("update Available");
                                builder.setIcon(R.mipmap.ic_launcher_new);
                                builder.setCancelable(false);
                                builder.setMessage("New Version Available")
                                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                final String appName = getPackageName();
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.medicento.retailerappmedi")));
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                            code = spo.getInt("code");
                            count1[0] = spo.getInt("count");
                            int count = mSharedPreferences.getInt("count", 0);
                            if (code == 101 && count <= spo.getInt("count")) {
                                SharedPreferences.Editor editor = mSharedPreferences.edit();
                                editor.putInt("count", count1[0] + 1);
                                editor.apply();
                                new GetNames().execute();
                                Toast.makeText(PlaceOrdersActivity.this, "List Updated", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("error_coce", e.getMessage().toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(str);
    mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (mSharedPreferences.getString(Constants.SALE_PERSON_NAME, "").isEmpty()) {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivityForResult(intent, Constants.RC_SIGN_IN);
        }
    }

    @Override
    protected void onPause() {
        SavedData.saveAdapter(mOrderedMedicineAdapter);
        SavedData.saveAreaAndPharmacy(mSelectedArea, mSelectedPharmacy);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        SavedData.mOrderedMedicineAdapter = null;
        super.onDestroy();
    }

    @Override
    public void onAreaSelected(SalesArea salesArea) {
        mSelectedArea = salesArea;
        repopulateThePharmacyList();
    }

    @Override
    public void onPharmaSelected(SalesPharmacy salesPharmacy) {
        mSelectedPharmacy = salesPharmacy;
    }

    @Override
    public void onOrderPlaced(OrderedMedicineAdapter adapter, String[] output) {
        mOrderConfirmed = new OrderConfirmed();
        mOrderConfirmed.setIdAndDeliveryDate(output);
        mOrderConfirmed.setAdapter(adapter);
        mOrderConfirmed.setSelectedPharmacy(mSelectedPharmacy);
        mToolbar.setTitle("Order Placed!");
        mFragmentManager.beginTransaction().replace(R.id.main_container, mOrderConfirmed).commit();
    }
}