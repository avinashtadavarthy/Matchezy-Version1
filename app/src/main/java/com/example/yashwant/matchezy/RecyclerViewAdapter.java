package com.example.yashwant.matchezy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext ;
    private List<MatchedProfiles> mData ;


    public RecyclerViewAdapter(Context mContext, List<MatchedProfiles> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_matched_profiles,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.name.setText(mData.get(position).getName() + ", ");
        Glide.with(mContext)
                .load(mData.get(position).getThumbnail())
                .into(holder.img_book_thumbnail);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date date = format.parse(String.valueOf(mData.get(position).getAge()));
            holder.name.append(User.getInstance().getAge(date.getYear() + 1900,
                    date.getMonth(), date.getDay()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.interests.setText( mData.get(position).getInterests().toString()
                .replace('[',' ').replace(']',' ')
                .replace('"',' '));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mContext, ProfilePage.class)
                        .putExtra("myprofile", "false")
                        .putExtra("user_id", mData.get(position).getUser_id())
                        .putExtra("userData", mData.get(position).getUserData());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);

               // Toast.makeText(mContext, String.valueOf(position), Toast.LENGTH_SHORT).show();
              /*  Intent intent = new Intent(mContext,MatchedProfiles_Activity.class);

                // passing data to the book activity
                intent.putExtra("Title",mData.get(position).getTitle());
                intent.putExtra("Description",mData.get(position).getDescription());
                intent.putExtra("Thumbnail",mData.get(position).getThumbnail());
                // start the activity
                mContext.startActivity(intent);*/

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
        CardView cardView ;
        TextView interests;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name) ;
            img_book_thumbnail = (ImageView) itemView.findViewById(R.id.book_img_id);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
            interests = itemView.findViewById(R.id.interests);

        }
    }


}
