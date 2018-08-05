package com.einheit.matchezy.hometab;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.einheit.matchezy.R;

import java.util.Collections;
import java.util.List;

public class HorizontalRecyclerAdapter extends RecyclerView.Adapter<HorizontalRecyclerAdapter.MyViewHolder> {


    List<Fragment_Home.Data> horizontalList = Collections.emptyList();
    Context context;
    private final OnItemClickListener clickListener;

    private int selectedPos = RecyclerView.NO_POSITION;


    public HorizontalRecyclerAdapter(List<Fragment_Home.Data> horizontalList, Context context,
                                     OnItemClickListener clickListener) {
        this.horizontalList = horizontalList;
        this.context = context;
        this.clickListener = clickListener;

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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.homescreen_top_menu, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.itemView.setSelected(selectedPos == position);

        holder.imageView.setImageResource(horizontalList.get(position).imageId);
        holder.txtview.setText(horizontalList.get(position).txt);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                clickListener.onItemClick(horizontalList.get(position).txt);

                selectedPos = holder.getLayoutPosition();
                notifyItemChanged(selectedPos);
            }

        });

    }


    @Override
    public int getItemCount()
    {
        return horizontalList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(String name);
    }

}