package com.einheit.matchezy.bookmarkstab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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


public class LikedRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity c;
    private Context mContext ;
    private List<MatchedProfiles> mData ;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public LikedRecyclerViewAdapter(Context mContext, List<MatchedProfiles> mData, Activity activity) {
        this.mContext = mContext;
        this.mData = mData;
        this.c = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view;
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            view = mInflater.inflate(R.layout.cardview_likes, parent, false);
            return new LikedRecyclerViewAdapter.MyViewHolder(view);
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

            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(mContext);
            circularProgressDrawable.setStrokeWidth(5f);
            circularProgressDrawable.setCenterRadius(25f);
            circularProgressDrawable.setBackgroundColor(R.color.appred);
            circularProgressDrawable.start();
            Glide.with(mContext)
                    .load(mData.get(position).getThumbnail())
                    .apply(new RequestOptions().placeholder(circularProgressDrawable))
                    .into(dataViewHolder.img_book_thumbnail);


            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            try {
                Date date = format.parse(String.valueOf(mData.get(position).getAge()));
                dataViewHolder.name.append(Utility.getInstance().getAge(date.getYear() + 1900,
                        date.getMonth(), date.getDay()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            dataViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(mContext, ProfilePage.class)
                            .putExtra("fromStatusCode", Utility.FROM_LIKED)
                            .putExtra("user_id", mData.get(position).getUser_id())
                            .putExtra("userData", mData.get(position).getUserData());
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
        CardView cardView ;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name) ;
            img_book_thumbnail = (ImageView) itemView.findViewById(R.id.book_img_id);
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

}
