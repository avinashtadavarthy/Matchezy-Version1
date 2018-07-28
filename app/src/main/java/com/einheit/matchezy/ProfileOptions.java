package com.einheit.matchezy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.einheit.matchezy.login.Login;
import com.einheit.matchezy.profileoptions.BlockedListScreen;
import com.einheit.matchezy.profilescreen.ProfilePage;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileOptions extends AppCompatActivity {

    TextView blankspace;

    View progressOverlay;

    LinearLayout viewprofile, privacysettings, helpandfeedback, logout, termsandconditions,
            privacypolicy, aboutus, blockedProfiles;

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

        blockedProfiles = findViewById(R.id.blockedprofile);

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
        termsandconditions = (LinearLayout) findViewById(R.id.termsandconditions);
        privacypolicy = (LinearLayout) findViewById(R.id.privacypolicy);
        aboutus = (LinearLayout) findViewById(R.id.aboutus);

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
                finish();
            }
        });

        Log.e("ASd",getSPData("user_id"));

        termsandconditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileOptions.this, StaticTextPage.class).putExtra("type","terms");
                startActivity(i);
                finish();
            }
        });

        privacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileOptions.this, StaticTextPage.class).putExtra("type","privacy");
                startActivity(i);
                finish();
            }
        });

        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileOptions.this, StaticTextPage.class).putExtra("type","about");
                startActivity(i);
                finish();
            }
        });

        helpandfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileOptions.this, StaticTextPage.class).putExtra("type","help");
                startActivity(i);
                finish();
            }
        });

        privacysettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileOptions.this, StaticTextPage.class).putExtra("type","privsettings");
                startActivity(i);
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressOverlay.setVisibility(View.VISIBLE);

                AndroidNetworking.post(Utility.getInstance().BASE_URL + "logout")
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

                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    FirebaseInstanceId.getInstance().deleteInstanceId();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).start();

                                        Intent i = new Intent(getApplicationContext(),Login.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        finish();
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

        blockedProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), BlockedListScreen.class);
                startActivity(i);
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
