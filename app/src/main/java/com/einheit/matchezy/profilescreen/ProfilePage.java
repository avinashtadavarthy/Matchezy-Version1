package com.einheit.matchezy.profilescreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.einheit.matchezy.HomeScreen;
import com.einheit.matchezy.R;
import com.einheit.matchezy.Utility;
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
    ActionButton disLikeFab;
    ActionButton likeFab;

    TextView profilename, age, city;

    JSONObject userData;

    int fromStatusCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrowshadow);

        AndroidNetworking.initialize(this);

        final ViewPager imagePager = (ViewPager) findViewById(R.id.imagePager);
        final CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);

        final ViewPager pager = (ViewPager) findViewById(R.id.viewPager);

        likeFab = (ActionButton) findViewById(R.id.action_button_like);
        disLikeFab = (ActionButton) findViewById(R.id.action_button_dislike);

        bookmarkbtn = findViewById(R.id.bookmarkbtn);
        editbtn = findViewById(R.id.editbtn);

        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfilePage.this, EditProfile.class);
                startActivity(i);
            }
        });

        profile_sliding_layout = (SlidingUpPanelLayout) findViewById(R.id.profile_sliding_layout);
        profile_sliding_layout.setScrollableView(pager);


        profilename = findViewById(R.id.profilename);
        city = findViewById(R.id.city);

        AndroidNetworking.initialize(this);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        myprofile = getIntent().getStringExtra("myprofile");
        fromStatusCode = getIntent().getIntExtra("fromStatusCode", 1);

        try {
            if(fromStatusCode == Utility.FROM_PROFILE_PAGE)
                userData = new JSONObject(getSPData("userdata"));
            else userData = new JSONObject(getIntent().getStringExtra("userData"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(getIntent().hasExtra("tag")) {
            if (getIntent().getBooleanExtra("tag", false)) {
                ct = true;
                bookmarkbtn.setImageResource(R.drawable.bookmark_full);
            } else {
                ct = false;
                bookmarkbtn.setImageResource(R.drawable.bookmark_empty);
            }
        }

        profilename.setText(userData.optString("name") + ", ");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date date = format.parse(String.valueOf(userData.optString("dob")));
            profilename.append(Utility.getInstance().getAge(date.getYear() + 1900,
                    date.getMonth(), date.getDay()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        city.setText(userData.optString("currentCity"));

        JSONArray picsArray = userData.optJSONArray("pictures");

        String[] picurls = {"", "", "", ""};

        for(int i = 0;i < picsArray.length(); i++) {
            try {
                picurls[i] = picsArray.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(this, picurls);
        imagePager.setAdapter(mCustomPagerAdapter);
        indicator.setViewPager(imagePager);
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), userData, userData.optJSONArray("interests"));
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(0);

        if(fromStatusCode == Utility.FROM_PROFILE_PAGE) {
            myProfile();
        } else if(fromStatusCode == Utility.FROM_HOMESCREEN){
            fromHomeScreen();
        } else if (fromStatusCode == Utility.FROM_BOOKMARKED) {
            fromBookmarkedProfiles();
        } else if (fromStatusCode == Utility.FROM_LIKED) {
            fromLikedProfiles();
        } else if (fromStatusCode == Utility.FROM_MATCHED) {
            fromMatchedProfiles();
        }

        bookmarkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!ct) {
                    AndroidNetworking.post(Utility.getInstance().BASE_URL + "bookmarkUser")
                            .addBodyParameter("user_id", getSPData("user_id"))
                            .addBodyParameter("user_token", getSPData("user_token"))
                            .addBodyParameter("user_id_2", userData.optString("user_id"))
                            .setPriority(Priority.HIGH)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject res) {

                                    if(res.optInt("status_code") == 200) {
                                        bookmarkbtn.setImageResource(R.drawable.bookmark_full);
                                        ct = true;
                                        Toast.makeText(ProfilePage.this, res.optString("message"), Toast.LENGTH_SHORT).show();

                                    }
                                    else
                                        Toast.makeText(ProfilePage.this, res.optString("message"), Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onError(ANError error) {
                                    error.printStackTrace();
                                }
                            });

                } else {
                    AndroidNetworking.post(Utility.getInstance().BASE_URL + "unBookmarkUser")
                            .addBodyParameter("user_id", getSPData("user_id"))
                            .addBodyParameter("user_token", getSPData("user_token"))
                            .addBodyParameter("user_id_2", userData.optString("user_id"))
                            .setPriority(Priority.HIGH)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject res) {

                                    if(res.optInt("status_code") == 200) {
                                        bookmarkbtn.setImageResource(R.drawable.bookmark_empty);
                                        ct = false;
                                        Toast.makeText(ProfilePage.this, res.optString("message"), Toast.LENGTH_SHORT).show();

                                    }
                                    else
                                        Toast.makeText(ProfilePage.this, res.optString("message"), Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onError(ANError error) {
                                    error.printStackTrace();
                                }
                            });

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
        likeFab.setType(ActionButton.Type.DEFAULT);
        //actionButton.setSize(65.0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            likeFab.setButtonColor(Color.parseColor("#EA5251"));
        }
        likeFab.setRippleEffectEnabled(true);
        likeFab.playShowAnimation();
        likeFab.setImageResource(R.drawable.ic_action_like);


        // actionButton.hide();
        disLikeFab.setType(ActionButton.Type.DEFAULT);
        //actionButton.setSize(65.0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            disLikeFab.setButtonColor(Color.parseColor("#FF4A4A4A"));
        }
        disLikeFab.setRippleEffectEnabled(true);
        disLikeFab.playShowAnimation();
        disLikeFab.setImageResource(R.drawable.ic_action_dislike);

        likeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidNetworking.post(Utility.getInstance().BASE_URL + "likeUser")
                        .addBodyParameter("user_id", getSPData("user_id"))
                        .addBodyParameter("user_token", getSPData("user_token"))
                        .addBodyParameter("user_id_2", userData.optString("user_id"))
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // do anything with response

                                if(response.optInt("status_code") == 200) {
                                    Log.e("userdata", response.toString());
                                    Toast.makeText(ProfilePage.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ProfilePage.this, HomeScreen.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    Toast.makeText(ProfilePage.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onError(ANError error) {

                                error.printStackTrace();

                            }
                        });
            }
        });

        disLikeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidNetworking.post(Utility.getInstance().BASE_URL + "disLikeUser")
                        .addBodyParameter("user_id", getSPData("user_id"))
                        .addBodyParameter("user_token", getSPData("user_token"))
                        .addBodyParameter("user_id_2", userData.optString("user_id"))
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // do anything with response

                                if(response.optInt("status_code") == 200) {
                                    Log.e("userdata", response.toString());
                                    Toast.makeText(ProfilePage.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ProfilePage.this, HomeScreen.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    Toast.makeText(ProfilePage.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onError(ANError error) {

                                error.printStackTrace();

                            }
                        });
            }
        });

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
        JSONObject userdata;

        public MyPagerAdapter(FragmentManager fm, JSONObject userdata, JSONArray interests) {
            super(fm);
            this.userdata = userdata;
            this.interests = interests;
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

                case 0: return Fragment_profileInterests.newInstance("FirstFragment, Instance 1", interests, userdata.toString());
                case 1: return Fragment_profileInfo.newInstance("SecondFragment, Instance 1",userdata);
                case 2: return Fragment_profileBio.newInstance("ThirdFragment, Instance 1");
                default: return Fragment_profileInterests.newInstance("ThirdFragment, Default", interests, userdata.toString());
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

        if(fromStatusCode != Utility.FROM_PROFILE_PAGE) {
            MenuInflater mi = getMenuInflater();
            mi.inflate(R.menu.profile_options, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
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

    void fromMatchedProfiles() {
        bookmarkbtn.setVisibility(View.GONE);
        likeFab.setVisibility(View.GONE);
        editbtn.setVisibility(View.GONE);
        disLikeFab.setVisibility(View.VISIBLE);
    }

    void myProfile() {
        bookmarkbtn.setVisibility(View.GONE);
        editbtn.setVisibility(View.VISIBLE);
        likeFab.setVisibility(View.GONE);
        disLikeFab.setVisibility(View.GONE);
    }

    void fromHomeScreen() {
        editbtn.setVisibility(View.GONE);
        bookmarkbtn.setVisibility(View.VISIBLE);
        likeFab.setVisibility(View.VISIBLE);
        disLikeFab.setVisibility(View.VISIBLE);
    }

    void fromLikedProfiles() {
        editbtn.setVisibility(View.GONE);
        bookmarkbtn.setVisibility(View.GONE);
        likeFab.setVisibility(View.GONE);
        disLikeFab.setVisibility(View.VISIBLE);
    }

    void fromBookmarkedProfiles() {
        editbtn.setVisibility(View.GONE);
        bookmarkbtn.setVisibility(View.VISIBLE);
        likeFab.setVisibility(View.VISIBLE);
        disLikeFab.setVisibility(View.VISIBLE);
    }

}
