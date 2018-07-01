package com.einheit.matchezy;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesListRecyclerAdapter extends RecyclerView.Adapter<MessagesListRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private JSONArray mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView profilename, lastmessage, timestamp;
        public CircleImageView profileimg;
        public ViewHolder(View v) {
            super(v);
            profilename = v.findViewById(R.id.profilename);
            lastmessage = v.findViewById(R.id.lastmessage);
            timestamp = v.findViewById(R.id.timestamp);
            profileimg = v.findViewById(R.id.profileimg);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MessagesListRecyclerAdapter(JSONArray myDataset, Context context) {
        mDataset = myDataset;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MessagesListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_view, parent, false);

        return new ViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        try {

            if (mDataset.getJSONObject(position).optString("read").equals("true")) {
                holder.profilename.setTypeface(null, Typeface.BOLD);
                holder.lastmessage.setTypeface(null, Typeface.BOLD);
            }

            holder.profilename.setText(mDataset.getJSONObject(position).optString("name"));
            holder.lastmessage.setText(mDataset.getJSONObject(position).optString("lastMessage"));
            holder.timestamp.setText(mDataset.getJSONObject(position).optString("timeStamp"));
            Glide.with(mContext).load(mDataset.getJSONObject(position).optString("profileImageURL")).into(holder.profileimg);

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