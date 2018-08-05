package com.einheit.matchezy.notificationstab;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.einheit.matchezy.R;
import com.einheit.matchezy.Utility;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationsRecyclerAdapter extends RecyclerView.Adapter<NotificationsRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private JSONArray mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView profilename, timestamp;
        public CircleImageView profileimg;
        public ViewHolder(View v) {
            super(v);
            profilename = v.findViewById(R.id.profilename);
            timestamp = v.findViewById(R.id.timestamp);
            profileimg = v.findViewById(R.id.profileimg);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NotificationsRecyclerAdapter(JSONArray myDataset, Context context) {
        mDataset = myDataset;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NotificationsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list_item, parent, false);

        return new ViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        try {

            /*if (mDataset.getJSONObject(position).optString("read").equals("false")) {
                holder.profilename.setTypeface(null, Typeface.BOLD);
                holder.lastmessage.setTypeface(null, Typeface.BOLD);
            }*/

            holder.profilename.setText(mDataset.getJSONObject(position).optString("title"));

            String time = "";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            try {
                Date date = format.parse(mDataset.getJSONObject(position).optString("timeStamp"));
                date = new Date(date.getTime() - (date.getTimezoneOffset() * 60000));
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                time = dateFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.timestamp.setText(time);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.app_logo);
            requestOptions.error(R.drawable.app_logo_nobg);

            Glide.with(mContext)
                    .load(mDataset.getJSONObject(position).optString("profileImageURL"))
                    .apply(requestOptions)
                    .into(holder.profileimg);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length();
    }
}