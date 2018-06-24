package com.example.yashwant.matchezy;

import android.content.Intent;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

import com.bumptech.glide.Glide;
import com.scalified.fab.ActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

            switch (item.getItemId()) {

                case R.id.navigation_home:
                    fragmentManager.beginTransaction().replace(R.id.home_container, new Fragment_Home()).commit();
                    break;
                case R.id.navigation_bookmarks:
                    fragmentManager.beginTransaction().replace(R.id.home_container, new Fragment_favorites()).commit();
                    break;
                case R.id.navigation_messages:
                    fragmentManager.beginTransaction().replace(R.id.home_container, new Fragment_messages()).commit();
                    break;
                case R.id.navigation_notifications:
                    fragmentManager.beginTransaction().replace(R.id.home_container, new Fragment_notifications()).commit();
                    break;
            }

            return true;

        }
    };

    CircleImageView profileimg;

    JSONObject userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //BottomNavigationViewHelper.removeShiftMode(navigation);
        navigation.setSelectedItemId(R.id.navigation_home);

        Log.e("userdata", getSPData("userdata"));

        try {
            userData = new JSONObject(getSPData("userdata"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        profileimg = (CircleImageView) findViewById(R.id.profileimg);

        Glide.with(getApplicationContext()).load(userData.optString("profileImageURL")).into(profileimg);

        profileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeScreen.this, ProfileOptions.class);
                startActivity(i);
//                overridePendingTransition(R.anim.slide_in_up,0);
            }
        });

    }


    private String getSPData(String key) {

        SharedPreferences mUserData = this.getSharedPreferences("UserData", MODE_PRIVATE);
        String data = mUserData.getString(key, "");

        return data;
    }

}
