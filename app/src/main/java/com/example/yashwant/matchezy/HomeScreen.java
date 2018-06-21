package com.example.yashwant.matchezy;

import android.app.FragmentManager;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.scalified.fab.ActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity {

    private TextView mTextMessage;

    List<MatchedProfiles> lstMatchedProfiles ;



    FrameLayout frameLayout;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment = null;
            frameLayout =(FrameLayout)findViewById(R.id.home_container);
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

            switch (item.getItemId()) {

                case R.id.navigation_home:
                    frameLayout.setVisibility(View.VISIBLE);
                    selectedFragment = Fragment_Home.newInstance();

                    break;
                case R.id.navigation_bookmarks:
                    frameLayout.setVisibility(View.VISIBLE);
                    selectedFragment = Fragment_favorites.newInstance();
                    break;

                case R.id.navigation_messages:
                    frameLayout.setVisibility(View.VISIBLE);
                    selectedFragment = Fragment_messages.newInstance();
                    break;
                case R.id.navigation_notifications:
                    frameLayout.setVisibility(View.VISIBLE);
                    selectedFragment = Fragment_notifications.newInstance();
                    break;

                default:
                    frameLayout.setVisibility(View.VISIBLE);
                    selectedFragment = Fragment_Home.newInstance();
                    break;


            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.home_container, selectedFragment);
            transaction.commit();
            return true;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



    }

}
