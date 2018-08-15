package com.einheit.matchezy.hometab;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipDrawable;
import android.support.design.chip.ChipGroup;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.einheit.matchezy.MatchedProfiles;
import com.einheit.matchezy.R;
import com.einheit.matchezy.Utility;
import com.einheit.matchezy.login.OnboardingNew;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scalified.fab.ActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Home extends android.support.v4.app.Fragment implements HorizontalRecyclerAdapter.OnItemClickListener {

    View myView;

    List<com.einheit.matchezy.MatchedProfiles> lstMatchedProfiles;
    RecyclerView horizontal_recycler_view;
    HorizontalRecyclerAdapter horizontalAdapter;
    List<Data> data;

    RecyclerView myrv;
    RecyclerViewAdapter myAdapter;
    JsonObject filterObject;
    JSONObject userData;

    LinearLayout placeholder;

    RecyclerViewScrollListener scrollListener;
    boolean isBioWarningCard = false;

    private int lastItemCount = 0;

    public Fragment_Home() {
        // Required empty public constructor
    }

    public static Fragment_Home newInstance() {

        Fragment_Home fragment_home = new Fragment_Home();

        return fragment_home;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment__home, container, false);

        placeholder = (LinearLayout) myView.findViewById(R.id.placeholder);

        horizontal_recycler_view = myView.findViewById(R.id.horizontal_recycler_view);

        data = filldata();
        horizontalAdapter = new HorizontalRecyclerAdapter(data, getContext(), this);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);
        horizontal_recycler_view.setAdapter(horizontalAdapter);


        final ActionButton actionButton = (ActionButton) myView.findViewById(R.id.action_button_filter);
        actionButton.setType(ActionButton.Type.DEFAULT);
        //actionButton.setSize(65.0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            actionButton.setButtonColor(Color.parseColor("#EA5251"));
        }
        actionButton.setRippleEffectEnabled(true);
        actionButton.playShowAnimation();
        actionButton.setImageResource(R.drawable.filter);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Filter.class);
                startActivity(i);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                File storageDir = new File(getContext().getCacheDir()
                        + "/MatchEzy");
                try {
                    Utility.deleteDir(storageDir);
                } catch (Exception e) {
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (Utility.getDirSize(getContext().getCacheDir()) > Utility.CACHE_MAX_SIZE) {
                    Utility.deleteDir(getContext().getCacheDir());
                }
            }
        }).start();


        myrv = (RecyclerView) myView.findViewById(R.id.recyclerview_id);

        lstMatchedProfiles = new ArrayList<>();

        myAdapter = new RecyclerViewAdapter(getContext(), lstMatchedProfiles, getActivity());
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        myrv.setLayoutManager(layoutManager);
        myrv.setAdapter(myAdapter);

        filterObject = null;

        lastItemCount = 0;

        filterObject = new JsonObject();

        try {
            userData = new JSONObject(getSPData("userData"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (userData.has("bio")) {
            if (userData.optString("bio").isEmpty() && userData.optString("bio").trim().length() == 0) {
                lstMatchedProfiles.add(new MatchedProfiles(null, Utility.VIEW_TYPE_BIO,
                        null, null, null, null));
                isBioWarningCard = true;
            } else isBioWarningCard = false;
        } else {
            lstMatchedProfiles.add(new MatchedProfiles(null, Utility.VIEW_TYPE_BIO,
                    null, null, null, null));
            isBioWarningCard = true;
        }

        lstMatchedProfiles.add(new MatchedProfiles(null, Utility.VIEW_TYPE_TITLE,
                "Recommended for you", null, null, null));

        if (getSPData("filterObject").length() > 0 && !getSPData("filterObject").isEmpty()) {
            JsonParser parser = new JsonParser();
            filterObject = parser.parse(getSPData("filterObject")).getAsJsonObject();
        }

        filterProfiles(filterObject, lastItemCount);

        scrollListener = new RecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMoreData(myAdapter.getItemCount() - 1);
            }
        };

        myrv.addOnScrollListener(scrollListener);

        return myView;
    }

    public List<Data> filldata() {

        List<Data> data = new ArrayList<>();

        data.add(new Data(R.drawable.animallover, "Animal Lover"));
        data.add(new Data(R.drawable.bibliophile, "Bibliophile"));
        data.add(new Data(R.drawable.sports, "Sports"));
        data.add(new Data(R.drawable.photography, "Photography"));
        data.add(new Data(R.drawable.running, "Running"));
        data.add(new Data(R.drawable.music, "Music"));
        data.add(new Data(R.drawable.travel, "Travelling"));
        data.add(new Data(R.drawable.cinema, "Movie Buff"));
        data.add(new Data(R.drawable.adrenalinejunkie, "Adrenaline Junky"));
        data.add(new Data(R.drawable.potterhead, "Potter Head"));
        data.add(new Data(R.drawable.entrepreneur, "Entrepreneur"));

        return data;
    }

    @Override
    public void onItemClick(String name) {
        filterObject = new JsonObject();

        filterObject.addProperty("interests", "[" + name + "]");

        lstMatchedProfiles.clear();
        myAdapter.notifyDataSetChanged();

        if (userData.has("bio")) {
            if (userData.optString("bio").isEmpty() && userData.optString("bio").trim().length() == 0) {
                lstMatchedProfiles.add(new MatchedProfiles(null, Utility.VIEW_TYPE_BIO,
                        null, null, null, null));
                myAdapter.notifyItemInserted(lstMatchedProfiles.size() - 1);
                isBioWarningCard = true;
            } else isBioWarningCard = false;
        } else {
            lstMatchedProfiles.add(new MatchedProfiles(null, Utility.VIEW_TYPE_BIO,
                    null, null, null, null));
            myAdapter.notifyItemInserted(lstMatchedProfiles.size() - 1);
            isBioWarningCard = true;
        }

        lstMatchedProfiles.add(new MatchedProfiles(null, Utility.VIEW_TYPE_TITLE,
                name, null, null, null));
        myAdapter.notifyItemInserted(lstMatchedProfiles.size() - 1);

        lastItemCount = 0;

        filterProfiles(filterObject, lastItemCount);
    }

    void loadMoreData(int index) {
        Log.e("ASDE", String.valueOf(index));
        if (isBioWarningCard)
            index -= 2;
        else index -= 1;
        filterProfiles(filterObject, index + 1);
    }

    private void filterProfiles(JsonObject filterObject, int index) {

        Utility.getInstance().networkCheck(getContext());

        filterObject.addProperty("user_id", getSPData("user_id"));
        filterObject.addProperty("user_token", getSPData("user_token"));
        filterObject.addProperty("offset", index);

        lstMatchedProfiles.add(null);
        myAdapter.notifyItemInserted(lstMatchedProfiles.size() - 1);

        AndroidNetworking.post(Utility.getInstance().BASE_URL + "filterProfiles")
                .addBodyParameter(filterObject).setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject res) {

                        lstMatchedProfiles.remove(lstMatchedProfiles.size() - 1);
                        myAdapter.notifyItemRemoved(lstMatchedProfiles.size());

                        if (res.optInt("status_code") == 200) {

                            try {
                                JSONArray profilesArray = res.getJSONArray("message");
                                if (profilesArray.length() == 0) {
                                    scrollListener.setReachedEnd();
                                }

                                if (profilesArray.length() == 0) {
                                    placeholder.setVisibility(View.VISIBLE);
                                } else {
                                    placeholder.setVisibility(View.GONE);
                                }

                                for (int i = 0; i < profilesArray.length(); i++) {
                                    JSONObject object = (JSONObject) profilesArray.get(i);
                                    lstMatchedProfiles.add(new com.einheit.matchezy.MatchedProfiles(
                                            object.optString("_id"),
                                            object.optString("name"),
                                            object.optString("profileImageURL"),
                                            object.optString("dob"),
                                            object.optJSONArray("interests"),
                                            object.toString()));
                                    Log.e(String.valueOf(i), String.valueOf(object.optInt("noOfMatchingInterests")));

                                }

                                scrollListener.setLoaded(lstMatchedProfiles.size());
                                myAdapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else
                            Toast.makeText(getContext(), res.optString("message"), Toast.LENGTH_SHORT).show();

                        if (res.optString("message").equals("Access denied")) {
                            clearSPData();

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        FirebaseInstanceId.getInstance().deleteInstanceId();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();

                            Intent i = new Intent(getContext(), OnboardingNew.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            getActivity().startActivity(i);
                            getActivity().finish();
                            //auto logout
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        error.printStackTrace();
                    }
                });
    }


    public class Data {
        public int imageId;
        public String txt;

        Data(int imageId, String text) {

            this.imageId = imageId;
            this.txt = text;
        }
    }


    private String getSPData(String key) {

        SharedPreferences mUserData = getActivity().getSharedPreferences("UserData", MODE_PRIVATE);
        String data = mUserData.getString(key, "");

        return data;
    }

    private void clearSPData() {
        SharedPreferences mUserData = getActivity().getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor mUserEditor = mUserData.edit();
        mUserEditor.clear();
        mUserEditor.apply();
    }


    public abstract class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {

        private int visibleThreshold = 2;
        private int currentPage = 0;
        private int previousTotalItemCount = 0;
        private boolean loading = true;
        private int startingPageIndex = 0;

        private boolean reachedEnd = false;

        private RecyclerView.LayoutManager layoutManager;

        public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        public RecyclerView.LayoutManager getLayoutManager() {
            return layoutManager;
        }

        public RecyclerViewScrollListener(RecyclerView.LayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        public RecyclerViewScrollListener(GridLayoutManager layoutManager, int visibleThreshold) {
            this.layoutManager = layoutManager;
            this.visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
        }

        public RecyclerViewScrollListener(
                StaggeredGridLayoutManager layoutManager,
                int visibleThreshold) {
            this.layoutManager = layoutManager;
            this.visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
        }

        public int getLastVisibleItem(int[] lastVisibleItemPositions) {
            int maxSize = 0;
            for (int i = 0; i < lastVisibleItemPositions.length; i++) {
                if (i == 0) {
                    maxSize = lastVisibleItemPositions[i];
                } else if (lastVisibleItemPositions[i] > maxSize) {
                    maxSize = lastVisibleItemPositions[i];
                }
            }
            return maxSize;
        }

        @Override
        public void onScrolled(RecyclerView view, int dx, int dy) {
            if (dy < 1) {
                return;
            }
            int lastVisibleItemPosition = 0;
            int totalItemCount = layoutManager.getItemCount();

            if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
            } else if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof LinearLayoutManager) {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }

            if (totalItemCount < previousTotalItemCount) {
                this.currentPage = this.startingPageIndex;
                this.previousTotalItemCount = totalItemCount;
                if (totalItemCount == 0) {
                    this.loading = true;
                }
            }
            if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount && !reachedEnd) {
                currentPage++;
                onLoadMore(currentPage, totalItemCount, view);
                loading = true;
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                boolean canScrollDownMore = recyclerView.canScrollVertically(1);
                if (!canScrollDownMore) {
                    onScrolled(recyclerView, 0, 1);
                }
            }
        }

        public void resetState() {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = 0;
            this.loading = true;
        }

        public void setLoaded(int totalItemCount) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        public void setReachedEnd() {
            reachedEnd = true;
        }

        public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView view);

    }

}
