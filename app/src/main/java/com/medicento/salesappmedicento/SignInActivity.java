package com.medicento.salesappmedicento;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.medicento.salesappmedicento.helperData.Constants;
import com.medicento.salesappmedicento.networking.SalesDataLoader;
import com.medicento.salesappmedicento.networking.data.SalesPerson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SignInActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Object> {

    EditText mEmailEditTv;
    TextView forget;
    String mUserCode;

    SignInButton mGoogleBtn;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 2;
    FirebaseAuth mAuth;

    SalesPerson sp;
    ProgressBar mProgressBar;
    Animation mAnimation;
    ImageView mLogo;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mLogo = findViewById(R.id.medicento_logo);
        forget = findViewById(R.id.forgetpassword);
        Button acc = findViewById(R.id.createaccount);
        mEmailEditTv = findViewById(R.id.email_edit_tv);
        Button btn = findViewById(R.id.sign_in_btn);
        mProgressBar = findViewById(R.id.sign_in_progress);
        mProgressBar.setVisibility(View.GONE);

        mGoogleBtn = findViewById(R.id.google_sign_in);
        mGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserCode = mEmailEditTv.getText().toString();
                if(!amIConnect()) {
                    return;
                }
                if (isInputEmpty(mUserCode)) {
                    Toast.makeText(SignInActivity.this, "Please enter data for sign in!", Toast.LENGTH_SHORT).show();
                    return;
                }
                mProgressBar.setVisibility(View.VISIBLE);
                getLoaderManager().initLoader(Constants.LOG_IN_LOADER_ID, null, SignInActivity.this);
            }
        });


        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignInActivity.this,recover.class);
                SignInActivity.this.finish();
                startActivity(i);
            }
        });
        mAnimation = new AlphaAnimation(1, 0);
        mAnimation.setDuration(2000);
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.REVERSE);
        mLogo.startAnimation(mAnimation);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("SignUp1", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("SignUp1", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SignUp1", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SignUp1", "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(final FirebaseUser user) {
        if(user != null) {
            String username = user.getDisplayName();
            String email = user.getEmail();
            Uri url = user.getPhotoUrl();
            Toast.makeText(this, "Username : " + username, Toast.LENGTH_SHORT).show();
            RequestQueue queue = Volley.newRequestQueue(SignInActivity.this);
            String url1 = "https://medicento-api.herokuapp.com/user/login?useremail=" + email;
            StringRequest str = new StringRequest(Request.Method.GET, url1,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            sp = null;
                            try {
                                JSONObject spo = new JSONObject(response.toString());
                                JSONArray spa = spo.getJSONArray("Sales_Person");
                                JSONObject user = spa.getJSONObject(0);
                                sp = new SalesPerson(user.getString("Name"),
                                        user.getLong("Total_sales"),
                                        user.getInt("No_of_order"),
                                        user.getInt("Returns"),
                                        user.getLong("Earnings"),
                                        user.getString("_id"),
                                        user.getString("Allocated_Area"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SignInActivity.this);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Constants.SALE_PERSON_EMAIL, user.getEmail());
                            editor.putString(Constants.SALE_PERSON_ID, sp.getId());
                            editor.putString(Constants.SALE_PERSON_NAME, sp.getName());
                            editor.putFloat(Constants.SALE_PERSON_TOTAL_SALES, sp.getTotalSales());
                            editor.putInt(Constants.SALE_PERSON_NO_OF_ORDERS, sp.getNoOfOrder());
                            editor.putInt(Constants.SALE_PERSON_RETURNS, sp.getReturn());
                            editor.putString(Constants.SALE_PERSON_ALLOCATED_AREA_ID, sp.getAllocatedArea());
                            editor.apply();
                            Intent intent = new Intent();
                            if (getParent() == null) {
                                setResult(Activity.RESULT_OK, intent);
                            } else {
                                getParent().setResult(RESULT_OK);
                            }
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            mEmailEditTv.setText("");
                            Toast.makeText(SignInActivity.this, "Invalid Usercode ", Toast.LENGTH_SHORT).show();
                        }
                    });
            queue.add(str);

        }
    }

    private boolean isInputEmpty(String userCode) {
        return userCode.isEmpty();
    }


    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        mAnimation.setDuration(200);
        mLogo.startAnimation(mAnimation);
        if (id == Constants.LOG_IN_LOADER_ID) {
            Uri baseUri = Uri.parse(Constants.USER_LOGIN_URL);
            Uri.Builder builder = baseUri.buildUpon();

            builder.appendQueryParameter("usercode", mUserCode);

            return new SalesDataLoader(this, builder.toString(), getString(R.string.login_action));
        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        mProgressBar.setVisibility(View.GONE);
        SalesPerson salesPerson = (SalesPerson) data;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (salesPerson != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constants.SALE_PERSON_EMAIL, mUserCode);
            editor.putString(Constants.SALE_PERSON_NAME, salesPerson.getName());
            editor.putFloat(Constants.SALE_PERSON_TOTAL_SALES, salesPerson.getTotalSales());
            editor.putInt(Constants.SALE_PERSON_NO_OF_ORDERS, salesPerson.getNoOfOrder());
            editor.putInt(Constants.SALE_PERSON_RETURNS, salesPerson.getReturn());
            editor.putFloat(Constants.SALE_PERSON_EARNINGS, salesPerson.getEarnings());
            editor.putString(Constants.SALE_PERSON_ID, salesPerson.getId());
            editor.putString(Constants.SALE_PERSON_ALLOCATED_AREA_ID, salesPerson.getAllocatedArea());
            editor.apply();
            Intent intent = new Intent();
            if (getParent() == null) {
                setResult(Activity.RESULT_OK, intent);
            } else {
                getParent().setResult(RESULT_OK);
            }
            finish();
        } else {
            mEmailEditTv.setText("");
            Toast.makeText(this, "Incorrect UserCode!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {
    }
    private boolean amIConnect() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}