package com.example.yashwant.matchezy;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.scalified.fab.ActionButton;

public class ProfilePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);


        final ActionButton actionButton = (ActionButton) findViewById(R.id.action_button_like);
        // actionButton.hide();
        actionButton.setType(ActionButton.Type.DEFAULT);
        //actionButton.setSize(65.0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            actionButton.setButtonColor(Color.parseColor("#EA5251"));
        }
        actionButton.setRippleEffectEnabled(true);
        actionButton.playShowAnimation();
        actionButton.setImageResource(R.drawable.ic_action_like);

        final ActionButton actionButton2 = (ActionButton) findViewById(R.id.action_button_dislike);
        // actionButton.hide();
        actionButton2.setType(ActionButton.Type.DEFAULT);
        //actionButton.setSize(65.0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            actionButton2.setButtonColor(Color.parseColor("#FF4A4A4A"));
        }
        actionButton2.setRippleEffectEnabled(true);
        actionButton2.playShowAnimation();
        actionButton2.setImageResource(R.drawable.ic_action_dislike);

    }
}
