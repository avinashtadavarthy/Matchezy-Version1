package com.einheit.matchezy.profilescreen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.einheit.matchezy.R;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_profileBio extends Fragment {

    JSONObject userData;

    public Fragment_profileBio() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_bio, container, false);

        TextView bioTextView = v.findViewById(R.id.bioTextView);

        try {
            userData = new JSONObject(getArguments().getString("userData"));
            bioTextView.setText(userData.optString("bio"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return v;
    }

    public static Fragment_profileBio newInstance(JSONObject userData) {

        Fragment_profileBio fragment_profileBio = new Fragment_profileBio();

        Bundle b = new Bundle();
        b.putString("userData", userData.toString());

        fragment_profileBio.setArguments(b);

        return fragment_profileBio;
    }
}
