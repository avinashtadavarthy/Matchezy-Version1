package com.example.yashwant.matchezy;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pchmn.materialchips.ChipView;
import com.scalified.fab.ActionButton;

public class Registration_Interests extends AppCompatActivity {


    EditText interest_edit;

    LinearLayout parent;
    LinearLayout parent2;
    LinearLayout layout1,layout2,layout3,layout4,layout5,layout6,layout7,layout8,layout9,layout10,layout11,layout12,layout13;
    String[] SUGGESTIONS ={"Cooking","Foodie","Pet Lover","Movies","Cricket","Football","Tv","Cat Lover","Pets","Tech","Gaming","Wine tasting"};
    LinearLayout slayout1,slayout2,slayout3,slayout4,slayout5,slayout6,slayout7,slayout8,slayout9,slayout10,slayout11,slayout12,slayout13;
    GridLayout gridLayout;

    int chipNo =0;

    String [] chips = new String[50];
    int a=-1;
    int b=0;
    int i=-1;
    int j =0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_interests);

        layout1= new LinearLayout(this);
        layout1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout1.setOrientation(LinearLayout.HORIZONTAL);
        layout2 = new LinearLayout(this);
        layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout2.setOrientation(LinearLayout.HORIZONTAL);
        layout3 = new LinearLayout(this);
        layout3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout3.setOrientation(LinearLayout.HORIZONTAL);
        layout4 = new LinearLayout(this);
        layout4.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout4.setOrientation(LinearLayout.HORIZONTAL);
        layout5 = new LinearLayout(this);
        layout5.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout5.setOrientation(LinearLayout.HORIZONTAL);
        layout6 = new LinearLayout(this);
        layout6.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout6.setOrientation(LinearLayout.HORIZONTAL);
        layout7 = new LinearLayout(this);
        layout7.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout7.setOrientation(LinearLayout.HORIZONTAL);
        layout8 = new LinearLayout(this);
        layout8.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout8.setOrientation(LinearLayout.HORIZONTAL);
        layout9 = new LinearLayout(this);
        layout9.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout9.setOrientation(LinearLayout.HORIZONTAL);
        layout10 = new LinearLayout(this);
        layout10.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout10.setOrientation(LinearLayout.HORIZONTAL);
        layout11 = new LinearLayout(this);
        layout11.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout11.setOrientation(LinearLayout.HORIZONTAL);
        layout12 = new LinearLayout(this);
        layout12.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout12.setOrientation(LinearLayout.HORIZONTAL);
        layout13 = new LinearLayout(this);
        layout13.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout13.setOrientation(LinearLayout.HORIZONTAL);




        slayout1= new LinearLayout(this);
        slayout1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        slayout1.setOrientation(LinearLayout.HORIZONTAL);
        slayout2 = new LinearLayout(this);
        slayout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        slayout2.setOrientation(LinearLayout.HORIZONTAL);
        slayout3 = new LinearLayout(this);
        slayout3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        slayout3.setOrientation(LinearLayout.HORIZONTAL);
        slayout4 = new LinearLayout(this);
        slayout4.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        slayout4.setOrientation(LinearLayout.HORIZONTAL);
        slayout5 = new LinearLayout(this);
        slayout5.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        slayout5.setOrientation(LinearLayout.HORIZONTAL);
        slayout6 = new LinearLayout(this);
        slayout6.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        slayout6.setOrientation(LinearLayout.HORIZONTAL);
        slayout7 = new LinearLayout(this);
        slayout7.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        slayout7.setOrientation(LinearLayout.HORIZONTAL);
        slayout8 = new LinearLayout(this);
        slayout8.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        slayout8.setOrientation(LinearLayout.HORIZONTAL);
        slayout9 = new LinearLayout(this);
        slayout9.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        slayout9.setOrientation(LinearLayout.HORIZONTAL);
        slayout10 = new LinearLayout(this);
        slayout10.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        slayout10.setOrientation(LinearLayout.HORIZONTAL);
        slayout11 = new LinearLayout(this);
        slayout11.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        slayout11.setOrientation(LinearLayout.HORIZONTAL);
        slayout12 = new LinearLayout(this);
        slayout12.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        slayout12.setOrientation(LinearLayout.HORIZONTAL);
        slayout13 = new LinearLayout(this);
        slayout13.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        slayout13.setOrientation(LinearLayout.HORIZONTAL);



        parent = (LinearLayout) findViewById(R.id.interest_linear);
        parent2 =(LinearLayout)findViewById(R.id.interest_linear2);

       /* String test = "layout";*/


        interest_edit =(EditText)findViewById(R.id.interestbox);
      /*  gridLayout =(GridLayout)findViewById(R.id.interest_linear);*/

        parent.addView(layout1);
       parent2.addView(slayout1);

      /*   parent2.addView(slayout2);
        parent2.addView(slayout3);
        parent2.addView(slayout4);
        parent2.addView(slayout5);
        parent2.addView(slayout6);
*/

        int size = SUGGESTIONS.length;
        for (int c=0;c<size;c++) {


            LinearLayout[] test1 = {slayout1,slayout2,slayout3,slayout4,slayout5,slayout6,slayout7,slayout8,slayout9,slayout10,slayout11,slayout12,slayout13};
            a++;

            LinearLayout test3;
            test3=test1[b];

            if(a%4==0)
            {
                b++;
                test3=test1[b];
                parent2.addView(test3);

            }


            make_bubblesuggest(SUGGESTIONS[c],test3);
        }

        interest_edit.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press

                  /*  Toast.makeText(Registration_Interests.this, interest_edit.getText(), Toast.LENGTH_SHORT).show();*/

                   runBubble(interest_edit.getText().toString().trim());
                    interest_edit.setText(null);
                }
                return false;
            }


        });

      /*  ChipsInput chipsInput = (ChipsInput) findViewById(R.id.chips_input);*/
