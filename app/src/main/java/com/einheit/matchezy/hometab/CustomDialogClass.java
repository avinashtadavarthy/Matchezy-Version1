package com.einheit.matchezy.hometab;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.einheit.matchezy.R;
import com.webianks.library.scroll_choice.ScrollChoice;

import java.util.ArrayList;
import java.util.List;

public class CustomDialogClass extends Dialog {

    public Activity c;
    public TextView dptxt;
    public String displaytext;
    public Button choose;

    String chosenheight = "";
    String minht;

    List<String> newdata;

    OnMyDialogResult mDialogResult;

    public CustomDialogClass(Activity a, String text, String minht) {
        super(a);
        this.c = a;
        this.displaytext = text;
        this.minht = minht;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.height_selector);

        dptxt = findViewById(R.id.displaytext);
        dptxt.setText(displaytext);

        choose = findViewById(R.id.choose);

        ScrollChoice scrollChoice = (ScrollChoice) findViewById(R.id.scroll_choice);
        List<String> data = new ArrayList<>();
        data.add("4'");
        data.add("4'1\"");
        data.add("4'2\"");
        data.add("4'3\"");
        data.add("4'4\"");
        data.add("4'5\"");
        data.add("4'6\"");
        data.add("4'7\"");
        data.add("4'8\"");
        data.add("4'9\"");
        data.add("4'10\"");
        data.add("4'11\"");
        data.add("5'");
        data.add("5'1\"");
        data.add("5'2\"");
        data.add("5'3\"");
        data.add("5'4\"");
        data.add("5'5\"");
        data.add("5'6\"");
        data.add("5'7\"");
        data.add("5'8\"");
        data.add("5'9\"");
        data.add("5'10\"");
        data.add("5'11\"");
        data.add("6'");
        data.add("6'1\"");
        data.add("6'2\"");
        data.add("6'3\"");
        data.add("6'4\"");
        data.add("6'5\"");
        data.add("6'6\"");
        data.add("6'7\"");
        data.add("6'8\"");
        data.add("6'9\"");
        data.add("6'10\"");
        data.add("6'11\"");
        data.add("7'");

        if(minht.equals("new")) {

            scrollChoice.addItems(data, 14);

        } else {

            int index = data.indexOf(minht);

            newdata = data.subList(index, data.size());

            scrollChoice.addItems(newdata, 0);

        }


        scrollChoice.setOnItemSelectedListener(new ScrollChoice.OnItemSelectedListener() {
            @Override
            public void onItemSelected(ScrollChoice scrollChoice, int position, String name) {

                chosenheight = name;

            }
        });

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( mDialogResult != null ) {

                    if (!chosenheight.equals("")) mDialogResult.finish(chosenheight);
                    else {
                        if (minht.equals("new")) mDialogResult.finish("5'2\"");
                        else mDialogResult.finish(newdata.get(0));
                    }

                }

                CustomDialogClass.this.dismiss();

            }
        });

    }

    public void setDialogResult(OnMyDialogResult dialogResult) {
        mDialogResult = dialogResult;
    }

    public interface OnMyDialogResult{
        void finish(String result);
    }

}

