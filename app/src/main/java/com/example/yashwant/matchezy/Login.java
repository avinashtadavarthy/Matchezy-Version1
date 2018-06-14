package com.example.yashwant.matchezy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class Login extends AppCompatActivity {

    TextView signup;

    //fb login integration
    CallbackManager callbackManager;
    String access;
    String url = "https://graph.facebook.com/me?fields=id,verified,first_name,friends,last_name,address,location,name,gender,email,birthday,picture.height(720),age_range&access_token=";
    Button facebook;
    private List<String> permissionNeeds = Arrays.asList("public_profile",
            "email", "user_friends", "user_birthday", "user_gender", "user_location",
            "user_friends", "user_photos", "user_likes");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        AndroidNetworking.initialize(this);

        signup = (TextView) findViewById(R.id.signupButton);
        facebook = (Button) findViewById(R.id.facebook);


        //facebook signin integration
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback< LoginResult >() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        access = loginResult.getAccessToken().getToken();

                        //making the request and getting the data
                        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url + access, null,
                                new Response.Listener<JSONObject>()
                                {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        String fb_id = response.optString("id");
                                        storeSPData("fb_id", fb_id);

                                        AndroidNetworking.post(User.getInstance().BASE_URL + "fbLogin")
                                                .addBodyParameter("fb_id",fb_id)
                                                .setPriority(Priority.HIGH)
                                                .build()
                                                .getAsJSONObject(new JSONObjectRequestListener() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        if(response.optString("status_code").equals("200")) {

                                                            Log.e("fbLogin", "user already exists");
                                                            Intent intent = new Intent(Login.this, HomeScreen.class);
                                                            startActivity(intent);

                                                        } else if(response.optString("status_code").equals("404")) {

                                                            Log.e("fbLogin", "user doesnt exist");
                                                            storeSPData("facebookdata", response.toString());
                                                            storeSPData("isLoggedInThroughFb", true);
                                                            Intent intent = new Intent(Login.this, Registration.class);
                                                            startActivity(intent);

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
}
