package com.einheit.matchezy;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
    RecyclerView myrv;
    com.einheit.matchezy.RecyclerViewAdapter myAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView =  inflater.inflate(R.layout.fragment__home, container, false);

        final ActionButton actionButton = (ActionButton) myView.findViewById(R.id.action_button_filter);
        actionButton.setType(ActionButton.Type.DEFAULT);
        actionButton.setSize(65.0f);
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

        JsonObject o = new JsonObject();

            o.addProperty("user_id", getSPData("user_id"));
            o.addProperty("user_token", getSPData("user_token"));
            o.addProperty("lookingFor", "Both");/*
            o.addProperty("interests", "[Tv]");*/

        AndroidNetworking.post(com.einheit.matchezy.User.getInstance().BASE_URL + "filterProfiles")
                .addBodyParameter(o)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject res) {

                        if(res.optInt("status_code") == 200) {

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


        myAdapter = new com.einheit.matchezy.RecyclerViewAdapter(getActivity().getApplicationContext(),lstMatchedProfiles);
        myrv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        myrv.setAdapter(myAdapter);

        return myView;
    }

    private String getSPData(String key) {

        SharedPreferences mUserData = getActivity().getSharedPreferences("UserData", MODE_PRIVATE);
        String data = mUserData.getString(key, "");

        return data;
    }

}
