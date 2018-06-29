package com.einheit.matchezy;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_profileBio extends Fragment {


    public Fragment_profileBio() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_bio, container, false);

       /* TextView tv = (TextView) v.findViewById(R.id.tvFragFirst);
        tv.setText(getArguments().getString("msg"));*/

        return v;
    }

    public static Fragment_profileBio newInstance(String text) {

       Fragment_profileBio fragment_profileBio = new Fragment_profileBio();

        /* Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);*/

        return fragment_profileBio;
    }
}
