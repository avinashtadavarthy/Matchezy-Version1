package com.einheit.matchezy.hometab;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipDrawable;
import android.support.design.chip.ChipGroup;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.einheit.matchezy.R;
import com.einheit.matchezy.Utility;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scalified.fab.ActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Home extends android.support.v4.app.Fragment implements HorizontalRecyclerAdapter.OnItemClickListener{

    View myView;
    List<com.einheit.matchezy.MatchedProfiles> lstMatchedProfiles ;
    RecyclerView horizontal_recycler_view;
    HorizontalRecyclerAdapter horizontalAdapter;
    List<Data> data;

    View progressOverlay;

    RecyclerView myrv;
    RecyclerViewAdapter myAdapter;
    JsonObject filterObject;
    TextView recommendedTextView;

    public Fragment_Home() {
        // Required empty public constructor
    }

    public static Fragment_Home newInstance() {

        Fragment_Home fragment_home = new Fragment_Home();

        return fragment_home;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView =  inflater.inflate(R.layout.fragment__home, container, false);

        progressOverlay = myView.findViewById(R.id.progress_overlay);
        progressOverlay.setVisibility(View.VISIBLE);

        horizontal_recycler_view = myView.findViewById(R.id.horizontal_recycler_view);
        recommendedTextView = myView.findViewById(R.id.recommendedTextView);

        data = filldata();
        horizontalAdapter=new HorizontalRecyclerAdapter(data, getContext(), this);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);
        horizontal_recycler_view.setAdapter(horizontalAdapter);


        final ActionButton actionButton = (ActionButton) myView.findViewById(R.id.action_button_filter);
        actionButton.setType(ActionButton.Type.DEFAULT);
        //actionButton.setSize(65.0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            actionButton.setButtonColor(Color.parseColor("#EA5251"));
        }
        actionButton.setRippleEffectEnabled(true);
        actionButton.playShowAnimation();
        actionButton.setImageResource(R.drawable.filter);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getContext(), Filter.class);
                startActivity(i);

            }
        });

        myrv = (RecyclerView) myView.findViewById(R.id.recyclerview_id);

        lstMatchedProfiles = new ArrayList<>();

        filterObject = null;

        filterObject = new JsonObject();

        if(getSPData("filterObject").length() > 0 && !getSPData("filterObject").isEmpty()) {
            JsonParser parser = new JsonParser();
            filterObject = parser.parse(getSPData("filterObject")).getAsJsonObject();
        }

        filterProfiles(filterObject);

        myAdapter = new RecyclerViewAdapter(getContext(),lstMatchedProfiles, getActivity());
        myrv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        myrv.setAdapter(myAdapter);


        return myView;
    }


    public List<Data> filldata() {

        List<Data> data = new ArrayList<>();

        data.add(new Data( R.drawable.photography, "Photography"));
        data.add(new Data( R.drawable.pets, "Pets"));
        data.add(new Data( R.drawable.books, "Books"));
        data.add(new Data( R.drawable.travel, "Travel"));
        data.add(new Data( R.drawable.philosophy, "Philosophy"));
        data.add(new Data( R.drawable.history, "History"));


        return data;
    }

    @Override
    public void onItemClick(String name) {
        filterObject = new JsonObject();

        storeSPData("filterObject",filterObject.toString());

        filterObject.addProperty("interests", "[" + name + "]");

        recommendedTextView.setText(name);

        lstMatchedProfiles.clear();
        myAdapter.notifyDataSetChanged();

        filterProfiles(filterObject);
    }

    private void filterProfiles(JsonObject filterObject) {

        filterObject.addProperty("user_id", getSPData("user_id"));
        filterObject.addProperty("user_token", getSPData("user_token"));

        myAdapter = new RecyclerViewAdapter(getContext(),lstMatchedProfiles, getActivity());
        myrv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        myrv.setAdapter(myAdapter);

        AndroidNetworking.post(Utility.getInstance().BASE_URL + "filterProfiles")
                .addBodyParameter(filterObject).setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject res) {

                        if(res.optInt("status_code") == 200) {
                            //Log.e("ASD", res.toString());

                            progressOverlay.setVisibility(View.GONE);

                            try {
                                JSONArray profilesArray = res.getJSONArray("message");
                                for (int i = 0; i < profilesArray.length(); i++) {
                                    JSONObject object = (JSONObject) profilesArray.get(i);
                                    lstMatchedProfiles.add(new com.einheit.matchezy.MatchedProfiles(
                                            object.optString("_id"),
                                            object.optString("name"),
                                            object.optString("profileImageURL"),
                                            object.optString("dob"),
                                            object.optJSONArray("interests"),
                                            object.toString()));
                                    Log.e(String.valueOf(i), String.valueOf(object.optInt("noOfMatchingInterests" )));
                                    myAdapter.notifyDataSetChanged();


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        else
                            Toast.makeText(getContext(), res.optString("message"), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(ANError error) {
                        error.printStackTrace();
                    }
                });
    }


    public class Data {
        public int imageId;
        public String txt;

        Data(int imageId, String text) {

            this.imageId = imageId;
            this.txt=text;
        }
    }


    private String getSPData(String key) {

        SharedPreferences mUserData = getActivity().getSharedPreferences("UserData", MODE_PRIVATE);
        String data = mUserData.getString(key, "");

        return data;
    }

    private void storeSPData(String key, String data) {

        SharedPreferences mUserData = getActivity().getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor mUserEditor = mUserData.edit();
        mUserEditor.putString(key, data);
        mUserEditor.commit();

    }

}
