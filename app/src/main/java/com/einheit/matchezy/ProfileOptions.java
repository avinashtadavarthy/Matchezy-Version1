package com.einheit.matchezy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileOptions extends AppCompatActivity {

    TextView blankspace;

    View progressOverlay;

    LinearLayout viewprofile, privacysettings, helpandfeedback, logout;

    RelativeLayout optionslistlayout;

    JSONObject userData;

    CircleImageView profileimg;
    TextView profilename, profileemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_options);

        optionslistlayout = findViewById(R.id.optionslistlayout);
        optionslistlayout.setClipToOutline(true);

        AndroidNetworking.initialize(this);

         try {
            userData = new JSONObject(getSPData("userdata"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        getWindow().setLayout(width,height);


        profileimg = findViewById(R.id.profileimg);
        profilename = findViewById(R.id.profilename);
        profileemail = findViewById(R.id.profileemail);

        Glide.with(getApplicationContext()).load(userData.optString("profileImageURL")).into(profileimg);
        profilename.setText(userData.optString("name"));
        profileemail.setText(userData.optString("email"));


        progressOverlay = findViewById(R.id.progress_overlay);

        viewprofile = (LinearLayout) findViewById(R.id.viewprofile);
        privacysettings = (LinearLayout) findViewById(R.id.privacysettings);
        helpandfeedback = (LinearLayout) findViewById(R.id.helpandfeedback);
        logout = (LinearLayout) findViewById(R.id.logout);

        blankspace = (TextView) findViewById(R.id.blankspace);

        blankspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ProfilePage.class).putExtra("myprofile", "true");
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressOverlay.setVisibility(View.VISIBLE);

                AndroidNetworking.post(User.getInstance().BASE_URL + "logout")
                        .addBodyParameter("user_id", getSPData("user_id"))
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                switch(response.optString("status_code")) {
                                    case "200": progressOverlay.setVisibility(View.GONE);
                                        Toast.makeText(ProfileOptions.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                                        clearSPData();
                                        Intent i = new Intent(getApplicationContext(),Login.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        break;

                                    case "400": progressOverlay.setVisibility(View.GONE);
                                        Toast.makeText(ProfileOptions.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                                        finish();
                                }

                            }
                            @Override
                            public void onError(ANError error) {
                               error.printStackTrace();
                            }
                        });

            }
        });

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

}
