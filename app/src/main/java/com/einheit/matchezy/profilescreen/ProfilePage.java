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
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.einheit.matchezy.EncUtil;
import com.einheit.matchezy.HomeScreen;
import com.einheit.matchezy.R;
import com.einheit.matchezy.Utility;
import com.einheit.matchezy.profileoptions.BlockedListScreen;
import com.einheit.matchezy.profileoptions.DislikedScreen;
import com.scalified.fab.ActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.relex.circleindicator.CircleIndicator;

public class ProfilePage extends AppCompatActivity {


    SlidingUpPanelLayout profile_sliding_layout;
    EncUtil.SecretKeys secretKeys;
    RelativeLayout maininfo;
    ImageView bookmarkbtn, editbtn, one, two, three;
    TextView pagertextindicator;
    boolean ct = false;
    ActionButton disLikeFab;
    ActionButton likeFab;
    LinearLayout fabLayout;

    TextView profilename, age, city;

    JSONObject userData;

    int fromStatusCode = 0;

    ViewPager imagePager;
    CircleIndicator indicator;
    ViewPager pager;

    View progressOverlay;

    android.support.v7.widget.ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_button_shadow);

        AndroidNetworking.initialize(this);

        imagePager = (ViewPager) findViewById(R.id.imagePager);
        indicator = (CircleIndicator) findViewById(R.id.indicator);

        pager = (ViewPager) findViewById(R.id.viewPager);

        likeFab = (ActionButton) findViewById(R.id.action_button_like);
        disLikeFab = (ActionButton) findViewById(R.id.action_button_dislike);

        bookmarkbtn = findViewById(R.id.bookmarkbtn);
        editbtn = findViewById(R.id.editbtn);

        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        pagertextindicator = findViewById(R.id.pagertextindicator);

        progressOverlay = findViewById(R.id.progress_overlay);

        try {
            secretKeys = EncUtil.generateKeyFromPassword(Utility.PASS, EncUtil.SALT);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfilePage.this, EditProfile.class);
                startActivity(i);
                finish();
            }
        });

        profile_sliding_layout = (SlidingUpPanelLayout) findViewById(R.id.profile_sliding_layout);
        profile_sliding_layout.setScrollableView(pager);

        fabLayout = findViewById(R.id.fab_buttons_layout);

        profilename = findViewById(R.id.profilename);
        city = findViewById(R.id.city);

        AndroidNetworking.initialize(this);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        fromStatusCode = getIntent().getIntExtra("fromStatusCode", 1);

        if (fromStatusCode != Utility.FROM_SHARED_PROFILE) {
            try {
                if (fromStatusCode == Utility.FROM_PROFILE_PAGE)
                    userData = new JSONObject(getSPData("userData"));
                else userData = new JSONObject(getIntent().getStringExtra("userData"));

                populateFields();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            progressOverlay.setVisibility(View.VISIBLE);
            fabLayout.setVisibility(View.GONE);
            profile_sliding_layout.setVisibility(View.GONE);

            EncUtil.CipherTextIvMac cipherTextIvMac1 = new EncUtil.CipherTextIvMac(
                    getIntent().getStringExtra("queryUserId"));
            String plainText = null;
            try {
                plainText = EncUtil.decryptString(cipherTextIvMac1, secretKeys);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }

                AndroidNetworking.post(Utility.getInstance().BASE_URL + "getUserData")
                        .addBodyParameter("user_id", getSPData("user_id"))
                        .addBodyParameter("user_token", getSPData("user_token"))
                        .addBodyParameter("user_id_2", plainText)
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // do anything with response

                                progressOverlay.setVisibility(View.GONE);
                                fabLayout.setVisibility(View.VISIBLE);
                                profile_sliding_layout.setVisibility(View.VISIBLE);

                                if (response.optInt("status_code") == 200) {
                                    userData = response.optJSONObject("message");

                                    populateFields();
                                } else {
                                    Toast.makeText(ProfilePage.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(ANError error) {

                                error.printStackTrace();

                            }
                        });
        }

        if (getIntent().hasExtra("tag")) {
            if (getIntent().getBooleanExtra("tag", false)) {
                ct = true;
                bookmarkbtn.setImageResource(R.drawable.bookmark_full);
            } else {
                ct = false;
                bookmarkbtn.setImageResource(R.drawable.bookmark_empty);
            }
        }


        if (fromStatusCode == Utility.FROM_PROFILE_PAGE) {
            myProfile();
        } else if (fromStatusCode == Utility.FROM_HOMESCREEN || fromStatusCode == Utility.FROM_SHARED_PROFILE) {
            fromHomeScreen();
        } else if (fromStatusCode == Utility.FROM_BOOKMARKED) {
            fromBookmarkedProfiles();
        } else if (fromStatusCode == Utility.FROM_LIKED) {
            fromLikedProfiles();
        } else if (fromStatusCode == Utility.FROM_MATCHED) {
            fromMatchedProfiles();
        } else if (fromStatusCode == Utility.FROM_DISLIKED) {
            fromDislikedProfiles();
        } else if (fromStatusCode == Utility.FROM_BLOCKED) {
            fromBlockedProfiles();
        }

        bookmarkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!ct) {
                    AndroidNetworking.post(Utility.getInstance().BASE_URL + "bookmarkUser")
                            .addBodyParameter("user_id", getSPData("user_id"))
                            .addBodyParameter("user_token", getSPData("user_token"))
                            .addBodyParameter("user_id_2", userData.optString("user_id"))
                            .setPriority(Priority.HIGH)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject res) {

                                    if (res.optInt("status_code") == 200) {
                                        bookmarkbtn.setImageResource(R.drawable.bookmark_full);
                                        ct = true;
                                        Toast.makeText(ProfilePage.this, res.optString("message"), Toast.LENGTH_SHORT).show();

                                    } else
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

                                    if (res.optInt("status_code") == 200) {
                                        bookmarkbtn.setImageResource(R.drawable.bookmark_empty);
                                        ct = false;
                                        Toast.makeText(ProfilePage.this, res.optString("message"), Toast.LENGTH_SHORT).show();

                                    } else
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


        profile_sliding_layout.setPanelHeight((int) Math.round(height * 0.45));
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

                if (newState.equals(SlidingUpPanelLayout.PanelState.EXPANDED) || newState.equals(SlidingUpPanelLayout.PanelState.DRAGGING)) {
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

                                if (response.optInt("status_code") == 200) {
                                    Log.e("userData", response.toString());
                                    Toast.makeText(ProfilePage.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                                    checkOnBackPressed();
                                    finish();
                                } else {
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

                                if (response.optInt("status_code") == 200) {
                                    Log.e("userData", response.toString());
                                    Toast.makeText(ProfilePage.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                                    checkOnBackPressed();
                                    finish();
                                } else {
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
        pager.post(new Runnable() {
            @Override
            public void run() {
                changeListener.onPageSelected(pager.getCurrentItem());
            }
        });/*
        changeListener.onPageSelected(pager.getCurrentItem());*/


    }

    void populateFields() {
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

        for (int i = 0; i < picsArray.length(); i++) {
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
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        JSONArray interests;
        JSONObject userData;

        public MyPagerAdapter(FragmentManager fm, JSONObject userData, JSONArray interests) {
            super(fm);
            this.userData = userData;
            this.interests = interests;
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {

                case 0:
                    return Fragment_profileInterests.newInstance("FirstFragment, Instance 1", interests, userData.toString());
                case 1:
                    return Fragment_profileInfo.newInstance("SecondFragment, Instance 1", userData);
                case 2:
                    return Fragment_profileBio.newInstance(userData);
                default:
                    return Fragment_profileInterests.newInstance("ThirdFragment, Default", interests, userData.toString());
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

        if (fromStatusCode != Utility.FROM_PROFILE_PAGE) {
            MenuInflater mi = getMenuInflater();
            mi.inflate(R.menu.profile_options, menu);
            mShareActionProvider = (android.support.v7.widget.ShareActionProvider)
                    MenuItemCompat.getActionProvider(menu.findItem(R.id.share_profile));
            if (fromStatusCode == Utility.FROM_BLOCKED) {
                menu.findItem(R.id.unblock).setVisible(true);
                menu.findItem(R.id.block).setVisible(false);
            }
            menu.findItem(R.id.share_profile).setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {

        checkOnBackPressed();

        super.onBackPressed();
    }

    void checkOnBackPressed() {
        if (fromStatusCode == Utility.FROM_HOMESCREEN) {
            Intent intent = new Intent(ProfilePage.this, HomeScreen.class);
            startActivity(intent);
        } else if (fromStatusCode == Utility.FROM_BOOKMARKED) {
            Intent intent = new Intent(ProfilePage.this, HomeScreen.class)
                    .putExtra("notify", "bookmark");
            startActivity(intent);
        } else if (fromStatusCode == Utility.FROM_LIKED) {
            Intent intent = new Intent(ProfilePage.this, HomeScreen.class)
                    .putExtra("notify", "like");
            startActivity(intent);
        } else if (fromStatusCode == Utility.FROM_DISLIKED) {
            Intent intent = new Intent(ProfilePage.this, DislikedScreen.class);
            startActivity(intent);
        } else if (fromStatusCode == Utility.FROM_BLOCKED) {
            Intent intent = new Intent(ProfilePage.this, BlockedListScreen.class);
            startActivity(intent);
        } else if (fromStatusCode == Utility.FROM_PROFILE_PAGE) {
            Intent intent = new Intent(ProfilePage.this, HomeScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.block:
                AndroidNetworking.post(Utility.getInstance().BASE_URL + "blockUser")
                        .addBodyParameter("user_id", getSPData("user_id"))
                        .addBodyParameter("user_token", getSPData("user_token"))
                        .addBodyParameter("user_id_2", userData.optString("user_id"))
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // do anything with response

                                if (response.optInt("status_code") == 200) {
                                    Log.e("userData", response.toString());
                                    Toast.makeText(ProfilePage.this, response.optString("message"), Toast.LENGTH_SHORT).show();

                                    checkOnBackPressed();
                                    finish();
                                } else {
                                    Toast.makeText(ProfilePage.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(ANError error) {

                                error.printStackTrace();

                            }
                        });
                break;
            case R.id.unblock:
                AndroidNetworking.post(Utility.getInstance().BASE_URL + "unBlockUser")
                        .addBodyParameter("user_id", getSPData("user_id"))
                        .addBodyParameter("user_token", getSPData("user_token"))
                        .addBodyParameter("user_id_2", userData.optString("user_id"))
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // do anything with response

                                if (response.optInt("status_code") == 200) {
                                    Log.e("userData", response.toString());
                                    Toast.makeText(ProfilePage.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                                    checkOnBackPressed();
                                    finish();
                                } else {
                                    Toast.makeText(ProfilePage.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(ANError error) {

                                error.printStackTrace();

                            }
                        });
                break;
            case R.id.share_profile:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Matchezy");

                EncUtil.CipherTextIvMac cipherTextIvMac = null;
                try {
                    cipherTextIvMac = EncUtil.encrypt(userData.optString("user_id"), secretKeys);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }
                String cipherTextString = cipherTextIvMac.toString();

                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey, I saw this profile on Matchezy, " +
                        "what do you think about " + userData.optString("name") + " ?\n\n" +
                        "http://matchezy.com/profile?q=" + cipherTextString);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share using"));

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
        likeFab.setVisibility(View.VISIBLE);
        disLikeFab.setVisibility(View.VISIBLE);
    }

    void fromBookmarkedProfiles() {
        editbtn.setVisibility(View.GONE);
        bookmarkbtn.setVisibility(View.VISIBLE);
        likeFab.setVisibility(View.VISIBLE);
        disLikeFab.setVisibility(View.VISIBLE);
    }

    void fromDislikedProfiles() {
        editbtn.setVisibility(View.GONE);
        bookmarkbtn.setVisibility(View.GONE);
        likeFab.setVisibility(View.VISIBLE);
        disLikeFab.setVisibility(View.GONE);
    }

    void fromBlockedProfiles() {
        editbtn.setVisibility(View.GONE);
        bookmarkbtn.setVisibility(View.GONE);
        likeFab.setVisibility(View.GONE);
        disLikeFab.setVisibility(View.GONE);
    }

}
