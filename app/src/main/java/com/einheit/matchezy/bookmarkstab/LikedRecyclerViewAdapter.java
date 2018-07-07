package com.einheit.matchezy.bookmarkstab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.einheit.matchezy.MatchedProfiles;
import com.einheit.matchezy.R;
import com.einheit.matchezy.Utility;
import com.einheit.matchezy.hometab.RecyclerViewAdapter;
import com.einheit.matchezy.profilescreen.ProfilePage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class LikedRecyclerViewAdapter extends RecyclerView.Adapter<LikedRecyclerViewAdapter.MyViewHolder> {

    private Activity c;
    private Context mContext ;
    private List<MatchedProfiles> mData ;


    public LikedRecyclerViewAdapter(Context mContext, List<MatchedProfiles> mData, Activity activity) {
        this.mContext = mContext;
        this.mData = mData;
        this.c = activity;
    }

    @Override
    public LikedRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_matched_profiles,parent,false);
        return new LikedRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LikedRecyclerViewAdapter.MyViewHolder holder, final int position) {

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
            JSONObject userData = new JSONObject(mData.get(position).getUserData());
            JSONArray matchingInterests = userData.getJSONArray("matchingInterests");
            JSONArray otherInterests = userData.getJSONArray("otherInterests");

            if (userData.optString("noOfMatchingInterests").equals("0")) {
                holder.matchinglayout.setVisibility(View.GONE);
            } else {
                holder.matchingnumber.setText(userData.optString("noOfMatchingInterests"));
            }


            if(matchingInterests.length() != 0) {
                for(int i = 0; i<matchingInterests.length(); i++) {

                    Chip chip = new Chip(c);
                    chip.setChipText(matchingInterests.getString(i).toUpperCase());
                    chip.setChipBackgroundColorResource(R.color.appdarkred);
                    chip.setTextAppearanceResource(R.style.HomepageInterestsStyle);
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
                    chip.setTextAppearanceResource(R.style.HomepageUnmatchedInterestsStyle);
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

                if (holder.bookmarkbtn.getTag().equals("empty")) {
                    holder.bookmarkbtn.setTag("full");
                    holder.bookmarkbtn.setImageResource(R.drawable.bookmark_full);
                } else {
                    holder.bookmarkbtn.setTag("empty");
                    holder.bookmarkbtn.setImageResource(R.drawable.bookmark_full_grey);
                }

            }
        });


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mContext, ProfilePage.class)
                        .putExtra("fromStatusCode", Utility.FROM_LIKED)
                        .putExtra("user_id", mData.get(position).getUser_id())
                        .putExtra("userData", mData.get(position).getUserData());
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

}
