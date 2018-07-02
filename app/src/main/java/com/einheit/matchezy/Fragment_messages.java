package com.einheit.matchezy;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.einheit.matchezy.Chat.ChatListItem;
import com.einheit.matchezy.Chat.Message;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
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
    List<com.einheit.matchezy.MatchedProfiles> lstMatchedProfiles;
    RecyclerView horizontal_recycler_view, conversations_recycler;
    MatchedProfileHorizontalAdapter horizontalAdapter;
    MessagesListRecyclerAdapter conversationsAdapter;
    private DatabaseReference mFirebaseDatabaseReference;
    List<ChatListItem> chatList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_messages, container, false);

        horizontal_recycler_view = myView.findViewById(R.id.horizontal_recycler_view);
        conversations_recycler = myView.findViewById(R.id.conversations_recycler);

        lstMatchedProfiles = new ArrayList<>();
        chatList = new ArrayList<>();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

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

                        if (res.optInt("status_code") == 200) {

                            Log.e("ASD", res.toString());

                            try {
                                JSONArray profilesArray = res.getJSONArray("message");
                                for (int i = 0; i < profilesArray.length(); i++) {
                                    final JSONObject object = (JSONObject) profilesArray.get(i);
                                    lstMatchedProfiles.add(new com.einheit.matchezy.MatchedProfiles(
                                            object.optString("_id"),
                                            object.optString("name"),
                                            object.optString("profileImageURL"),
                                            object.optString("dob"),
                                            object.optJSONArray("interests"),
                                            object.toString()));

                                    Query query = mFirebaseDatabaseReference.child(object.optString("matched_id")).limitToLast(1);

                                    query.addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                            if (dataSnapshot.exists()) {

                                                DataSnapshot issue = dataSnapshot;
                                                boolean isFound = false;
                                                for (int i = 0; i < chatList.size(); i++) {
                                                    if (chatList.get(i).getName().equals(object.optString("name"))) {
                                                        chatList.get(i).setLastMessage(issue.getValue(Message.class).getText());
                                                        chatList.get(i).setMessageTime(issue.getValue(Message.class).getMessageTime());
                                                        conversationsAdapter.notifyDataSetChanged();
                                                        isFound = true;
                                                    }
                                                }
                                                if(!isFound) {
                                                    chatList.add(new ChatListItem(
                                                        object.optString("name"),
                                                        object.optString("profileImageURL"),
                                                        issue.getValue(Message.class).getText(),
                                                        issue.getValue(Message.class).getMessageTime(),
                                                        false));
                                                    conversationsAdapter.notifyDataSetChanged();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                        }

                                        @Override
                                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                    horizontalAdapter.notifyDataSetChanged();


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else
                            Toast.makeText(Fragment_messages.this.getContext(), res.optString("message"), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(ANError error) {
                        error.printStackTrace();
                    }
                });

        horizontalAdapter = new MatchedProfileHorizontalAdapter(lstMatchedProfiles, getContext());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);
        horizontal_recycler_view.setAdapter(horizontalAdapter);

        conversationsAdapter = new MessagesListRecyclerAdapter(chatList, getContext());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        conversations_recycler.setLayoutManager(mLayoutManager);
        conversations_recycler.setAdapter(conversationsAdapter);

        return myView;
    }

    private String getSPData(String key) {

        SharedPreferences mUserData = getActivity().getSharedPreferences("UserData", MODE_PRIVATE);
        String data = mUserData.getString(key, "");

        return data;
    }

}