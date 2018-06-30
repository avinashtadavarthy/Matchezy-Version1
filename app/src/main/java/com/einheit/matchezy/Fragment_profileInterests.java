package com.einheit.matchezy;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v4.app.Fragment;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_profileInterests extends Fragment {


    public Fragment_profileInterests() {
        // Required empty public constructor
    }


    ChipGroup interests_chipgroup;

    JSONArray interests;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_interests, container, false);

        JSONArray interests = null;
        try {

            interests = new JSONArray(getArguments().getString("interests"));
            interests_chipgroup = (ChipGroup) v.findViewById(R.id.interests_chipgroup);
            interests_chipgroup.setChipSpacing(2);
            populateChips(interests);

        } catch (JSONException e) {
            e.printStackTrace();
        }




        return v;
    }

    private int findnumber(String intr) {

        int n=0;

        for(int i=0;i<intr.length();i++) {
            if(intr.charAt(i) == ',')
                n++;
        }

        return n+1;

    }

    private void populateChips(JSONArray arr) throws JSONException {

        for (int i=0;i<arr.length();i++) {
            Chip chip = new Chip(getContext());
            chip.setChipText(arr.getString(i));
            //chip.setCloseIconEnabled(true);
            //chip.setCloseIconResource(R.drawable.fab_add);
            //chip.setChipIconResource(R.drawable.fab_add);
            //chip.setChipBackgroundColorResource(R.color.appred);
            chip.setTextAppearanceResource(R.style.ChipTextStyle);
            //chip.setElevation(15);

            interests_chipgroup.addView(chip);

          /*  chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    interests_chipgroup.removeView(view);
                    Toast.makeText(getContext(), "baba!", Toast.LENGTH_SHORT).show();
                }
            });

            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    interests_chipgroup.removeView(view);
                    Toast.makeText(getContext(), "Removed!", Toast.LENGTH_SHORT).show();
                }
            });*/

        }

    }

    public static Fragment_profileInterests newInstance(String text, JSONArray interests) {

        Fragment_profileInterests fragment_profileInterests = new Fragment_profileInterests();

         Bundle b = new Bundle();
        b.putString("msg", text);
        b.putString("interests", interests.toString());

        fragment_profileInterests.setArguments(b);

        return fragment_profileInterests;
    }

}
