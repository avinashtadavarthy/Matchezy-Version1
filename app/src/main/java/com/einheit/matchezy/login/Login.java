package com.einheit.matchezy.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.einheit.matchezy.Utility;
import com.einheit.matchezy.registration.OTP;
import com.einheit.matchezy.registration.Registration;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class Login extends AppCompatActivity {

    TextView signup, forgotpassword;
    EditText emailEditText, passwordEditText;

    //fb login integration
    CallbackManager callbackManager;
    String access;
    String url = "https://graph.facebook.com/me?fields=id,verified,first_name,friends,last_name,address,location,name,gender,email,birthday,picture.height(720),age_range&access_token=";
    Button facebook;
    Button loginButton;
    private List<String> permissionNeeds = Arrays.asList("public_profile",
            "email", "user_friends", "user_birthday", "user_gender", "user_location",
            "user_friends", "user_photos", "user_likes");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        FirebaseMessaging.getInstance().subscribeToTopic("Hy");

        if (!getSPData("user_id").equals("") && !getSPData("user_token").equals("")) {
            Intent i = new Intent(getApplicationContext(),HomeScreen.class);

            if(getIntent().hasExtra("notify"))
                i.putExtra("notify", getIntent().getStringExtra("notify"));
            startActivity(i);
            finish();
        }

        Log.e("ASD", getSPBoolean("isOtpVerificationCompleted").toString());

        if(!getSPBoolean("isOtpVerificationCompleted")) {
            Intent i = new Intent(Login.this ,OTP.class);
            startActivity(i);
            finish();
        }

        AndroidNetworking.initialize(this);

        signup = (TextView) findViewById(R.id.signupButton);
        forgotpassword = (TextView) findViewById(R.id.forgotpassword);
        facebook = (Button) findViewById(R.id.facebook);
        loginButton = (Button) findViewById(R.id.loginbtn);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                if(email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
                else {
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
                                                                Log.e("userdata", response.toString());
                                                                storeSPData("userdata", response.optJSONObject("message").toString());
                                                                Intent intent = new Intent(Login.this, HomeScreen.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                            else {
                                                                Toast.makeText(Login.this, response.optString("message"), Toast.LENGTH_SHORT).show();
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

                                            Toast.makeText(Login.this, res.optString("message"), Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                        case "400":

                                            Toast.makeText(Login.this, res.optString("message"), Toast.LENGTH_SHORT).show();
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

                                        AndroidNetworking.post(Utility.getInstance().BASE_URL + "fbLogin")
                                                .addBodyParameter("fb_id",fb_id)
                                                .setPriority(Priority.HIGH)
                                                .build()
                                                .getAsJSONObject(new JSONObjectRequestListener() {
                                                    @Override
                                                    public void onResponse(JSONObject res) {
                                                        switch (res.optString("status_code")) {
                                                            case "200": {

                                                                Log.e("fbLogin", res.toString());
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
                                                                                    Log.e("userdata", response.toString());
                                                                                    storeSPData("userdata", response.optJSONObject("message").toString());
                                                                                    Intent intent = new Intent(Login.this, HomeScreen.class);
                                                                                    startActivity(intent);
                                                                                    finish();
                                                                                }
                                                                                else {
                                                                                    Toast.makeText(Login.this, response.optString("message"), Toast.LENGTH_SHORT).show();
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

                                                                Log.e("fbLogin", "user doesnt exist");
                                                                storeSPData("facebookdata", response.toString());
                                                                storeSPData("isLoggedInThroughFb", true);
                                                                Intent intent = new Intent(Login.this, Registration.class);
                                                                startActivity(intent);
                                                                break;
                                                            }
                                                            case "400":
                                                                Toast.makeText(Login.this, res.optString("message"), Toast.LENGTH_SHORT).show();
                                                                break;
                                                        }

                                                    }
                                                    @Override
                                                    public void onError(ANError error) {
                                                        error.printStackTrace();
                                                    }
                                                });

                                        Log.e("fbresponse", response.toString());

                                    }
                                },
                                new Response.ErrorListener()
                                {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                        Toast.makeText(Login.this, "Login Error", Toast.LENGTH_SHORT).show();
                                        error.printStackTrace();
                                    }
                                }
                        );

                        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(getRequest);
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(Login.this, "Login attempt cancelled.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        exception.printStackTrace();
                        Toast.makeText(Login.this, "Login attempt failed", Toast.LENGTH_SHORT).show();
                    }
                }
        );


        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent intent = new Intent(Login.this, ForgotPassword.class);
               startActivity(intent);

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                storeSPData("isLoggedInThroughFb", false);

                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);

            }
        });
    }


    //facebook login

    public void fbLogin(View view) {

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

}