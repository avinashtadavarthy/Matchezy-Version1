package com.example.yashwant.matchezy;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class IntroAdapter extends FragmentPagerAdapter {

    public IntroAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return IntroFragment.newInstance(Color.parseColor("#FFFFFF"), position); // blue
            case 1:
                return IntroFragment.newInstance(Color.parseColor("#FFFFFF"), position); // blue

            default:
                return IntroFragment.newInstance(Color.parseColor("#FFFFFF"), position); // green
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

}