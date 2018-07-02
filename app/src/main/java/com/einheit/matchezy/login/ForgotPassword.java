package com.einheit.matchezy.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.einheit.matchezy.R;
import com.einheit.matchezy.Utility;
import com.scalified.fab.ActionButton;

import org.json.JSONObject;

public class ForgotPassword extends AppCompatActivity {

    ActionButton action_button_next2;
    EditText input_phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        action_button_next2 = (ActionButton) findViewById(R.id.action_button_next2);
        input_phone_number = (EditText) findViewById(R.id.input_phone_number);

        action_button_next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AndroidNetworking.post(Utility.getInstance().BASE_URL + "resetPassword")
                        .addBodyParameter("phoneNumber", input_phone_number.getText().toString().trim())
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                    finish();
                                    Toast.makeText(ForgotPassword.this, response.optString("message"), Toast.LENGTH_LONG).show();
                            }
                            @Override
                            public void onError(ANError error) {
                                error.printStackTrace();
                            }
                        });
            }

        });
    }
}
