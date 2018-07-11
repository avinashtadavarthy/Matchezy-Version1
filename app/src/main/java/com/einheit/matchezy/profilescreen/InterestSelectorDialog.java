package com.einheit.matchezy.profilescreen;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.einheit.matchezy.R;
import com.einheit.matchezy.hometab.Filter;
import com.webianks.library.scroll_choice.ScrollChoice;

import java.util.ArrayList;
import java.util.List;

public class InterestSelectorDialog extends Dialog {

    public Activity c;
    EditText enter_interests;
    ChipGroup selectedinterests, suggestedinterests;
    Button choose;

    String[] suggested = {
            "Pets", "Gaming", "Cooking","Foodie","Pet Lover","Movies","Cricket","Football","Tv","Cat Lover","Pets","Tech","Gaming","Wine tasting"
    };

    ArrayList<String> exisint;

    ArrayList<String> newinterests;

    OnMyDialogResult mDialogResult;

    public InterestSelectorDialog(Activity a, ArrayList<String> existinginterests) {
        super(a);
        this.c = a;
        this.exisint = existinginterests;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.interest_edit_dialog);


        enter_interests = findViewById(R.id.enter_interests);
        selectedinterests = findViewById(R.id.selectedinterests);
        suggestedinterests = findViewById(R.id.suggestedinterests);
        choose = findViewById(R.id.choose);

        populateExistingChips(exisint);
        populateSuggestedChips(suggested);

        enter_interests.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {

                    if (enter_interests.getText().toString().trim().equals("")) {
                        Toast.makeText(c, "Enter a valid interest!", Toast.LENGTH_SHORT).show();
                    } else {
                        populateSelectedChips(enter_interests.getText().toString().trim());
                        enter_interests.setText(null);
                    }

                    return true;
                }
                return false;
            }
        });


        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( mDialogResult != null ) {

                    if(newinterests == null) newinterests = new ArrayList<>();
                    mDialogResult.finish(newinterests);

                }

                InterestSelectorDialog.this.dismiss();

            }
        });

    }

    private void populateSuggestedChips(String[] arr) {

        for (int i=0;i<arr.length;i++) {
            Chip chip = new Chip(c);
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

    private void populateExistingChips(ArrayList<String> existing) {

        for (int i=0;i<existing.size();i++) {
            Chip chip = new Chip(c);
            chip.setChipText(existing.get(i));
            chip.setCloseIconEnabled(true);
            chip.setCloseIconResource(R.drawable.cross_small);
            chip.setCloseIconTintResource(R.color.white);
            //chip.setChipIconResource(R.drawable.add_small);
            chip.setChipBackgroundColorResource(R.color.appred);
            chip.setTextAppearanceResource(R.style.CommonChipTextStyle);
            //chip.setElevation(15);

            selectedinterests.addView(chip);

            newinterests = existing;

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

    private void populateSelectedChips(String text) {

        Chip chip = new Chip(c);
        chip.setChipText(text);
        chip.setCloseIconEnabled(true);
        chip.setCloseIconResource(R.drawable.cross_small);
        chip.setCloseIconTintResource(R.color.white);
        //chip.setChipIconResource(R.drawable.fab_add);
        chip.setChipBackgroundColorResource(R.color.appred);
        chip.setTextAppearanceResource(R.style.CommonChipTextStyle);
        //chip.setElevation(15);

        selectedinterests.addView(chip);
        if(newinterests != null)
            newinterests.add(text);
        else newinterests = new ArrayList<>();

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


    public void setDialogResult(OnMyDialogResult dialogResult) {
        mDialogResult = dialogResult;
    }

    public interface OnMyDialogResult{
        void finish(ArrayList<String> result);
    }

}

