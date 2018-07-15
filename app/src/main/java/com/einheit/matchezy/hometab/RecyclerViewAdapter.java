package com.einheit.matchezy.hometab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.einheit.matchezy.HomeScreen;
import com.einheit.matchezy.MatchedProfiles;
import com.einheit.matchezy.R;
import com.einheit.matchezy.Utility;
import com.einheit.matchezy.profilescreen.ProfilePage;
import com.einheit.matchezy.registration.Registration_Interests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Activity c;
    private Context mContext ;
    private List<MatchedProfiles> mData;
    JSONObject userData;


    public RecyclerViewAdapter(Context mContext, List<MatchedProfiles> mData, Activity activity) {
        this.mContext = mContext;
        this.mData = mData;
        this.c = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_matched_profiles,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        String name;
        if(mData.get(position).getName().contains(" ")) {
            name = mData.get(position).getName().split(" ")[0];
        } else {
            name = mData.get(position).getName().split("@")[0];
        }

        holder.name.setText(name + ", ");
        Glide.with(mContext)
                .load(mData.get(position).getThumbnail())
                .into(holder.img_book_thumbnail);


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date date = format.parse(String.valueOf(mData.get(position).getAge()));
            holder.name.append(Utility.getInstance().getAge(date.getYear() + 1900,
                    date.getMonth(), date.getDay()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.chipgroup_interests.setSingleLine(true);

        try {
            userData = new JSONObject(mData.get(position).getUserData());
            JSONArray matchingInterests = userData.getJSONArray("matchingInterests");
            JSONArray otherInterests = userData.getJSONArray("otherInterests");

            Log.e("ASD", "MI - " + matchingInterests.length() + " - " + matchingInterests.toString());
            Log.e("ASD", "OI - " + otherInterests.length() + " - " + otherInterests.toString());

            if (userData.optString("noOfMatchingInterests").equals("0")) {
                holder.matchinglayout.setVisibility(View.GONE);
            } else {
                holder.matchingnumber.setText(userData.optString("noOfMatchingInterests"));
            }

            holder.chipgroup_interests.removeAllViews();

            if(matchingInterests.length() != 0) {
                for(int i = 0; i<matchingInterests.length(); i++) {

                    Chip chip = new Chip(c);
                    chip.setChipText(matchingInterests.getString(i).toUpperCase());
                    chip.setChipStrokeColorResource(R.color.orange);
                    chip.setTextAppearanceResource(R.style.HomepageInterestsStyle);
                    chip.setChipBackgroundColorResource(android.R.color.transparent);
                    chip.setChipStrokeWidth(2);
                    chip.setTextStartPadding(1);
                    chip.setTextEndPadding(0);
                    chip.setChipMinHeight(0);
                    chip.setChipCornerRadius(20);

                    holder.chipgroup_interests.addView(chip);
                }
            }

            if (otherInterests.length() != 0) {
                for(int i = 0; i<otherInterests.length(); i++) {

                    Chip chip = new Chip(c);
                    chip.setChipText(otherInterests.getString(i).toUpperCase());
                    chip.setChipStrokeColorResource(R.color.lightgrey);
                    chip.setTextAppearanceResource(R.style.HomepageUnmatchedInterestsStyle);
                    chip.setChipBackgroundColorResource(android.R.color.transparent);
                    chip.setChipStrokeWidth(2);
                    chip.setTextStartPadding(1);
                    chip.setTextEndPadding(0);
                    chip.setChipMinHeight(0);
                    chip.setChipCornerRadius(20);

                    holder.chipgroup_interests.addView(chip);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.bookmarkbtn.setTag("empty");

        holder.bookmarkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    userData = new JSONObject(mData.get(position).getUserData());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (holder.bookmarkbtn.getTag().equals("empty")) {
                    AndroidNetworking.post(Utility.getInstance().BASE_URL + "bookmarkUser")
                            .addBodyParameter("user_id", getSPData("user_id"))
                            .addBodyParameter("user_token", getSPData("user_token"))
                            .addBodyParameter("user_id_2", userData.optString("user_id"))
                            .setPriority(Priority.HIGH)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject res) {

                                    if(res.optInt("status_code") == 200) {
                                        holder.bookmarkbtn.setTag("full");
                                        holder.bookmarkbtn.setImageResource(R.drawable.bookmark_full);
                                        Toast.makeText(mContext, res.optString("message"), Toast.LENGTH_SHORT).show();

                                    }
                                    else
                                        Toast.makeText(mContext, res.optString("message"), Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onError(ANError error) {
                                    error.printStackTrace();
                                }
                            });
                } else {
                    AndroidNetworking.post(Utility.getInstance().BASE_URL + "unBookmarkUser")
                            .addBodyParameter("user_id", getSPData("user_id"))
                            .addBodyParameter("user_token", getSPData("user_token"))
                            .addBodyParameter("user_id_2", userData.optString("user_id"))
                            .setPriority(Priority.HIGH)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject res) {

                                    if(res.optInt("status_code") == 200) {
                                        holder.bookmarkbtn.setTag("empty");
                                        holder.bookmarkbtn.setImageResource(R.drawable.bookmark_full_grey);
                                        Toast.makeText(mContext, res.optString("message"), Toast.LENGTH_SHORT).show();

                                    }
                                    else
                                        Toast.makeText(mContext, res.optString("message"), Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onError(ANError error) {
                                    error.printStackTrace();
                                }
                            });
                }

            }
        });


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mContext, ProfilePage.class)
                        .putExtra("fromStatusCode", Utility.FROM_HOMESCREEN)
                        .putExtra("user_id", mData.get(position).getUser_id())
                        .putExtra("userData", mData.get(position).getUserData())
                        .putExtra("tag", holder.bookmarkbtn.getTag().toString());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);

            }
        });



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView img_book_thumbnail;
        ImageView bookmarkbtn;
        CardView cardView ;
        ChipGroup chipgroup_interests;
        LinearLayout matchinglayout;
        TextView matchingnumber;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name) ;
            img_book_thumbnail = (ImageView) itemView.findViewById(R.id.book_img_id);
            bookmarkbtn = (ImageView) itemView.findViewById(R.id.bookmarkbtn);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
            chipgroup_interests = (ChipGroup) itemView.findViewById(R.id.chipgp_interests);
            matchinglayout = (LinearLayout) itemView.findViewById(R.id.matchinglayout);
            matchingnumber = (TextView) itemView.findViewById(R.id.matchingnumber);

        }
    }

    private String getSPData(String key) {

        SharedPreferences mUserData = c.getSharedPreferences("UserData", MODE_PRIVATE);
        String data = mUserData.getString(key, "");

        return data;
    }

}
