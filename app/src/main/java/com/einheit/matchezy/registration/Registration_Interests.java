package com.einheit.matchezy.registration;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.einheit.matchezy.R;
import com.pchmn.materialchips.ChipView;
import com.scalified.fab.ActionButton;

import java.util.ArrayList;

public class Registration_Interests extends AppCompatActivity {


    EditText enter_interests;
    ChipGroup selectedinterests, suggestedinterests;

    String[] suggested = {
            "Pets", "Gaming", "Cooking","Foodie","Pet Lover","Movies","Cricket","Football","Tv","Cat Lover","Pets","Tech","Gaming","Wine tasting"
    };

    ArrayList<String> newinterests = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_interests);

        enter_interests = findViewById(R.id.enter_interests);
        selectedinterests = findViewById(R.id.selectedinterests);
        suggestedinterests = findViewById(R.id.suggestedinterests);

        populateSuggestedChips(suggested);

        enter_interests.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {

                    if (enter_interests.getText().toString().trim().equals("")) {
                        Toast.makeText(Registration_Interests.this, "Enter a valid interest!", Toast.LENGTH_SHORT).show();
                    } else {
                        populateSelectedChips(enter_interests.getText().toString().trim());
                        enter_interests.setText(null);
                    }

                    return true;
                }
                return false;
            }
        });


        final ActionButton actionButton = (ActionButton) findViewById(R.id.action_button_next3);
        actionButton.setType(ActionButton.Type.DEFAULT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            actionButton.setButtonColor(Color.parseColor("#EA5251"));
        }
        actionButton.setRippleEffectEnabled(true);
        actionButton.playShowAnimation();
        actionButton.setImageResource(R.drawable.ic_action_arrow);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newinterests.size() < 4) {
                    Toast.makeText(getApplicationContext(), "Select atleast four interests",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(Registration_Interests.this, Registration_Imageupload.class)
                            .putStringArrayListExtra("interestsArray", newinterests);
                    startActivity(intent);
                }
            }
        });
    }


    private void populateSuggestedChips(String[] arr) {

        for (int i=0;i<arr.length;i++) {
            Chip chip = new Chip(Registration_Interests.this);
            chip.setChipText(arr[i]);
            //chip.setCloseIconEnabled(true);
            //chip.setCloseIconResource(R.drawable.fab_add);
            chip.setChipIconResource(R.drawable.add_small);
            //chip.setChipBackgroundColorResource(R.color.appred);
            chip.setTextAppearanceResource(R.style.ChipTextStyle);
            //chip.setElevation(15);

            suggestedinterests.addView(chip);

            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    suggestedinterests.removeView(view);

                    if(view instanceof Chip) {
                        Chip temp = (Chip) view;
                        String s = temp.getChipText().toString();
                        populateSelectedChips(s);
                    }
                }
            });

        }

    }



    private void populateSelectedChips(String text) {

        Chip chip = new Chip(Registration_Interests.this);
        chip.setChipText(text);
        chip.setCloseIconEnabled(true);
        chip.setCloseIconResource(R.drawable.cross_small);
        chip.setCloseIconTintResource(R.color.white);
        //chip.setChipIconResource(R.drawable.fab_add);
        chip.setChipBackgroundColorResource(R.color.appred);
        chip.setTextAppearanceResource(R.style.CommonChipTextStyle);
        //chip.setElevation(15);

        selectedinterests.addView(chip);
        newinterests.add(text);

        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedinterests.removeView(view);

                if(view instanceof Chip) {
                    Chip temp = (Chip) view;
                    newinterests.remove(temp.getChipText().toString());
                }
            }
        });

    }


}
