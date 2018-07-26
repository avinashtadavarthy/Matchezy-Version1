package com.einheit.matchezy.bookmarkstab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
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

import static android.content.Context.MODE_PRIVATE;


public class BookmarksRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity c;
    private Context mContext ;
    private List<MatchedProfiles> mData;
    JSONObject userData;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public BookmarksRecyclerViewAdapter(Context mContext, List<MatchedProfiles> mData, Activity activity) {
        this.mContext = mContext;
        this.mData = mData;
        this.c = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view;
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            view = mInflater.inflate(R.layout.cardview_bookmarks, parent, false);
            return new MyViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_loading_recycler_view, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof MyViewHolder) {

            final MyViewHolder dataViewHolder = (MyViewHolder) holder;


            String name;
            if (mData.get(position).getName().contains(" ")) {
                name = mData.get(position).getName().split(" ")[0];
            } else {
                name = mData.get(position).getName().split("@")[0];
            }

            dataViewHolder.name.setText(name + ", ");
            Glide.with(mContext)
                    .load(mData.get(position).getThumbnail())
                    .into(dataViewHolder.img_book_thumbnail);


            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            try {
                Date date = format.parse(String.valueOf(mData.get(position).getAge()));
                dataViewHolder.name.append(Utility.getInstance().getAge(date.getYear() + 1900,
                        date.getMonth(), date.getDay()));
            } catch (ParseException e) {
                e.printStackTrace();
            }


            dataViewHolder.bookmarkbtn.setTag("full");

            dataViewHolder.bookmarkbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        userData = new JSONObject(mData.get(position).getUserData());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (dataViewHolder.bookmarkbtn.getTag().equals("empty")) {
                        AndroidNetworking.post(Utility.getInstance().BASE_URL + "bookmarkUser")
                                .addBodyParameter("user_id", getSPData("user_id"))
                                .addBodyParameter("user_token", getSPData("user_token"))
                                .addBodyParameter("user_id_2", userData.optString("user_id"))
                                .setPriority(Priority.HIGH)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject res) {

                                        if (res.optInt("status_code") == 200) {
                                            dataViewHolder.bookmarkbtn.setTag("full");
                                            dataViewHolder.bookmarkbtn.setImageResource(R.drawable.bookmark_full);
                                            Toast.makeText(mContext, res.optString("message"), Toast.LENGTH_SHORT).show();

                                        } else
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

                                        if (res.optInt("status_code") == 200) {
                                            dataViewHolder.bookmarkbtn.setTag("empty");
                                            dataViewHolder.bookmarkbtn.setImageResource(R.drawable.bookmark_full_grey);
                                            Toast.makeText(mContext, res.optString("message"), Toast.LENGTH_SHORT).show();

                                        } else
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


            dataViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(mContext, ProfilePage.class)
                            .putExtra("fromStatusCode", Utility.FROM_BOOKMARKED)
                            .putExtra("user_id", mData.get(position).getUser_id())
                            .putExtra("userData", mData.get(position).getUserData())
                            .putExtra("tag", dataViewHolder.bookmarkbtn.getTag().toString());
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);

                }
            });

        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) loadingViewHolder.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);
        }

    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView img_book_thumbnail;
        ImageView bookmarkbtn;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            img_book_thumbnail = (ImageView) itemView.findViewById(R.id.book_img_id);
            bookmarkbtn = (ImageView) itemView.findViewById(R.id.bookmarkbtn);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);

        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        }
    }

    private String getSPData(String key) {

        SharedPreferences mUserData = c.getSharedPreferences("UserData", MODE_PRIVATE);
        String data = mUserData.getString(key, "");

        return data;
    }

}
