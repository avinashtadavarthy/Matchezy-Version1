package com.example.yashwant.matchezy;

import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.scalified.fab.ActionButton;

public class ProfilePage extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);


        final ActionButton actionButton = (ActionButton) findViewById(R.id.action_button_like);
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
    }
