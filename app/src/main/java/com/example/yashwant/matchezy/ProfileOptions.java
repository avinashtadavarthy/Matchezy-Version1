package com.example.yashwant.matchezy;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProfileOptions extends AppCompatActivity {

    TextView blankspace;

    LinearLayout viewprofile, changepassword, helpandfeedback, logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_options);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        getWindow().setLayout(width,height);

        viewprofile = (LinearLayout) findViewById(R.id.viewprofile);
        changepassword = (LinearLayout) findViewById(R.id.changepassword);
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
                Intent i = new Intent(getApplicationContext(), ProfilePage.class);
                startActivity(i);
            }
        });

    }
}
