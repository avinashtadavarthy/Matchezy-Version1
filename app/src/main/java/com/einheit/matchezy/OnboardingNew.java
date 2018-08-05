package com.einheit.matchezy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.einheit.matchezy.R;
import com.einheit.matchezy.login.Login;
import com.einheit.matchezy.messagestab.ForceUpdateChecker;
import com.einheit.matchezy.profilescreen.CustomPagerAdapter;
import com.facebook.CallbackManager;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class OnboardingNew extends AppCompatActivity {

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES= {R.drawable.first,R.drawable.second,R.drawable.third};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    private ArrayList<String> TextArray = new ArrayList<String>();
    CircleIndicator indicator;

    Button fblogin, emaillogin;
    String queryUserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_new);

        FirebaseMessaging.getInstance().subscribeToTopic("Hy");

        if (Intent.ACTION_VIEW.equals(getIntent().getAction())) {
            Uri uri = getIntent().getData();
            queryUserId = uri.getQueryParameter("q");
        }

        if (!getSPData("user_id").equals("") && !getSPData("user_token").equals("")) {
            Intent i = new Intent(getApplicationContext(),HomeScreen.class);

            if(!queryUserId.isEmpty()) {
                i.putExtra("queryUserId",queryUserId);
            }

            if(getIntent().hasExtra("notify"))
                i.putExtra("notify", getIntent().getStringExtra("notify"));
            startActivity(i);
            finish();
        }

        fblogin = findViewById(R.id.fblogin);
        emaillogin = findViewById(R.id.emaillogin);

        emaillogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OnboardingNew.this, Login.class).putExtra("fblogin","false");
                startActivity(i);
            }
        });

        fblogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OnboardingNew.this, Login.class).putExtra("fblogin","true");
                startActivity(i);
            }
        });


        Collections.addAll(ImagesArray, IMAGES);

        mPager = (ViewPager) findViewById(R.id.onboardingpager);
        TextArray.add("Find your Soulmate");
        TextArray.add("Find matches based on your interests");
        TextArray.add("No Swiping. Find a curated list of people who are looking for their Soulmate");

        mPager.setAdapter(new SlidingImage_Adapter(OnboardingNew.this,ImagesArray,TextArray));


        indicator = (CircleIndicator) findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        NUM_PAGES =IMAGES.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        },3000,3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }





    private String getSPData(String key) {

        SharedPreferences mUserData = this.getSharedPreferences("UserData", MODE_PRIVATE);
        String data = mUserData.getString(key, "");

        return data;
    }


}
