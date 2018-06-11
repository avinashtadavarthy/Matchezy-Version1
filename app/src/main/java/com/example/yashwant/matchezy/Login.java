package com.example.yashwant.matchezy;

import android.app.Activity;
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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class Login extends AppCompatActivity {

    TextView signup;

    //fb login integration
    CallbackManager callbackManager;
    String access;
    String url = "https://graph.facebook.com/me?fields=id,verified,first_name,last_name,name,gender,email,cover.height(720),picture.height(720),age_range&access_token=";
    Button facebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);


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

                                       storeSPData("facebookdata", response.toString());

                                        Intent intent = new Intent(Login.this, Registration.class);
                                        startActivity(intent);

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


                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);

            }
        });
    }


    //facebook login

    public void fbLogin(View view) {

        if(AccessToken.getCurrentAccessToken()!=null) {
            LoginManager.getInstance().logOut();
            LoginManager.getInstance().logInWithReadPermissions(
                    this,
                    Arrays.asList("user_photos", "email", "user_birthday", "public_profile"));
        } else {

            LoginManager.getInstance().logInWithReadPermissions(
                    this,
                    Arrays.asList("user_photos", "email", "user_birthday", "public_profile"));

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

    private String getSPData(String key) {

        SharedPreferences mUserData = this.getSharedPreferences("UserData", MODE_PRIVATE);
        String data = mUserData.getString(key, "");

        return data;
    }

}
