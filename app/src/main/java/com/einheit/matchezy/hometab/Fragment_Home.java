package com.einheit.matchezy.hometab;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class Fragment_Home extends android.support.v4.app.Fragment {

    View myView;
    List<com.einheit.matchezy.MatchedProfiles> lstMatchedProfiles ;
    RecyclerView horizontal_recycler_view;
    HorizontalRecyclerAdapter horizontalAdapter;
    List<Data> data;

    RecyclerView myrv;
    RecyclerViewAdapter myAdapter;
    JsonObject filterObject;

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

        horizontal_recycler_view = myView.findViewById(R.id.horizontal_recycler_view);
        data = filldata();
        horizontalAdapter=new HorizontalRecyclerAdapter(data, getContext());
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

        filterObject.addProperty("user_id", getSPData("user_id"));
        filterObject.addProperty("user_token", getSPData("user_token"));

        AndroidNetworking.post(Utility.getInstance().BASE_URL + "filterProfiles")
                .addBodyParameter(filterObject).setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject res) {

                        if(res.optInt("status_code") == 200) {
                            Log.e("ASD", res.toString());

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
                                    Log.d(String.valueOf(i), String.valueOf(object.optInt("noOfMatchingInterests" )));
                                    myAdapter.notifyDataSetChanged();


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else
                            Toast.makeText(Fragment_Home.this.getContext(), res.optString("message"), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(ANError error) {
                        error.printStackTrace();
                    }
                });


        myAdapter = new RecyclerViewAdapter(getActivity().getApplicationContext(),lstMatchedProfiles);
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

}
