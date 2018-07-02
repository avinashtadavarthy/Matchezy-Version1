package com.einheit.matchezy.registration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.einheit.matchezy.login.Login;
import com.einheit.matchezy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.scalified.fab.ActionButton;

import java.util.concurrent.TimeUnit;

public class OTP extends AppCompatActivity {

    EditText otp;
    Button callbtn, retrybtn;
    String verificationId = "";
    ActionButton actionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        otp = (EditText)findViewById(R.id.editText_otp);
        callbtn = (Button) findViewById(R.id.callbtn);
        retrybtn = (Button) findViewById(R.id.retrybtn);

        actionButton = (ActionButton) findViewById(R.id.action_button_next2);
        // actionButton.hide();
        actionButton.setType(ActionButton.Type.BIG);
        //actionButton.setSize(65.0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            actionButton.setButtonColor(Color.parseColor("#EA5251"));
        }
        actionButton.setRippleEffectEnabled(true);
        actionButton.playShowAnimation();
        actionButton.setImageResource(R.drawable.ic_action_arrow);

        final String TAG = "ASD";

        retrybtn.setEnabled(false);
        callbtn.setVisibility(View.GONE);

        storeSPData("isOtpVerificationCompleted", false);

        final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.e(TAG, "onVerificationCompleted:" + credential);
                storeSPData("isOtpVerificationCompleted", true);
                storeSPData("isOngoingVerification", false);

                Toast.makeText(OTP.this, "Registration Success", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(OTP.this, Login.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.e(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(OTP.this,"Invalid phone number", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(OTP.this,"Trying too many times", Toast.LENGTH_SHORT).show();
                }

                storeSPData("isOtpVerificationCompleted", false);
                storeSPData("isOngoingVerification", false);

                retrybtn.setEnabled(true);
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Log.e(TAG, "code sent - " + verificationId);
                OTP.this.verificationId = verificationId;
            }
        };

        if(!getSPBoolean("isOngoingVerification")) {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(getSPData("phone_number"), 120,
                    TimeUnit.SECONDS, OTP.this, mCallbacks);
        }
        storeSPData("isOtpVerified", false);
        storeSPData("isOngoingVerification", true);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!getSPBoolean("isOtpVerified")) {

                    storeSPData("isOtpVerified", true);
                    String code = otp.getText().toString().trim();

                    if (verificationId.isEmpty()) {
                        Toast.makeText(OTP.this, "Wait till you get the otp", Toast.LENGTH_SHORT).show();
                        storeSPData("isOtpVerified", false);
                    } else if (code.isEmpty()) {
                        Toast.makeText(OTP.this, "Invalid verification code", Toast.LENGTH_SHORT).show();
                        storeSPData("isOtpVerified", false);
                    } else {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                        Log.e(TAG, "onVerified:" + credential + " - " + credential.getSmsCode());
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        auth.signInWithCredential(credential)
                                .addOnCompleteListener(OTP.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            storeSPData("isOtpVerificationCompleted", true);
                                            storeSPData("isOngoingVerification", false);
                                            Log.d(TAG, "signInWithCredential:success");
                                            Toast.makeText(OTP.this, "Registration Success", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(OTP.this, Login.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(i);

                                        } else {

                                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                                                Log.d(TAG, "Invalid verification code");
                                                Toast.makeText(OTP.this, "Invalid verification code", Toast.LENGTH_SHORT).show();
                                            } else Toast.makeText(OTP.this, "Verification failed", Toast.LENGTH_SHORT).show();
                                            storeSPData("isOtpVerificationCompleted", false);
                                            storeSPData("isOtpVerified", false);
                                            storeSPData("isOngoingVerification", false);
                                        }
                                    }
                                });
                    }
                } else Toast.makeText(OTP.this, "The code is being verified", Toast.LENGTH_SHORT).show();


            }
        });

        retrybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber( getSPData("phone_number"), 120,
                        TimeUnit.SECONDS, OTP.this, mCallbacks);

                storeSPData("isOtpVerified", false);
            }
        });

    }

    //Shared Preferences
    private void storeSPData(String key, Boolean data) {

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

    private Boolean getSPBoolean(String key) {

        SharedPreferences mUserData = this.getSharedPreferences("UserData", MODE_PRIVATE);
        Boolean data = mUserData.getBoolean(key, false);

        return data;

    }

}
