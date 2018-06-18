package com.example.yashwant.matchezy;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_profileInfo extends Fragment {


    public Fragment_profileInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_info, container, false);

       /* TextView tv = (TextView) v.findViewById(R.id.tvFragFirst);
        tv.setText(getArguments().getString("msg"));*/

        return v;
    }

    public static Fragment_profileInfo newInstance(String text) {

        Fragment_profileInfo fragment_profileInfo = new Fragment_profileInfo();

        /* Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);*/

        return fragment_profileInfo;
    }

}
