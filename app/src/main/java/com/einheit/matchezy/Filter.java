package com.einheit.matchezy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.appyvet.materialrangebar.RangeBar;
import com.einheit.matchezy.registration.Registration2;
import com.einheit.matchezy.registration.Registration3;
import com.einheit.matchezy.registration.Registration_Interests;
import com.scalified.fab.ActionButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Filter extends AppCompatActivity {

    ImageView downarrow;

    String[] suggested = {
            "Pets", "Gaming", "Cooking","Foodie","Pet Lover","Movies","Cricket","Football","Tv","Cat Lover","Pets","Tech","Gaming","Wine tasting"
    };

    EditText enter_interests;
    TextView clearinterests;

    ChipGroup selectedinterests, suggestedinterests;

    RangeBar rangebar_age;

    TextView height_start,height_end,age_start,age_end;

    EditText filter_relationship, filter_education, filter_annual, filter_religion, filter_tattoos , filter_piercings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        filter_relationship = findViewById(R.id.filter_relationship);
        filter_education = findViewById(R.id.filter_education);
        filter_annual = findViewById(R.id.filter_annual);
        filter_religion = findViewById(R.id.filter_religion);
        filter_tattoos = findViewById(R.id.filter_tattoos);
        filter_piercings = findViewById(R.id.filter_piercings);

        downarrow = findViewById(R.id.downarrow);

        downarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final ActionButton actionButton = (ActionButton) findViewById(R.id.action_filters_confirm);
        actionButton.setType(ActionButton.Type.DEFAULT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            actionButton.setButtonColor(Color.parseColor("#EA5251"));
        }
        actionButton.setRippleEffectEnabled(true);
        actionButton.playShowAnimation();
        actionButton.setImageResource(R.drawable.done);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        selectedinterests = findViewById(R.id.selectedinterests);
        suggestedinterests = findViewById(R.id.suggestedinterests);
        clearinterests = findViewById(R.id.clearinterests);

        clearinterests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(Filter.this);
                builder1.setMessage("Clear all selected interests?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                selectedinterests.removeAllViews();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });

        populateSuggestedChips(suggested);

        enter_interests = findViewById(R.id.enter_interests);
        enter_interests.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {

                    if (enter_interests.getText().toString().trim().equals("")) {
                        Toast.makeText(Filter.this, "Enter a valid interest!", Toast.LENGTH_SHORT).show();
                    } else {
                        populateSelectedChips(enter_interests.getText().toString().trim());
                        enter_interests.setText(null);
                    }

                    return true;
                }
                return false;
            }
        });

        rangebar_age = (RangeBar) findViewById(R.id.rangebar_age);

        height_start=(TextView)findViewById(R.id.height_start);
        height_end=(TextView)findViewById(R.id.height_end);

        age_start=(TextView)findViewById(R.id.age_start);
        age_end=(TextView)findViewById(R.id.age_end);

        height_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CustomDialogClass cdd = new CustomDialogClass(Filter.this, "Select Minimum Height", "new");
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();
                cdd.setDialogResult(new CustomDialogClass.OnMyDialogResult(){
                    public void finish(String result) {
                        height_start.setText(result);
                        height_end.setText(result);
                    }
                });

            }
        });

        height_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CustomDialogClass cdd = new CustomDialogClass(Filter.this, "Select Maximum Height", height_start.getText().toString());
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();
                cdd.setDialogResult(new CustomDialogClass.OnMyDialogResult(){
                    public void finish(String result) {
                        height_end.setText(result);
                    }
                });
            }
        });


        rangebar_age.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {

                age_start.setText(String.valueOf(leftPinIndex+20));
                age_end.setText(String.valueOf(rightPinIndex+20));
            }

        });


        filter_relationship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relationship();
            }
        });

        filter_education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                education();
            }
        });

        filter_religion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                religion();
            }
        });

        filter_tattoos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tattoos();
            }
        });

        filter_piercings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                piercing();
            }
        });


    }

    private void religion() {
        final CharSequence[] items = {"Hindu", "Muslim", "Sikh", "Christian", "Jain", "Parsi", "Buddhist", "Inter-Religion"};

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Filter.this);
        alertDialogBuilder.setTitle("Choose Religion");
        int position;
        switch (filter_religion.getText().toString()) {
            case "Hindu":
                position = 0;
                break;
            case "Muslim":
                position = 1;
                break;
            case "Sikh":
                position = 2;
                break;
            case "Christian":
                position = 3;
                break;
            case "Jain":
                position = 4;
                break;
            case "Parsi":
                position = 5;
                break;
            case "Buddhist":
                position = 6;
                break;
            case "Inter-Religion":
                position = 7;
                break;
            default:
                position = -1;
                break;
        }
        alertDialogBuilder
                .setSingleChoiceItems(items, position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListView lw = ((AlertDialog) dialog).getListView();
                        Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                        String selectedgend = checkedItem.toString();
                        filter_religion.setText(selectedgend);
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void tattoos() {
        final CharSequence[] items = { "Yes","No","Planning to get"};

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Filter.this);
        alertDialogBuilder.setTitle("Tattoos?");
        int position;
        switch (filter_tattoos.getText().toString()) {
            case "Yes":
                position = 0;
                break;
            case "No":
                position = 1;
                break;
            case "Planning to get":
                position = 2;
                break;
            default:
                position = -1;
                break;
        }
        alertDialogBuilder
                .setSingleChoiceItems(items, position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListView lw = ((AlertDialog) dialog).getListView();
                        Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                        String selectedgend = checkedItem.toString();
                        filter_tattoos.setText(selectedgend);
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void piercing() {
        final CharSequence[] items = { "Yes","No","Planning to get"};

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Filter.this);
        alertDialogBuilder.setTitle("Piercing?");
        int position;
        switch (filter_piercings.getText().toString()) {
            case "Yes":
                position = 0;
                break;
            case "No":
                position = 1;
                break;
            case "Planning to get":
                position = 2;
                break;
            default:
                position = -1;
                break;
        }
        alertDialogBuilder
                .setSingleChoiceItems(items, position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListView lw = ((AlertDialog) dialog).getListView();
                        Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                        String selectedgend = checkedItem.toString();
                        filter_piercings.setText(selectedgend);
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void education() {

        ArrayList<MultiSelectModel> edu = new ArrayList<>();
        User.getInstance().populateModel(edu, User.getInstance().education_items);

        MultiSelectDialog multiSelectDialog = new MultiSelectDialog()
                .title("Select Education") //setting title for dialog
                .titleSize(25)
                .positiveText("Done")
                .negativeText("Cancel")
                .setMinSelectionLimit(1) //you can set minimum checkbox selection limit (Optional)
                .multiSelectList(edu) // the multi select model list with ids and name
                .onSubmit(new MultiSelectDialog.SubmitCallbackListener() {
                    @Override
                    public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {
                        //will return list of selected IDS
                        /*for (int i = 0; i < selectedIds.size(); i++) {
                            Toast.makeText(Registration4.this, "Selected Ids : " + selectedIds.get(i) + "\n" +
                                    "Selected Names : " + selectedNames.get(i) + "\n" +
                                    "DataString : " + dataString, Toast.LENGTH_SHORT).show();
                        }*/

                        filter_education.setText(dataString);
                    }

                    @Override
                    public void onCancel() {
                        Log.d("multidialog","Dialog cancelled");
                    }

                });

        multiSelectDialog.show(getSupportFragmentManager(), "multiSelectDialog");

    }


    public void relationship() {
        final CharSequence[] items = { "Single", "Single with Children", "Divorced", "Divorced with Children", "Widowed", "Widowed with Children" };

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Filter.this);
        alertDialogBuilder.setTitle("Choose Gender");
        int position;
        switch (filter_relationship.getText().toString()) {
            case "Single": position = 0; break;
            case "Single with Children": position = 1; break;
            case "Divorced": position = 2; break;
            case "Divorced with Children": position = 3; break;
            case "Widowed": position = 4; break;
            case "Widowed with Children": position = 5; break;
            default: position = -1; break;
        }
        alertDialogBuilder.setSingleChoiceItems(items, position, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListView lw = ((AlertDialog) dialog).getListView();
                Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                String selectedgend = checkedItem.toString();
                filter_relationship.setText(selectedgend);
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void populateSuggestedChips(String[] arr) {

        for (int i=0;i<arr.length;i++) {
            Chip chip = new Chip(Filter.this);
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

        Chip chip = new Chip(Filter.this);
        chip.setChipText(text);
        chip.setCloseIconEnabled(true);
        chip.setCloseIconResource(R.drawable.cross_small);
        chip.setCloseIconTintResource(R.color.white);
        //chip.setChipIconResource(R.drawable.fab_add);
        chip.setChipBackgroundColorResource(R.color.appred);
        chip.setTextAppearanceResource(R.style.CommonChipTextStyle);
        //chip.setElevation(15);

        selectedinterests.addView(chip);

        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedinterests.removeView(view);
            }
        });

    }

}
