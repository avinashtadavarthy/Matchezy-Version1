package com.einheit.matchezy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import de.hdodenhof.circleimageview.CircleImageView;
import com.bumptech.glide.Glide;
import com.einheit.matchezy.bookmarkstab.Fragment_favorites;
import com.einheit.matchezy.hometab.Fragment_Home;
import com.einheit.matchezy.messagestab.Fragment_messages;
import com.einheit.matchezy.notificationstab.Fragment_notifications;
import com.einheit.matchezy.profilescreen.ProfilePage;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeScreen extends AppCompatActivity {

    Bundle notifyBundle;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

            switch (item.getItemId()) {

                case R.id.navigation_home:
                    fragmentManager.beginTransaction().replace(R.id.home_container, Fragment_Home.newInstance()).commit();
                    break;
                case R.id.navigation_bookmarks:
                    Fragment_favorites fragmentFavorites = Fragment_favorites.newInstance();
                    fragmentFavorites.setArguments(notifyBundle);
                    fragmentManager.beginTransaction().replace(R.id.home_container, fragmentFavorites).commit();
                    break;
                case R.id.navigation_messages:
                    fragmentManager.beginTransaction().replace(R.id.home_container, new Fragment_messages()).commit();
                    break;
                case R.id.navigation_notifications:
                    fragmentManager.beginTransaction().replace(R.id.home_container, new Fragment_notifications()).commit();
                    break;
            }

            return true;

        }
    };

    CircleImageView profileimg;

    JSONObject userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //BottomNavigationViewHelper.removeShiftMode(navigation);
        navigation.setSelectedItemId(R.id.navigation_home);

        notifyBundle = new Bundle();
        notifyBundle.putString("notify", "bookmark");

        if(getIntent().hasExtra("queryUserId")) {
            Intent intent = new Intent(HomeScreen.this, ProfilePage.class);
            intent.putExtra("queryUserId",getIntent().getStringExtra("queryUserId"));
            intent.putExtra("fromStatusCode", Utility.FROM_SHARED_PROFILE);
            startActivity(intent);
            finish();
        }


        if(getIntent().hasExtra("notify")) {
            String selectedId = getIntent().getStringExtra("notify");
            if (selectedId.equals("chat"))
                navigation.setSelectedItemId(R.id.navigation_messages);
            else if (selectedId.equals("like")) {
                notifyBundle.putString("notify", "bookmark");
                navigation.setSelectedItemId(R.id.navigation_bookmarks);
            } else if (selectedId.equals("bookmark")) {
                navigation.setSelectedItemId(R.id.navigation_bookmarks);
            }
        }

        //Log.e("userData", getSPData("userData"));

        try {
            userData = new JSONObject(getSPData("userData"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        profileimg = (CircleImageView) findViewById(R.id.profileimg);

        //Log.e("ASD", userData.toString());

        Glide.with(getApplicationContext()).load(userData.optString("profileImageURL")).into(profileimg);

        profileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(HomeScreen.this, com.einheit.matchezy.ProfileOptions.class);
                startActivity(i);

            }
        });

    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAffinity(this);
    }

    private String getSPData(String key) {

        SharedPreferences mUserData = this.getSharedPreferences("UserData", MODE_PRIVATE);
        String data = mUserData.getString(key, "");

        return data;
    }

}
