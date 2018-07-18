package com.einheit.matchezy.notificationstab;


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
import com.einheit.matchezy.R;
import com.einheit.matchezy.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_notifications extends android.support.v4.app.Fragment {

    View myView;

    NotificationsRecyclerAdapter notificationsAdapter;
    RecyclerView notifications_recycler;
    JSONArray notifications;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView =  inflater.inflate(R.layout.fragment_notifications, container, false);

        notifications_recycler = myView.findViewById(R.id.notifications_recycler);

        notifications = new JSONArray();

        AndroidNetworking.post(Utility.getInstance().BASE_URL + "getNotifications")
                .addBodyParameter("user_id", getSPData("user_id"))
                .addBodyParameter("user_token", getSPData("user_token"))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject res) {

                        Log.e("ASD", res.toString());

                        if(res.optInt("status_code") == 200) {

                            try {
                                //notifications = new JSONArray("[{\"name\": \"Avinash liked you back\",\"profileImageURL\": \"https://pbs.twimg.com/profile_images/803217914337828864/99oo37KN_400x400.jpg\",\"lastMessage\": \"sunt aut facere repellat provident occaecati excepturi optio reprehenderit\",\"timeStamp\": \"1:05pm\",\"read\": false},{\"name\": \"Aiden wants to message you\",\"profileImageURL\": \"https://www.healthline.com/hlcmsresource/images/topic_centers/parkinsons-disease/400x400_7-Famous-Faces-Parkinsons-Disease-1_Michael_J_Fox.jpg\",\"lastMessage\": \"qui est esse\",\"timeStamp\": \"2:15pm\",\"read\": false},{\"name\": \"Hari wants to \\\"massage\\\" you :P\",\"profileImageURL\": \"https://pbs.twimg.com/profile_images/803217914337828864/99oo37KN_400x400.jpg\",\"lastMessage\": \"ea molestias quasi exercitationem repellat qui ipsa sit aut\",\"timeStamp\": \"3:25pm\",\"read\": false},{\"name\": \"Logesh likes you desperately\",\"profileImageURL\": \"https://pbs.twimg.com/profile_images/803217914337828864/99oo37KN_400x400.jpg\",\"lastMessage\": \"eum et est occaecati\",\"timeStamp\": \"4:35pm\",\"read\": true},{\"name\": \"Avinash is cool\",\"profileImageURL\": \"https://www.healthline.com/hlcmsresource/images/topic_centers/parkinsons-disease/400x400_7-Famous-Faces-Parkinsons-Disease-1_Michael_J_Fox.jpg\",\"lastMessage\": \"nesciunt quas odio\",\"timeStamp\": \"5:45pm\",\"read\": true},{\"name\": \"Aiden rocks\",\"profileImageURL\": \"https://pbs.twimg.com/profile_images/803217914337828864/99oo37KN_400x400.jpg\",\"lastMessage\": \"dolorem eum magni eos aperiam quia\",\"timeStamp\": \"6:15pm\",\"read\": true},{\"name\": \"Hari says Mangatha Daw\",\"profileImageURL\": \"https://www.healthline.com/hlcmsresource/images/topic_centers/parkinsons-disease/400x400_7-Famous-Faces-Parkinsons-Disease-1_Michael_J_Fox.jpg\",\"lastMessage\": \"magnam facilis autem\",\"timeStamp\": \"7:05pm\",\"read\": true}]");
                                notifications = res.getJSONArray("message");
                                notificationsAdapter = new NotificationsRecyclerAdapter(notifications, getContext());
                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                                notifications_recycler.setLayoutManager(mLayoutManager);
                                notifications_recycler.setAdapter(notificationsAdapter);
                                notificationsAdapter.notifyDataSetChanged();

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




        return myView;
    }

    private String getSPData(String key) {

        SharedPreferences mUserData = getContext().getSharedPreferences("UserData", MODE_PRIVATE);
        String data = mUserData.getString(key, "");

        return data;

    }

}