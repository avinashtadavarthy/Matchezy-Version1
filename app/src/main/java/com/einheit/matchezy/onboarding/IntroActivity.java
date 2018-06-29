package com.einheit.matchezy.onboarding;

import android.content.Intent;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.einheit.matchezy.Login;
import com.einheit.matchezy.R;
import com.scalified.fab.ActionButton;

public class IntroActivity extends AppCompatActivity {
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.intro_layout);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        final ActionButton actionButton = (ActionButton) findViewById(R.id.action_button_intro);
        actionButton.hide();

        // Set an Adapter on the ViewPager
        mViewPager.setAdapter(new IntroAdapter(getSupportFragmentManager()));

        // Set a PageTransformer
        mViewPager.setPageTransformer(false, new IntroPageTransformer());



        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {

                if(position==2)
                {

                    final ActionButton actionButton = (ActionButton) findViewById(R.id.action_button_intro);
                    actionButton.hide();
                    actionButton.setType(ActionButton.Type.BIG);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        actionButton.setButtonColor(getColor(R.color.red));
                    }
                    actionButton.setRippleEffectEnabled(true);
                    actionButton.playShowAnimation();
                    actionButton.setImageResource(R.drawable.ic_action_arrow);

                  //  FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.nextt);

                    actionButton.show();
                    actionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            Log.e("check", "accessed");
                            Intent intent = new Intent(IntroActivity.this, Login.class);
                            startActivity(intent);
                        }
                    });
                }
                // Check if this is the page you want.
            }
        });





    }

}