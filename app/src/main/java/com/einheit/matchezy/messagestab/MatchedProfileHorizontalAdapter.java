package com.einheit.matchezy.messagestab;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.einheit.matchezy.MatchedProfiles;
import com.einheit.matchezy.R;

import java.util.Collections;
import java.util.List;

public class MatchedProfileHorizontalAdapter extends RecyclerView.Adapter<MatchedProfileHorizontalAdapter.MyViewHolder> {


    List<MatchedProfiles> horizontalList = Collections.emptyList();
    Context context;


    public MatchedProfileHorizontalAdapter(List<MatchedProfiles> horizontalList, Context context) {
        this.horizontalList = horizontalList;
        this.context = context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView txtview;
        public MyViewHolder(View view) {
            super(view);
            imageView=(ImageView) view.findViewById(R.id.imageView);
            txtview=(TextView) view.findViewById(R.id.textView);
        }
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatscreen_top_menu, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Glide.with(context)
                .load(horizontalList.get(position).getThumbnail())
                .into(holder.imageView);

        holder.txtview.setText(horizontalList.get(position).getName());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                /*String list = horizontalList.get(position).txt.toString();
                Toast.makeText(context, list, Toast.LENGTH_SHORT).show();*/
                Intent intent = new Intent(context, Chat.class);
                intent.putExtra("userdata", horizontalList.get(position).getUserData());
                context.startActivity(intent);
            }

        });

    }


    @Override
    public int getItemCount()
    {
        return horizontalList.size();
    }

}