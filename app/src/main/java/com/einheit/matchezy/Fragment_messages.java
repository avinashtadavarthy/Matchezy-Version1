package com.einheit.matchezy;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_messages extends android.support.v4.app.Fragment {

    View myView;
    List<com.einheit.matchezy.MatchedProfiles> lstMatchedProfiles ;
    RecyclerView horizontal_recycler_view, conversations_recycler;
    MatchedProfileHorizontalAdapter horizontalAdapter;
    MessagesListRecyclerAdapter conversationsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView =  inflater.inflate(R.layout.fragment_messages, container, false);

        horizontal_recycler_view = myView.findViewById(R.id.horizontal_recycler_view);
        conversations_recycler = myView.findViewById(R.id.conversations_recycler);

        lstMatchedProfiles = new ArrayList<>();

        JsonObject o = new JsonObject();

        o.addProperty("user_id", getSPData("user_id"));
        o.addProperty("user_token", getSPData("user_token"));
        o.addProperty("lookingFor", "Both");/*
            o.addProperty("interests", "[Tv]");*/

        AndroidNetworking.post(com.einheit.matchezy.User.getInstance().BASE_URL + "getMatchedProfiles")
                .addBodyParameter(o)
                .setPriority(Priority.HIGH)
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
                                    horizontalAdapter.notifyDataSetChanged();


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else
                            Toast.makeText(Fragment_messages.this.getContext(), res.optString("message"), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(ANError error) {
                        error.printStackTrace();
                    }
                });

        horizontalAdapter=new MatchedProfileHorizontalAdapter(lstMatchedProfiles, getContext());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);
        horizontal_recycler_view.setAdapter(horizontalAdapter);


        JSONArray samplejsondata;
            try {
                samplejsondata = new JSONArray("[{\"name\": \"Avinash\",\"profileImageURL\": \"https://pbs.twimg.com/profile_images/803217914337828864/99oo37KN_400x400.jpg\",\"lastMessage\": \"sunt aut facere repellat provident occaecati excepturi optio reprehenderit\",\"timeStamp\": \"1:05pm\",\"read\": true},{\"name\": \"Aiden\",\"profileImageURL\": \"https://www.healthline.com/hlcmsresource/images/topic_centers/parkinsons-disease/400x400_7-Famous-Faces-Parkinsons-Disease-1_Michael_J_Fox.jpg\",\"lastMessage\": \"qui est esse\",\"timeStamp\": \"2:15pm\",\"read\": true},{\"name\": \"Hari\",\"profileImageURL\": \"https://pbs.twimg.com/profile_images/803217914337828864/99oo37KN_400x400.jpg\",\"lastMessage\": \"ea molestias quasi exercitationem repellat qui ipsa sit aut\",\"timeStamp\": \"3:25pm\",\"read\": false},{\"name\": \"Logesh\",\"profileImageURL\": \"https://pbs.twimg.com/profile_images/803217914337828864/99oo37KN_400x400.jpg\",\"lastMessage\": \"eum et est occaecati\",\"timeStamp\": \"4:35pm\",\"read\": true},{\"name\": \"Avinash\",\"profileImageURL\": \"https://www.healthline.com/hlcmsresource/images/topic_centers/parkinsons-disease/400x400_7-Famous-Faces-Parkinsons-Disease-1_Michael_J_Fox.jpg\",\"lastMessage\": \"nesciunt quas odio\",\"timeStamp\": \"5:45pm\",\"read\": false},{\"name\": \"Aiden\",\"profileImageURL\": \"https://pbs.twimg.com/profile_images/803217914337828864/99oo37KN_400x400.jpg\",\"lastMessage\": \"dolorem eum magni eos aperiam quia\",\"timeStamp\": \"6:15pm\",\"read\": false},{\"name\": \"Hari\",\"profileImageURL\": \"https://www.healthline.com/hlcmsresource/images/topic_centers/parkinsons-disease/400x400_7-Famous-Faces-Parkinsons-Disease-1_Michael_J_Fox.jpg\",\"lastMessage\": \"magnam facilis autem\",\"timeStamp\": \"7:05pm\",\"read\": false}]");
                conversationsAdapter = new MessagesListRecyclerAdapter(samplejsondata, getContext());
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                conversations_recycler.setLayoutManager(mLayoutManager);
                conversations_recycler.setAdapter(conversationsAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }



        return myView;
    }

    private String getSPData(String key) {

        SharedPreferences mUserData = getActivity().getSharedPreferences("UserData", MODE_PRIVATE);
        String data = mUserData.getString(key, "");

        return data;
    }

}