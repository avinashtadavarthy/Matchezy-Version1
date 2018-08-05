package com.einheit.matchezy.profilescreen;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.CircularProgressDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.einheit.matchezy.R;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class CustomPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;

    JSONArray picUrls;

    public CustomPagerAdapter(Context context, JSONArray picUrls) {
        mContext = context;
        this.picUrls = picUrls;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return picUrls.length();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.image_pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(mContext);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(25f);
        circularProgressDrawable.setBackgroundColor(R.color.appred);
        circularProgressDrawable.start();
        try {
            Glide.with(mContext)
                    .load(picUrls.getString(position))
                    .apply(new RequestOptions().placeholder(circularProgressDrawable))
                    .into(imageView);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
