package com.example.yashwant.matchezy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.scalified.fab.ActionButton;
import com.twilio.verification.TwilioVerification;
import com.twilio.verification.external.VerificationStatus;
import com.twilio.verification.external.Via;

import org.json.JSONArray;
import org.json.JSONObject;

public class OTP extends AppCompatActivity {

    EditText otp;
    Button callbtn, retrybtn;

    String url = "https://matchezy.herokuapp.com/verify/token";

    private TwilioVerification twilioVerification;

    MyVerificationReceiver myVerificationReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        AndroidNetworking.initialize(this);
        twilioVerification = new TwilioVerification(this);

        otp = (EditText)findViewById(R.id.editText_otp);
        callbtn = (Button) findViewById(R.id.callbtn);
        retrybtn = (Button) findViewById(R.id.retrybtn);


        final ActionButton actionButton = (ActionButton) findViewById(R.id.action_button_next2);
        // actionButton.hide();
        actionButton.setType(ActionButton.Type.BIG);
        //actionButton.setSize(65.0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            actionButton.setButtonColor(Color.parseColor("#EA5251"));
        }
        actionButton.setRippleEffectEnabled(true);
        actionButton.playShowAnimation();
        actionButton.setImageResource(R.drawable.ic_action_arrow);

        AndroidNetworking.post(User.getInstance().BASE_URL + "/generateJWT")
                .addBodyParameter("phone_number", getSPData("phone_number"))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response

                        String jwt = response.optString("message");

                        Log.e("TWILIO", jwt);

                        twilioVerification.startVerification(jwt, Via.SMS);

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        error.printStackTrace();
                    }
                });


        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MyVerificationReceiver.state == VerificationStatus.State.STARTED) {
                    String fotp = otp.getText().toString().trim();

                    twilioVerification.checkVerificationPin(fotp);
                }

            }
        });

        callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidNetworking.post(url)
                        .addBodyParameter("phone_number", "+919445053456")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // do anything with response

                                String jwt = response.optString("jwt_token");

                                Log.e("TWILIO", jwt);

                                twilioVerification.startVerification(jwt, Via.CALL);

                            }
                            @Override
                            public void onError(ANError error) {
                                // handle error
                                error.printStackTrace();
                            }
                        });
            }
        });

        retrybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidNetworking.post(url)
                        .addBodyParameter("phone_number", "+919445053456")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // do anything with response

                                String jwt = response.optString("jwt_token");

                                Log.e("TWILIO", jwt);

                                twilioVerification.startVerification(jwt, Via.SMS);

                            }
                            @Override
                            public void onError(ANError error) {
                                // handle error
                                error.printStackTrace();
                            }
                        });
            }
        });

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
