package com.einheit.matchezy.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.einheit.matchezy.HomeScreen;
import com.einheit.matchezy.MySingleton;
import com.einheit.matchezy.R;
import com.einheit.matchezy.SlidingImage_Adapter;
import com.einheit.matchezy.Utility;
import com.einheit.matchezy.messagestab.ForceUpdateChecker;
import com.einheit.matchezy.registration.OTP;
import com.einheit.matchezy.registration.Registration;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class OnboardingNew extends AppCompatActivity implements ForceUpdateChecker.OnUpdateNeededListener {

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES= {R.drawable.first,R.drawable.second,R.drawable.third};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    private ArrayList<String> TextArray = new ArrayList<String>();
    CircleIndicator indicator;

    LinearLayout onboardingswipe, editable_layout, signinbtnlayout, signupbtnlayout;
    Button fblogin, emaillogin, signinbtn, signupbtn;
    TextView signup, forgotpassword;
    EditText emailEditText, passwordEditText, emailEditTextSignup;
    AlertDialog newUpdateDialog;
    String queryUserId = "";

    //fb login integration
    CallbackManager callbackManager;
    String access;
    //String url = "https://graph.facebook.com/me?fields=id,verified,first_name,friends,last_name,address,location,name,gender,email,birthday,picture.height(720),age_range&access_token=";
    String url = "https://graph.facebook.com/me?fields=id,verified,first_name,last_name,name,gender,email,birthday,picture.height(720)&access_token=";
    Button facebook;
    Button loginButton;
    /*private List<String> permissionNeeds = Arrays.asList("public_profile",
            "email", "user_friends", "user_birthday", "user_gender", "user_location",
            "user_friends", "user_photos", "user_likes");*/
    private List<String> permissionNeeds = Arrays.asList("public_profile", "email", "user_birthday", "user_gender");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_new);

        FirebaseMessaging.getInstance().subscribeToTopic("Hy");
        ForceUpdateChecker.with(this).onUpdateNeeded(this).check();


        final ProgressBar progressBar = findViewById(R.id.progressBar);


        emaillogin = (Button) findViewById(R.id.emaillogin);
        signinbtn = (Button) findViewById(R.id.signinbtn);
        signupbtn = (Button) findViewById(R.id.signupbtn);
        onboardingswipe = (LinearLayout) findViewById(R.id.onboardingswipe);
        editable_layout = (LinearLayout) findViewById(R.id.editable_layout);
        signinbtnlayout = (LinearLayout) findViewById(R.id.signinbtnlayout);
        signupbtnlayout = (LinearLayout) findViewById(R.id.signupbtnlayout);
        signup = (Button) findViewById(R.id.submitbtn);
        forgotpassword = (TextView) findViewById(R.id.forgotpassword);
        loginButton = (Button) findViewById(R.id.loginbtn);
        emailEditText = (EditText) findViewById(R.id.emailEditTextforsignin);
        emailEditTextSignup = (EditText) findViewById(R.id.emailEditTextforsignup);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        mPager = (ViewPager) findViewById(R.id.onboardingpager);
        indicator = (CircleIndicator) findViewById(R.id.indicator);


        emaillogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onboardingswipe.setVisibility(View.GONE);
                editable_layout.setVisibility(View.VISIBLE);
            }
        });

        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signinbtn.setBackgroundResource(R.drawable.outlinebox);
                signupbtn.setBackgroundResource(android.R.color.transparent);
                signinbtnlayout.setVisibility(View.VISIBLE);
                signupbtnlayout.setVisibility(View.GONE);
            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signinbtn.setBackgroundResource(android.R.color.transparent);
                signupbtn.setBackgroundResource(R.drawable.outlinebox);
                signinbtnlayout.setVisibility(View.GONE);
                signupbtnlayout.setVisibility(View.VISIBLE);
            }
        });


        Collections.addAll(ImagesArray, IMAGES);


        TextArray.add("Find your Soulmate");
        TextArray.add("Find matches based on your interests");
        TextArray.add("No Swiping. Find a curated list of people who are looking for their Soulmate");
        mPager.setAdapter(new SlidingImage_Adapter(OnboardingNew.this,ImagesArray,TextArray));
        indicator.setViewPager(mPager);
        NUM_PAGES =IMAGES.length;


        if (Intent.ACTION_VIEW.equals(getIntent().getAction())) {
            Uri uri = getIntent().getData();
            queryUserId = uri.getQueryParameter("q");
        }

        if (!getSPData("user_id").equals("") && !getSPData("user_token").equals("")) {
            Intent i = new Intent(getApplicationContext(),HomeScreen.class);

            if(!queryUserId.isEmpty()) {
                i.putExtra("queryUserId",queryUserId);
            }

            if(getIntent().hasExtra("notify"))
                i.putExtra("notify", getIntent().getStringExtra("notify"));
            startActivity(i);
            finish();
        }

        Log.e("ASD", getSPBoolean("isOtpVerificationCompleted").toString());

        if(!getSPBoolean("isOtpVerificationCompleted")) {
            Intent i = new Intent(OnboardingNew.this, OTP.class);
            startActivity(i);
            finish();
        }


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                if(email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(OnboardingNew.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
                else {

                    progressBar.setVisibility(View.VISIBLE);

                    AndroidNetworking.post(Utility.getInstance().BASE_URL + "login")
                            .addBodyParameter("email", email)
                            .addBodyParameter("password", password)
                            .setPriority(Priority.HIGH)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject res) {
                                    switch (res.optString("status_code")) {
                                        case "200": {

                                            progressBar.setVisibility(View.GONE);

                                            Log.e("ASD", res.toString());

                                            clearSPData();
                                            storeSPData("user_id", res.optJSONObject("message").optString("user_id"));
                                            storeSPData("user_token", res.optJSONObject("message").optString("user_token"));
                                            FirebaseMessaging.getInstance().subscribeToTopic(res.optJSONObject("message").optString("user_id"));

                                            //to get users data
                                            AndroidNetworking.post(Utility.getInstance().BASE_URL + "getUserData")
                                                    .addBodyParameter("user_id", getSPData("user_id"))
                                                    .addBodyParameter("user_token", getSPData("user_token"))
                                                    .addBodyParameter("user_id_2", getSPData("user_id"))
                                                    .setPriority(Priority.HIGH)
                                                    .build()
                                                    .getAsJSONObject(new JSONObjectRequestListener() {
                                                        @Override
                                                        public void onResponse(JSONObject response) {
                                                            // do anything with response

                                                            if(response.optInt("status_code") == 200) {
                                                                //Log.e("userData", response.toString());
                                                                storeSPData("userData", response.optJSONObject("message").toString());
                                                                Intent intent = new Intent(OnboardingNew.this, HomeScreen.class);
                                                                if(!queryUserId.isEmpty()) {
                                                                    intent.putExtra("queryUserId",queryUserId);
                                                                }
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                            else {
                                                                Toast.makeText(OnboardingNew.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                        @Override
                                                        public void onError(ANError error) {

                                                            error.printStackTrace();

                                                        }
                                                    });

                                            break;
                                        }
                                        case "404": {

                                            progressBar.setVisibility(View.GONE);

                                            Toast.makeText(OnboardingNew.this, res.optString("message"), Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                        case "400":

                                            progressBar.setVisibility(View.GONE);

                                            Toast.makeText(OnboardingNew.this, res.optString("message"), Toast.LENGTH_SHORT).show();
                                            break;
                                    }

                                }

                                @Override
                                public void onError(ANError error) {
                                    error.printStackTrace();
                                }
                            });
                }
            }
        });



        //facebook signin integration
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        access = loginResult.getAccessToken().getToken();

                        //making the request and getting the data
                        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url + access, null,
                                new Response.Listener<JSONObject>()
                                {
                                    @Override
                                    public void onResponse(final JSONObject response) {

                                        String fb_id = response.optString("id");
                                        storeSPData("fb_id", fb_id);

                                        progressBar.setVisibility(View.VISIBLE);

                                        AndroidNetworking.post(Utility.getInstance().BASE_URL + "fbLogin")
                                                .addBodyParameter("fb_id",fb_id)
                                                .setPriority(Priority.HIGH)
                                                .build()
                                                .getAsJSONObject(new JSONObjectRequestListener() {
                                                    @Override
                                                    public void onResponse(JSONObject res) {
                                                        switch (res.optString("status_code")) {
                                                            case "200": {

                                                                //Log.e("fbLogin", res.toString());
                                                                clearSPData();
                                                                storeSPData("user_id", res.optJSONObject("message").optString("user_id"));
                                                                storeSPData("user_token", res.optJSONObject("message").optString("user_token"));
                                                                FirebaseMessaging.getInstance().subscribeToTopic(res.optJSONObject("message").optString("user_id"));

                                                                //to get users data
                                                                AndroidNetworking.post(Utility.getInstance().BASE_URL + "getUserData")
                                                                        .addBodyParameter("user_id", getSPData("user_id"))
                                                                        .addBodyParameter("user_token", getSPData("user_token"))
                                                                        .addBodyParameter("user_id_2", getSPData("user_id"))
                                                                        .setPriority(Priority.HIGH)
                                                                        .build()
                                                                        .getAsJSONObject(new JSONObjectRequestListener() {
                                                                            @Override
                                                                            public void onResponse(JSONObject response) {
                                                                                // do anything with response

                                                                                progressBar.setVisibility(View.GONE);

                                                                                if(response.optInt("status_code") == 200) {
                                                                                    //Log.e("userData", response.toString());
                                                                                    storeSPData("userData", response.optJSONObject("message").toString());
                                                                                    Intent intent = new Intent(OnboardingNew.this, HomeScreen.class);
                                                                                    if(!queryUserId.isEmpty()) {
                                                                                        intent.putExtra("queryUserId",queryUserId);
                                                                                    }
                                                                                    startActivity(intent);
                                                                                    finish();
                                                                                }
                                                                                else {
                                                                                    progressBar.setVisibility(View.GONE);
                                                                                    Toast.makeText(OnboardingNew.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                            @Override
                                                                            public void onError(ANError error) {

                                                                                error.printStackTrace();

                                                                            }
                                                                        });
                                                                break;
                                                            }
                                                            case "404": {
                                                                progressBar.setVisibility(View.GONE);

                                                                Log.e("fbLogin", "user doesnt exist");
                                                                storeSPData("facebookdata", response.toString());
                                                                storeSPData("isLoggedInThroughFb", true);
                                                                Intent intent = new Intent(OnboardingNew.this, Registration.class);
                                                                startActivity(intent);
                                                                break;
                                                            }
                                                            case "400":

                                                                progressBar.setVisibility(View.GONE);

                                                                Toast.makeText(OnboardingNew.this, res.optString("message"), Toast.LENGTH_SHORT).show();
                                                                break;
                                                        }

                                                    }
                                                    @Override
                                                    public void onError(ANError error) {
                                                        error.printStackTrace();
                                                    }
                                                });

                                        //Log.e("fbresponse", response.toString());

                                    }
                                },
                                new Response.ErrorListener()
                                {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                        Toast.makeText(OnboardingNew.this, "Login Error", Toast.LENGTH_SHORT).show();
                                        error.printStackTrace();
                                    }
                                }
                        );

                        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(getRequest);
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(OnboardingNew.this, "Login attempt cancelled.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        exception.printStackTrace();
                        Toast.makeText(OnboardingNew.this, "Login attempt failed", Toast.LENGTH_SHORT).show();
                    }
                }
        );


        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OnboardingNew.this, ForgotPassword.class);
                startActivity(intent);

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (emailEditTextSignup.getText().toString().isEmpty()) {
                    Toast.makeText(OnboardingNew.this, "Enter a valid email id", Toast.LENGTH_SHORT).show();
                } else {
                    storeSPData("isLoggedInThroughFb", false);
                    storeSPData("emailFromSignup", emailEditTextSignup.getText().toString().trim());
                    Intent intent = new Intent(OnboardingNew.this, Registration.class);
                    startActivity(intent);
                }

            }
        });



        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        },3000,3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }



    //facebook login
    public void fbLoginNew(View view) {

        if(AccessToken.getCurrentAccessToken()!=null) {
            LoginManager.getInstance().logOut();
            LoginManager.getInstance().logInWithReadPermissions(this, permissionNeeds);
        } else {
            LoginManager.getInstance().logInWithReadPermissions(this, permissionNeeds);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        //facebook
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //Shared Preferences
    private void storeSPData(String key, String data) {

        SharedPreferences mUserData = this.getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor mUserEditor = mUserData.edit();
        mUserEditor.putString(key, data);
        mUserEditor.commit();

    }

    private void storeSPData(String key, boolean data) {

        SharedPreferences mUserData = this.getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor mUserEditor = mUserData.edit();
        mUserEditor.putBoolean(key, data);
        mUserEditor.commit();

    }

    private String getSPData(String key) {

        SharedPreferences mUserData = this.getSharedPreferences("UserData", MODE_PRIVATE);
        String data = mUserData.getString(key, "");

        return data;
    }

    private void clearSPData() {
        SharedPreferences mUserData = this.getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor mUserEditor = mUserData.edit();
        mUserEditor.clear();
        mUserEditor.apply();
    }

    private Boolean getSPBoolean(String key) {

        SharedPreferences mUserData = this.getSharedPreferences("UserData", MODE_PRIVATE);
        Boolean data = mUserData.getBoolean(key, true);

        return data;

    }

    @Override
    public void onUpdateNeeded(final String updateUrl) {
        newUpdateDialog = new AlertDialog.Builder(this)
                .setTitle("New version available")
                .setCancelable(false)
                .setMessage("Please, update app to new version to continue.")
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                redirectStore(updateUrl);
                            }
                        }).setNegativeButton("Later",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        }).create();
        newUpdateDialog.show();
    }


    private void redirectStore(String updateUrl) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}
