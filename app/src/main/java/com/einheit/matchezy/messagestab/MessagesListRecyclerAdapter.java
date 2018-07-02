package com.einheit.matchezy;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.einheit.matchezy.Chat.ChatListItem;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesListRecyclerAdapter extends RecyclerView.Adapter<MessagesListRecyclerAdapter.ViewHolder> {

    private Context mContext;
    List<ChatListItem> chatList = Collections.emptyList();

    public static class ViewHolder extends RecyclerView.ViewHolder {

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


    public MessagesListRecyclerAdapter(List<ChatListItem> chatList, Context context) {
        this.chatList = chatList;
        mContext = context;
    }

    @Override
    public MessagesListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_view, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (!chatList.get(position).isRead()) {
            holder.profilename.setTypeface(null, Typeface.BOLD);
            holder.lastmessage.setTypeface(null, Typeface.BOLD);
        }

        holder.profilename.setText(chatList.get(position).getName());
        holder.lastmessage.setText(chatList.get(position).getLastMessage());
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        Date date = new Date(chatList.get(position).getMessageTime());
        final String time = dateFormat.format(date);
        holder.timestamp.setText(time);
        Glide.with(mContext).load(chatList.get(position).getProfilePic()).into(holder.profileimg);

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}