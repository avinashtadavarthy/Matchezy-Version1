package com.einheit.matchezy.profilescreen;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.einheit.matchezy.R;
import com.einheit.matchezy.Utility;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

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

    JSONObject userData;

    ListView listView;

    String langs="", quali="", colleges = "", orgWorked = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_info, container, false);

        try {
            userData = new JSONObject(getArguments().getString("userData"));

            int i;

            for(i = 0; i<userData.optJSONArray("languagesKnown").length(); i++)
                langs = langs + userData.optJSONArray("languagesKnown").getString(i) + ", ";

            langs = langs.substring(0,langs.length()-2);

            for(i = 0; i<userData.optJSONArray("qualification").length(); i++)
                quali = quali + userData.optJSONArray("qualification").getString(i) + ", ";

            quali = quali.substring(0, quali.length()-2);

            for(i = 0; i<userData.optJSONArray("collegeName").length(); i++)
                colleges = colleges + userData.optJSONArray("collegeName").getString(i) + ", ";

            colleges = colleges.substring(0, colleges.length()-2);
            colleges = colleges.replace("\n", " ");

            for(i = 0; i<userData.optJSONArray("organisationWorked").length(); i++)
                orgWorked = orgWorked + userData.optJSONArray("organisationWorked").getString(i) + ", ";

            orgWorked = orgWorked.substring(0, orgWorked.length()-2);
            orgWorked = orgWorked.replace("\n", " ");

            //qualifications is also a json array

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<ProfileInfoModel> data = new ArrayList<>();
        data.add(new ProfileInfoModel("Gender", userData.optString("gender")));
        data.add(new ProfileInfoModel("Height", userData.optJSONObject("height").optString("feet") + "\'" + userData.optJSONObject("height").optString("inches") + "\""));
        data.add(new ProfileInfoModel("Languages Known", langs));
        if(userData.optBoolean("qualificationVisibility", true))
            data.add(new ProfileInfoModel("Qualifications", quali));
        else data.add(new ProfileInfoModel("Qualifications", Utility.PLACEHOLDER_TEXT_EMPTY_FIELDS));
        data.add(new ProfileInfoModel("Marital Status", userData.optString("maritalStatus")));
        data.add(new ProfileInfoModel("Looking For", userData.optString("lookingFor")));
        if(userData.has("religion"))
            data.add(new ProfileInfoModel("Religion", userData.optString("religion")));
        else data.add(new ProfileInfoModel("Religion", Utility.PLACEHOLDER_TEXT_EMPTY_FIELDS));
        if(userData.optBoolean("collegeNameVisibility", true))
            data.add(new ProfileInfoModel("College Name", colleges));
        else data.add(new ProfileInfoModel("College Name", Utility.PLACEHOLDER_TEXT_EMPTY_FIELDS));
        if(userData.optBoolean("organisationWorkedVisibility", true))
            data.add(new ProfileInfoModel("Work History", orgWorked));
        else data.add(new ProfileInfoModel("Work History", Utility.PLACEHOLDER_TEXT_EMPTY_FIELDS));
        if(userData.optBoolean("currentDesignationVisibility", true)) {
            if(userData.has("currentDesignation"))
                data.add(new ProfileInfoModel("Current Designation", userData.optString("currentDesignation")));
            else data.add(new ProfileInfoModel("Current Designation", Utility.PLACEHOLDER_TEXT_EMPTY_FIELDS));
        }
        else data.add(new ProfileInfoModel("Current Designation", Utility.PLACEHOLDER_TEXT_EMPTY_FIELDS));
        if(userData.optBoolean("annualIncomeVisibility", true)) {
            if(userData.has("annualIncome"))
                data.add(new ProfileInfoModel("Annual Income", userData.optString("annualIncome")));
            else data.add(new ProfileInfoModel("Annual Income", Utility.PLACEHOLDER_TEXT_EMPTY_FIELDS));
        }
        else data.add(new ProfileInfoModel("Annual Income", Utility.PLACEHOLDER_TEXT_EMPTY_FIELDS));
        if(userData.has("tattoo"))
            data.add(new ProfileInfoModel("Tattoo", userData.optString("tattoo")));
        else data.add(new ProfileInfoModel("Tattoo", Utility.PLACEHOLDER_TEXT_EMPTY_FIELDS));
        if(userData.has("piercings"))
            data.add(new ProfileInfoModel("Piercings", userData.optString("piercings")));
        else data.add(new ProfileInfoModel("Piercings", Utility.PLACEHOLDER_TEXT_EMPTY_FIELDS));


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

    public static Fragment_profileInfo newInstance(String text, JSONObject userData) {

        Fragment_profileInfo fragment_profileInfo = new Fragment_profileInfo();

         Bundle b = new Bundle();
        b.putString("msg", text);
        b.putString("userData", userData.toString());

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
