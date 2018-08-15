package com.einheit.matchezy.hometab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v4.widget.CircularProgressDrawable;
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
import com.bumptech.glide.request.RequestOptions;
import com.einheit.matchezy.HomeScreen;
import com.einheit.matchezy.MatchedProfiles;
import com.einheit.matchezy.R;
import com.einheit.matchezy.Utility;
import com.einheit.matchezy.profilescreen.EditProfile;
import com.einheit.matchezy.profilescreen.ProfilePage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity c;
    private Context mContext ;
    private List<MatchedProfiles> mData;
    JSONObject userData;

    public static final int VIEW_TYPE_ITEM = 0;
    public static final int VIEW_TYPE_LOADING = 1;
    public static final int VIEW_TYPE_BIO_WARNING = 2;
    public static final int VIEW_TYPE_TITLE = 3;


    public RecyclerViewAdapter(Context mContext, List<MatchedProfiles> mData, Activity activity) {
        this.mContext = mContext;
        this.mData = mData;
        this.c = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view;
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            view = mInflater.inflate(R.layout.cardview_matched_profiles, parent, false);
            return new MyViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_loading_recycler_view, parent, false);
            return new LoadingViewHolder(view);
        } else if (viewType == VIEW_TYPE_BIO_WARNING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_update_bio_warning, parent, false);
            return new UpdateBioViewHolder(view);
        } else if (viewType == VIEW_TYPE_TITLE) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_title_recycler_view, parent, false);
            return new TitleViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof MyViewHolder) {

            final MyViewHolder dataViewHolder = (MyViewHolder) holder;

            String name;
            if (mData.get(position).getName().contains(" ")) {
                name = mData.get(position).getName().split(" ")[0];
            } else {
                name = mData.get(position).getName().split("@")[0];
            }

            dataViewHolder.name.setText(name + ", ");

            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(mContext);
            circularProgressDrawable.setStrokeWidth(5f);
            circularProgressDrawable.setCenterRadius(25f);
            circularProgressDrawable.setBackgroundColor(R.color.appred);
            circularProgressDrawable.start();
            Glide.with(mContext)
                    .load(mData.get(position).getThumbnail())
                    .apply(new RequestOptions().placeholder(circularProgressDrawable))
                    .into(dataViewHolder.img_book_thumbnail);

            if(mData.get(position).getChecked()) {
                dataViewHolder.bookmarkbtn.setImageResource(R.drawable.bookmark_full);
            } else
                dataViewHolder.bookmarkbtn.setImageResource(R.drawable.bookmark_full_grey);


            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            try {
                Date date = format.parse(String.valueOf(mData.get(position).getAge()));
                dataViewHolder.name.append(Utility.getInstance().getAge(date.getYear() + 1900,
                        date.getMonth(), date.getDay()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            dataViewHolder.chipgroup_interests.setSingleLine(true);

            try {
                userData = new JSONObject(mData.get(position).getUserData());
                JSONArray matchingInterests = userData.getJSONArray("matchingInterests");
                JSONArray otherInterests = userData.getJSONArray("otherInterests");

                if (userData.optString("noOfMatchingInterests").equals("0")) {
                    dataViewHolder.matchingIntNoLayout.setVisibility(View.GONE);
                } else {
                    dataViewHolder.matchingnumber.setText(userData.optString("noOfMatchingInterests"));
                }

                dataViewHolder.chipgroup_interests.removeAllViews();

                if (matchingInterests.length() != 0) {
                    for (int i = 0; i < matchingInterests.length(); i++) {

                        Chip chip = new Chip(c);
                        chip.setChipText(matchingInterests.getString(i).toUpperCase());
                        chip.setTextAppearanceResource(R.style.HomepageInterestsStyle);
                        chip.setChipBackgroundColorResource(R.color.appred);
                        chip.setTextStartPadding(1);
                        chip.setTextEndPadding(0);
                        chip.setChipMinHeight(0);
                        chip.setChipCornerRadius(20);
                        /*chip.setChipStrokeColorResource(R.color.orange);
                        chip.setChipStrokeWidth(2);*/

                        dataViewHolder.chipgroup_interests.addView(chip);
                    }
                }

                if (otherInterests.length() != 0) {
                    for (int i = 0; i < otherInterests.length(); i++) {

                        Chip chip = new Chip(c);
                        chip.setChipText(otherInterests.getString(i).toUpperCase());
                        chip.setTextAppearanceResource(R.style.HomepageUnmatchedInterestsStyle);
                        chip.setTextStartPadding(1);
                        chip.setTextEndPadding(0);
                        chip.setChipMinHeight(0);
                        chip.setChipCornerRadius(20);

                        dataViewHolder.chipgroup_interests.addView(chip);
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            dataViewHolder.bookmarkbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        userData = new JSONObject(mData.get(position).getUserData());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (!mData.get(position).getChecked()) {

                        Utility.getInstance().networkCheck(mContext);

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
                                            mData.get(position).setChecked(true);
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
                        Utility.getInstance().networkCheck(mContext);

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
                                            mData.get(position).setChecked(false);
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
                            .putExtra("fromStatusCode", Utility.FROM_HOMESCREEN)
                            .putExtra("user_id", mData.get(position).getUser_id())
                            .putExtra("userData", mData.get(position).getUserData())
                            .putExtra("tag", mData.get(position).getChecked());
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                    c.finish();

                }
            });
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) loadingViewHolder.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);
        } else if (holder instanceof UpdateBioViewHolder) {
            UpdateBioViewHolder viewHolder = (UpdateBioViewHolder) holder;
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);
            viewHolder.closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mData.remove(position);
                    notifyItemRemoved(position);
                }
            });

            viewHolder.updateBioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext, EditProfile.class);
                    mContext.startActivity(i);
                }
            });

        } else if (holder instanceof TitleViewHolder) {
            TitleViewHolder viewHolder = (TitleViewHolder) holder;
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);
            viewHolder.titleTextView.setText(mData.get(position).getThumbnail());
        }



    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mData.size() > 0) {
            if (mData.get(position) == null)
                return VIEW_TYPE_LOADING;
            else if (mData.get(position).getName().equals(Utility.VIEW_TYPE_BIO))
                return VIEW_TYPE_BIO_WARNING;
            else if (mData.get(position).getName().equals(Utility.VIEW_TYPE_TITLE))
                return VIEW_TYPE_TITLE;
            else
                return VIEW_TYPE_ITEM;
        }
        return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView img_book_thumbnail;
        ImageView bookmarkbtn;
        CardView cardView ;
        ChipGroup chipgroup_interests;
        TextView matchingnumber;
        LinearLayout matchingIntNoLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name) ;
            img_book_thumbnail = (ImageView) itemView.findViewById(R.id.book_img_id);
            bookmarkbtn = (ImageView) itemView.findViewById(R.id.bookmarkbtn);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
            chipgroup_interests = (ChipGroup) itemView.findViewById(R.id.chipgp_interests);
            matchingnumber = (TextView) itemView.findViewById(R.id.matchingnumber);
            matchingIntNoLayout = itemView.findViewById(R.id.matchingIntNoLayout);

        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        }
    }

    private class UpdateBioViewHolder extends RecyclerView.ViewHolder {

        LinearLayout closeButton;
        CardView updateBioButton;

        public UpdateBioViewHolder(View view) {
            super(view);

            closeButton = view.findViewById(R.id.closeBioWarning);
            updateBioButton = view.findViewById(R.id.updateBioButton);

        }
    }

    private class TitleViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;

        public TitleViewHolder(View view) {
            super(view);

            titleTextView = view.findViewById(R.id.titleTextView);

        }
    }

    private String getSPData(String key) {

        SharedPreferences mUserData = c.getSharedPreferences("UserData", MODE_PRIVATE);
        String data = mUserData.getString(key, "");

        return data;
    }

}
