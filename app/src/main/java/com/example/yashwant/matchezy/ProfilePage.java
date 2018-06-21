package com.example.yashwant.matchezy;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.Switch;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.scalified.fab.ActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONObject;

public class ProfilePage extends FragmentActivity {


    SlidingUpPanelLayout profile_sliding_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        AndroidNetworking.initialize(this);

        AndroidNetworking.post(User.getInstance().BASE_URL + "getUserData")
                .addBodyParameter("user_token", getSPData("user_token"))
                .addBodyParameter("user_id", getSPData("user_id"))
                .addBodyParameter("user_id_2", getSPData("user_id"))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response

                        Log.e("hakuna", response.toString());
                        storeSPData("userdata", response.toString());

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        error.printStackTrace();
                    }
                });

        profile_sliding_layout = (SlidingUpPanelLayout) findViewById(R.id.profile_sliding_layout);
        profile_sliding_layout.setPanelHeight(height/2);
        profile_sliding_layout.setParallaxOffset(150);


       /* final ActionButton actionButton = (ActionButton) findViewById(R.id.action_button_like);
        // actionButton.hide();
        actionButton.setType(ActionButton.Type.DEFAULT);
        //actionButton.setSize(65.0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            actionButton.setButtonColor(Color.parseColor("#EA5251"));
        }
        actionButton.setRippleEffectEnabled(true);
        actionButton.playShowAnimation();
        actionButton.setImageResource(R.drawable.ic_action_like);

        final ActionButton actionButton2 = (ActionButton) findViewById(R.id.action_button_dislike);
        // actionButton.hide();
        actionButton2.setType(ActionButton.Type.DEFAULT);
        //actionButton.setSize(65.0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            actionButton2.setButtonColor(Color.parseColor("#FF4A4A4A"));
        }
        actionButton2.setRippleEffectEnabled(true);
        actionButton2.playShowAnimation();
        actionButton2.setImageResource(R.drawable.ic_action_dislike);

        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));


       switch(pager.getId())
        {
            case 0:
                Toast.makeText(this, "one", Toast.LENGTH_SHORT).show();
                break;

            case 1:
                Toast.makeText(this, "two", Toast.LENGTH_SHORT).show();


        }*/


    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

                case 0: return Fragment_profileInterests.newInstance("FirstFragment, Instance 1");
                case 1: return Fragment_profileInfo.newInstance("SecondFragment, Instance 1");
                case 2: return Fragment_profileBio.newInstance("ThirdFragment, Instance 1");
                default: return Fragment_profileInterests.newInstance("ThirdFragment, Default");
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
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