/*

        mNachoTextViewWithIcons =(NachoTextView)findViewById(R.id.nacho_text_view);*/

    /*    setupChipTextView(mNachoTextViewWithIcons);*/

        final ActionButton actionButton = (ActionButton) findViewById(R.id.action_button_next3);
        // actionButton.hide();
        actionButton.setType(ActionButton.Type.BIG);
        //actionButton.setSize(65.0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            actionButton.setButtonColor(Color.parseColor("#EA5251"));
        }
        actionButton.setRippleEffectEnabled(true);
        actionButton.playShowAnimation();
        actionButton.setImageResource(R.drawable.ic_action_arrow);

        /*mNachoTextView.setText("yo \n");*/

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Registration_Imageupload.class);
                startActivity(intent);
            }
        });


   /*     chipsInput.addChipsListener(new ChipsInput.ChipsListener() {
            @Override
            public void onChipAdded(ChipInterface chip, int newSize) {
                // chip added
                // newSize is the size of the updated selected chip list
            }

            @Override
            public void onChipRemoved(ChipInterface chip, int newSize) {
                // chip removed
                // newSize is the size of the updated selected chip list
            }

            @Override
            public void onTextChanged(CharSequence text) {
                // text changed
            }
        });
*/
/*
        mNachoTextViewWithIcons.setChipTokenizer(new SpanChipTokenizer<>(this, new ChipSpanChipCreator() {
            @Override
            public ChipSpan createChip(@NonNull Context context, @NonNull CharSequence text, Object data) {
                return new ChipSpan(context, text, ContextCompat.getDrawable(Registration_Interests.this, R.mipmap.remove), data);
            }

            @Override
            public void configureChip(@NonNull ChipSpan chip, @NonNull ChipConfiguration chipConfiguration) {
                super.configureChip(chip, chipConfiguration);
            }
        }, ChipSpan.class));

    }


    private void setupChipTextView(NachoTextView nachoTextView) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, SUGGESTIONS);
        nachoTextView.setAdapter(adapter);
        nachoTextView.setIllegalCharacters('\"');
        nachoTextView.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);
      *//*  nachoTextView.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);*//*
        nachoTextView.addChipTerminator(';', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_CURRENT_TOKEN);
        nachoTextView.setNachoValidator(new ChipifyingNachoValidator());
       *//* nachoTextView.enableEditChipOnTouch(true, true);*//*
        nachoTextView.setOnChipClickListener(new NachoTextView.OnChipClickListener() {
            @Override
            public void onChipClick(Chip chip, MotionEvent motionEvent) {
                Toast.makeText(Registration_Interests.this, "click" , Toast.LENGTH_SHORT).show();
                *//*Log.d(TAG, "onChipClick: " + chip.getText());*//*
            }
        });*/

        /*nachoTextView.setOnChipRemoveListener(new NachoTextView.OnChipRemoveListener() {
            @Override
            public void onChipRemove(Chip chip) {
                Log.d(TAG, "onChipRemoved: " + chip.getText());
            }
        });*/
    }

    public void make_bubble(String in, final LinearLayout linearLayout)

    {

        //set the properties for button

        Uri uri = null;
        final ChipView chipView1 = new ChipView(this);
        chipView1.setLabel(in);
        /* chipView1.setOnDeleteClicked();*/
        chipView1.setDeletable(true);
        chipView1.setPadding(2,2,2,2);


        linearLayout.addView(chipView1);



        chipView1.setOnChipClicked(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Toast.makeText(Registration_Interests.this, chipView1.getLabel().toString(), Toast.LENGTH_SHORT).show();

            /*Toast.makeText(this, chipView1.getLabel().toString(), Toast.LENGTH_SHORT).show();*/
            linearLayout.indexOfChild(chipView1);
            Toast.makeText(Registration_Interests.this, String.valueOf(linearLayout.indexOfChild(chipView1)), Toast.LENGTH_SHORT).show();
            /*linearLayout.removeViewAt();*/



        }


    });


        chipView1.setOnDeleteClicked(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });
    /*    ChipView chipView2 = new ChipView(this);
        chipView2.setLabel("Test 1");

      *//*
        chipView2.setChipBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
        chipView2.setLabelColor(ContextCompat.getColor(this, R.color.colorPrimary));*//*
        chipView2.setDeletable(true);*/
        /* chipView2.setAvatarIcon(uri);*/
        /*   chipView2.setDeleteIconColor(ContextCompat.getColor(this, R.color.colorPrimary));*/

        /*layout.addView(chipView1);*/



   /*     int total = 10;
        int column = 3;
        int row = total / column;
        gridLayout.setColumnCount(column);
        gridLayout.setRowCount(row + 1);
        for (int i = 0, c = 0, r = 0; i < total; i++, c++) {
            if (c == column) {
                c = 0;
                r++;
            }

            GridLayout.Spec rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 1);
            GridLayout.Spec colspan = GridLayout.spec(GridLayout.UNDEFINED, 1);
            if (r == 0 && c == 0) {
                Log.e("", "spec");
                colspan = GridLayout.spec(GridLayout.UNDEFINED, 2);
                rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 2);
            }
            GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(
                    rowSpan, colspan);
            gridLayout.addView(chipView1, gridParam);


        }*/
     /*   layout.addView(chipView2);*/


    }


    public boolean runBubble(String name)
    {
        LinearLayout[] test = {layout1,layout2,layout3,layout4,layout5,layout6,layout7,layout8,layout9,layout10,layout11,layout12,layout13};
        i++;

        LinearLayout test2;
        test2=test[j];

        if(i%3==0)
        {
            j++;
            test2=test[j];
            parent.addView(test2);

        }
        /*parent.addView(test2);*/


                   /* for (int j=0 ;j<3)
                    if(i==3)
                         test = layout2;
                    if(i==6)
                        test = layout3;
                    if(i==9)
                        test = layout4;
                    if(i==12)
                        test = layout5;
                    if(i==15)
                        test = layout6;
                    if(i==18)
                        test = layout7;*/


        chips[chipNo]=name;

        make_bubble(name,test2);

        chipNo++;
        return true;
    }
    public void make_bubblesuggest(String in, final LinearLayout linearLayout)

    {

        //set the properties for button

            Uri uri = null;
            final ChipView chipView2 = new ChipView(this);
            chipView2.setLabel(in);
            /* chipView1.setOnDeleteClicked();*/
            chipView2.setPadding(2, 2, 2, 2);
            linearLayout.addView(chipView2);


            chipView2.setOnChipClicked(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    linearLayout.removeViewAt(linearLayout.indexOfChild(chipView2));
                    runBubble(chipView2.getLabel().toString());

                    Toast.makeText(Registration_Interests.this, chipView2.getLabel().toString(), Toast.LENGTH_SHORT).show();

                }
            });

    }


}
