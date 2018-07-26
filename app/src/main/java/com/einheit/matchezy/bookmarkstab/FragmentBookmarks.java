package com.einheit.matchezy.bookmarkstab;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.einheit.matchezy.MatchedProfiles;
import com.einheit.matchezy.R;
import com.einheit.matchezy.Utility;
import com.einheit.matchezy.hometab.Fragment_Home;
import com.einheit.matchezy.hometab.RecyclerViewAdapter;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class FragmentBookmarks extends Fragment {

    View myView;
    List<MatchedProfiles> lstMatchedProfiles ;

    RecyclerView myrv;
    BookmarksRecyclerViewAdapter myAdapter;

    RecyclerViewScrollListener scrollListener;
    private int lastItemCount = 0;

    public FragmentBookmarks() {
        // Required empty public constructor
    }

    public static FragmentBookmarks newInstance() {

        FragmentBookmarks fragmentBookmarks = new FragmentBookmarks();

        return fragmentBookmarks;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView =  inflater.inflate(R.layout.fragment__bookmarks, container, false);

        myrv = (RecyclerView) myView.findViewById(R.id.recyclerview_id);

        lstMatchedProfiles = new ArrayList<>();
        lastItemCount = 0;

        myAdapter = new BookmarksRecyclerViewAdapter(getActivity().getApplicationContext(),lstMatchedProfiles, getActivity());
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        myrv.setLayoutManager(layoutManager);
        myrv.setAdapter(myAdapter);

        getBookmarkedProfiles(lastItemCount);

        scrollListener = new RecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMoreData(myAdapter.getItemCount()-1);
            }
        };

        myrv.addOnScrollListener(scrollListener);

        return myView;
    }

    void loadMoreData(int index) {
        getBookmarkedProfiles(index + 1);
    }

    void getBookmarkedProfiles(int lastItemCount) {
        JsonObject o = new JsonObject();

        o.addProperty("user_id", getSPData("user_id"));
        o.addProperty("user_token", getSPData("user_token"));
        o.addProperty("offset", lastItemCount);

        lstMatchedProfiles.add(null);
        myAdapter.notifyItemInserted(lstMatchedProfiles.size() - 1);

        AndroidNetworking.post(Utility.getInstance().BASE_URL + "getBookmarkedProfiles")
                .addBodyParameter(o)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject res) {

                        lstMatchedProfiles.remove(lstMatchedProfiles.size() - 1);
                        myAdapter.notifyItemRemoved(lstMatchedProfiles.size());

                        if(res.optInt("status_code") == 200) {

                            try {
                                JSONArray profilesArray = res.getJSONArray("message");
                                if(profilesArray.length() == 0) {
                                    scrollListener.setReachedEnd();
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
                                }

                                scrollListener.setLoaded(lstMatchedProfiles.size());
                                myAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else
                            Toast.makeText(getContext(), res.optString("message"), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(ANError error) {
                        error.printStackTrace();
                    }
                });
    }

    private String getSPData(String key) {

        SharedPreferences mUserData = getActivity().getSharedPreferences("UserData", MODE_PRIVATE);
        String data = mUserData.getString(key, "");

        return data;
    }

    public abstract class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {

        private int visibleThreshold = 2;
        private int currentPage = 0;
        private int previousTotalItemCount = 0;
        private boolean loading = true;
        private int startingPageIndex = 0;

        private boolean reachedEnd = false;

        private RecyclerView.LayoutManager layoutManager;

        public void setLayoutManager(RecyclerView.LayoutManager layoutManager)
        {
            this.layoutManager = layoutManager;
        }

        public RecyclerView.LayoutManager getLayoutManager()
        {
            return layoutManager;
        }

        public RecyclerViewScrollListener(RecyclerView.LayoutManager layoutManager)
        {
            this.layoutManager = layoutManager;
        }

        public RecyclerViewScrollListener(GridLayoutManager layoutManager, int visibleThreshold)
        {
            this.layoutManager = layoutManager;
            this.visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
        }

        public RecyclerViewScrollListener(
                StaggeredGridLayoutManager layoutManager,
                int visibleThreshold)
        {
            this.layoutManager = layoutManager;
            this.visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
        }

        public int getLastVisibleItem(int[] lastVisibleItemPositions)
        {
            int maxSize = 0;
            for (int i = 0; i < lastVisibleItemPositions.length; i++)
            {
                if (i == 0)
                {
                    maxSize = lastVisibleItemPositions[i];
                }
                else if (lastVisibleItemPositions[i] > maxSize)
                {
                    maxSize = lastVisibleItemPositions[i];
                }
            }
            return maxSize;
        }

        @Override
        public void onScrolled(RecyclerView view, int dx, int dy)
        {
            if (dy < 1)
            {
                return;
            }
            int lastVisibleItemPosition = 0;
            int totalItemCount = layoutManager.getItemCount();

            if (layoutManager instanceof StaggeredGridLayoutManager)
            {
                int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
            }
            else if (layoutManager instanceof GridLayoutManager)
            {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            else if (layoutManager instanceof LinearLayoutManager)
            {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }

            if (totalItemCount < previousTotalItemCount)
            {
                this.currentPage = this.startingPageIndex;
                this.previousTotalItemCount = totalItemCount;
                if (totalItemCount == 0)
                {
                    this.loading = true;
                }
            }
            if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount && !reachedEnd)
            {
                currentPage++;
                onLoadMore(currentPage, totalItemCount, view);
                loading = true;
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState)
        {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE)
            {
                boolean canScrollDownMore = recyclerView.canScrollVertically(1);
                if (!canScrollDownMore)
                {
                    onScrolled(recyclerView, 0, 1);
                }
            }
        }

        public void resetState()
        {
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