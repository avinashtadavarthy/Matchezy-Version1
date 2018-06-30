package com.einheit.matchezy;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_profileInfo extends Fragment {

    //dob
    //gender
    //height
    //languagesknown
    //qualification
    //maritalStatus
    //lookingFor
    //religion
    //tattoo
    //piercings
    //collegeName
    //organisationWorked
    //currentDesignation
    //annualIncome


    public Fragment_profileInfo() {
        // Required empty public constructor
    }

    JSONObject userdata;

    ListView listView;

    String ft, inch, langs="", quali="";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_info, container, false);

        try {
            userdata = new JSONObject(getArguments().getString("userdata"));

            ft = userdata.optJSONObject("height").optString("feet");
            inch = userdata.optJSONObject("height").optString("feet");

            int i;

            for(i = 0; i<userdata.optJSONArray("languagesKnown").length(); i++)
                langs = langs + userdata.optJSONArray("languagesKnown").getString(i) + ", ";

            langs = langs.substring(0,langs.length()-2);

            for(i = 0; i<userdata.optJSONArray("qualification").length(); i++)
                quali = quali + userdata.optJSONArray("qualification").getString(i) + ", ";

            quali = quali.substring(0, quali.length()-2);

            //qualifications is also a json array

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<ProfileInfoModel> data = new ArrayList<>();
        data.add(new ProfileInfoModel("Gender", userdata.optString("gender")));
        data.add(new ProfileInfoModel("Height", ft + "\'" + inch + "\""));
        data.add(new ProfileInfoModel("Languages Known", langs));
        data.add(new ProfileInfoModel("Qualifications", quali));
        data.add(new ProfileInfoModel("Marital Status", userdata.optString("maritalStatus")));
        data.add(new ProfileInfoModel("Looking For", userdata.optString("lookingFor")));
        data.add(new ProfileInfoModel("Religion", userdata.optString("religion")));
        data.add(new ProfileInfoModel("College Name", userdata.optString("collegeName")));
        data.add(new ProfileInfoModel("Organisation Working In", userdata.optString("organisationWorked")));
        data.add(new ProfileInfoModel("Current Designation", userdata.optString("currentDesignation")));
        data.add(new ProfileInfoModel("Annual Income", userdata.optString("annualIncome")));
        data.add(new ProfileInfoModel("Tattoo", userdata.optString("tattoo")));
        data.add(new ProfileInfoModel("Piercings", userdata.optString("piercings")));


        listView = v.findViewById(R.id.listView);
        ListAdapter customAdapter = new ListAdapter(getContext(), R.layout.listview_item, data);
        listView.setAdapter(customAdapter);

        View view = getActivity().findViewById(R.id.profile_sliding_layout);
        if( view instanceof SlidingUpPanelLayout ) {
            SlidingUpPanelLayout supl = (SlidingUpPanelLayout) view;
            supl.setScrollableView(listView);
        }

        return v;
    }

    public static Fragment_profileInfo newInstance(String text, JSONObject userdata) {

        Fragment_profileInfo fragment_profileInfo = new Fragment_profileInfo();

         Bundle b = new Bundle();
        b.putString("msg", text);
        b.putString("userdata", userdata.toString());

        fragment_profileInfo.setArguments(b);

        return fragment_profileInfo;
    }




    //populating fields

    public class ProfileInfoModel {
        String attribute, value;

        public ProfileInfoModel(String attribute, String value) {
            this.attribute = attribute;
            this.value = value;
        }
    }

    public class ListAdapter extends ArrayAdapter<ProfileInfoModel> {

        public ListAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public ListAdapter(Context context, int resource, List<ProfileInfoModel> items) {
            super(context, resource, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.listview_item, null);
            }

            ProfileInfoModel p = getItem(position);

            if (p != null) {
                TextView tt1 = (TextView) v.findViewById(R.id.attr);
                TextView tt2 = (TextView) v.findViewById(R.id.value);

                if (tt1 != null) {
                    tt1.setText(p.attribute);
                }

                if (tt2 != null) {
                    tt2.setText(p.value);
                }

            }

            return v;
        }

    }

}
