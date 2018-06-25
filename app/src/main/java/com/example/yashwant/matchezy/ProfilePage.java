package com.example.yashwant.matchezy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.JsonArray;
import com.scalified.fab.ActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.relex.circleindicator.CircleIndicator;

public class ProfilePage extends AppCompatActivity {


    SlidingUpPanelLayout profile_sliding_layout;
    RelativeLayout maininfo;
    ImageView bookmarkbtn, editbtn, one, two, three;
    TextView pagertextindicator;
    boolean ct = false;
    String myprofile;

    TextView profilename, age, city;

    JSONObject userData;


    String[] sampleimgurls = {
            "https://homepages.cae.wisc.edu/~ece533/images/monarch.png",
            "https://homepages.cae.wisc.edu/~ece533/images/goldhill.png",
            "https://homepages.cae.wisc.edu/~ece533/images/watch.png",
            "https://homepages.cae.wisc.edu/~ece533/images/airplane.png",
            "https://homepages.cae.wisc.edu/~ece533/images/arctichare.png"
    };
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final ViewPager imagePager = (ViewPager) findViewById(R.id.imagePager);
        final CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);

        final ViewPager pager = (ViewPager) findViewById(R.id.viewPager);

        final ActionButton actionButton = (ActionButton) findViewById(R.id.action_button_like);
        final ActionButton actionButton2 = (ActionButton) findViewById(R.id.action_button_dislike);

        bookmarkbtn = findViewById(R.id.bookmarkbtn);
        editbtn = findViewById(R.id.editbtn);

        profile_sliding_layout = (SlidingUpPanelLayout) findViewById(R.id.profile_sliding_layout);

        profilename = findViewById(R.id.profilename);
        city = findViewById(R.id.city);


        AndroidNetworking.initialize(this);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        myprofile = getIntent().getStringExtra("myprofile");

        if(myprofile.equals("true")) {

            try {
                userData = new JSONObject(getSPData("userdata"));
                profilename.setText(userData.optString("name") + ", ");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                try {
                    Date date = format.parse(String.valueOf(userData.optString("dob")));
                    profilename.append(User.getInstance().getAge(date.getYear() + 1900,
                            date.getMonth(), date.getDay()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                city.setText(userData.optString("currentCity"));


                //images on top
                JSONArray picsArray = userData.optJSONArray("pictures");

                String pics = userData.optString("pictures").replace("\\","");
                //String[] picurls = pics.substring(1, pics.length()-1).split(",", 4);
                String[] picurls = {"", "", "", ""};

                for(int i = 0;i < picsArray.length(); i++) {
                    try {
                        picurls[i] = picsArray.getString(i);
                        //picurls[i] = picurls[i].replace("https","http");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(this, picurls);
                imagePager.setAdapter(mCustomPagerAdapter);
                indicator.setViewPager(imagePager);
                MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),
                        userData.optJSONArray("interests"));
                pager.setAdapter(pagerAdapter);
                pager.setCurrentItem(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            String user_id = getIntent().getStringExtra("user_id");
            Log.e("ASd",user_id);
            AndroidNetworking.post(User.getInstance().BASE_URL + "getUserData")
                    .addBodyParameter("user_id", getSPData("user_id"))
                    .addBodyParameter("user_token", getSPData("user_token"))
                    .addBodyParameter("user_id_2", user_id)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // do anything with response

                            Log.e("userdata", response.toString());
                            userData = response.optJSONObject("message");
                            profilename.setText(userData.optString("name") + ", ");
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                            try {
                                Date date = format.parse(String.valueOf(userData.optString("dob")));
                                profilename.append(User.getInstance().getAge(date.getYear() + 1900,
                                        date.getMonth(), date.getDay()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            city.setText(userData.optString("currentCity"));


                            //images on top
                            JSONArray picsArray = userData.optJSONArray("pictures");

                            String pics = userData.optString("pictures").replace("\\","");
                            //String[] picurls = pics.substring(1, pics.length()-1).split(",", 4);
                            String[] picurls = {"", "", "", ""};

                            for(int i = 0;i < picsArray.length(); i++) {
                                try {
                                    picurls[i] = picsArray.getString(i);
                                    //picurls[i] = picurls[i].replace("https","http");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(getApplicationContext(), picurls);
                            imagePager.setAdapter(mCustomPagerAdapter);
                            indicator.setViewPager(imagePager);
                            MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),
                                    userData.optJSONArray("interests"));
                            pager.setAdapter(pagerAdapter);
                            pager.setCurrentItem(0);
                        }
                        @Override
                        public void onError(ANError error) {

                            error.printStackTrace();

                        }
                    });
        }




        if(myprofile.equals("true")) {
            bookmarkbtn.setVisibility(View.GONE);
            editbtn.setVisibility(View.VISIBLE);
            actionButton.setVisibility(View.GONE);
            actionButton2.setVisibility(View.GONE);
        } else {
            editbtn.setVisibility(View.GONE);
            bookmarkbtn.setVisibility(View.VISIBLE);
            actionButton.setVisibility(View.VISIBLE);
            actionButton2.setVisibility(View.VISIBLE);
        }

        bookmarkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!ct) {
                    bookmarkbtn.setImageResource(R.drawable.bookmark_full);
                    ct = true;
                } else {
                    bookmarkbtn.setImageResource(R.drawable.bookmark_empty);
                    ct = false;
                }

            }
        });


        profile_sliding_layout.setPanelHeight((int)Math.round(height*0.45));
        profile_sliding_layout.setParallaxOffset(150);

        maininfo = (RelativeLayout) findViewById(R.id.maininfo);
        SlidingUpPanelLayout.LayoutParams params = new SlidingUpPanelLayout.LayoutParams(
                SlidingUpPanelLayout.LayoutParams.MATCH_PARENT,
                SlidingUpPanelLayout.LayoutParams.MATCH_PARENT
        );
        params.setMargins(0, getStatusBarHeight(), 0, 0);
        maininfo.setLayoutParams(params);

        SlidingUpPanelLayout.PanelSlideListener panelSlideListener = new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

                if(newState.equals(SlidingUpPanelLayout.PanelState.EXPANDED) || newState.equals(SlidingUpPanelLayout.PanelState.DRAGGING)) {
                    getSupportActionBar().hide();
                } else {
                    getSupportActionBar().show();
                }
            }
        };

        profile_sliding_layout.addPanelSlideListener(panelSlideListener);

        //actionButton.hide();
        actionButton.setType(ActionButton.Type.DEFAULT);
        //actionButton.setSize(65.0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            actionButton.setButtonColor(Color.parseColor("#EA5251"));
        }
        actionButton.setRippleEffectEnabled(true);
        actionButton.playShowAnimation();
        actionButton.setImageResource(R.drawable.ic_action_like);


        // actionButton.hide();
        actionButton2.setType(ActionButton.Type.DEFAULT);
        //actionButton.setSize(65.0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            actionButton2.setButtonColor(Color.parseColor("#FF4A4A4A"));
        }
        actionButton2.setRippleEffectEnabled(true);
        actionButton2.playShowAnimation();
        actionButton2.setImageResource(R.drawable.ic_action_dislike);

        final Animation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(200);
        anim.setRepeatCount(1);
        anim.setRepeatMode(Animation.REVERSE);


        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        pagertextindicator = findViewById(R.id.pagertextindicator);
        final ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        one.setBackgroundColor(Color.parseColor("#4A90E2"));
                        two.setBackgroundColor(Color.parseColor("#60505154"));
                        three.setBackgroundColor(Color.parseColor("#60505154"));

                        anim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                                pagertextindicator.setText("INTERESTS");
                            }
                        });
                        pagertextindicator.startAnimation(anim);

                        break;
                    case 1:
                        one.setBackgroundColor(Color.parseColor("#60505154"));
                        two.setBackgroundColor(Color.parseColor("#4A90E2"));
                        three.setBackgroundColor(Color.parseColor("#60505154"));

                        anim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                                pagertextindicator.setText("INFO");
                            }
                        });
                        pagertextindicator.startAnimation(anim);

                        break;
                    case 2:
                        one.setBackgroundColor(Color.parseColor("#60505154"));
                        two.setBackgroundColor(Color.parseColor("#60505154"));
                        three.setBackgroundColor(Color.parseColor("#4A90E2"));

                        anim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                                pagertextindicator.setText("BIO");
                            }
                        });
                        pagertextindicator.startAnimation(anim);

                        break;
                    default:
                        one.setBackgroundColor(Color.parseColor("#4A90E2"));
                        two.setBackgroundColor(Color.parseColor("#60505154"));
                        three.setBackgroundColor(Color.parseColor("#60505154"));
                        anim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                                pagertextindicator.setText("INTERESTS");
                            }
                        });
                        pagertextindicator.startAnimation(anim);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        pager.addOnPageChangeListener(changeListener);
        pager.post(new Runnable(){
            @Override
            public void run() {
                changeListener.onPageSelected(pager.getCurrentItem());
            }
        });/*
        changeListener.onPageSelected(pager.getCurrentItem());*/


    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        JSONArray interests;

        public MyPagerAdapter(FragmentManager fm, JSONArray interests) {
            super(fm);
            this.interests = interests;
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

                case 0: return Fragment_profileInterests.newInstance("FirstFragment, Instance 1", interests);
                case 1: return Fragment_profileInfo.newInstance("SecondFragment, Instance 1");
                case 2: return Fragment_profileBio.newInstance("ThirdFragment, Instance 1");
                default: return Fragment_profileInterests.newInstance("ThirdFragment, Default", interests);
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public boolean onCreateOptionsMenu(Menu menu) {

        if(myprofile.equals("false")) {
            MenuInflater mi = getMenuInflater();
            mi.inflate(R.menu.profile_options, menu);
        }

        return super.onCreateOptionsMenu(menu);
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
