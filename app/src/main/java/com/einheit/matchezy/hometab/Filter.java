package com.einheit.matchezy.hometab;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.appyvet.materialrangebar.IRangeBarFormatter;
import com.appyvet.materialrangebar.RangeBar;
import com.einheit.matchezy.HomeScreen;
import com.einheit.matchezy.NumberTextWatcher;
import com.einheit.matchezy.R;
import com.einheit.matchezy.RawData;
import com.einheit.matchezy.Utility;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scalified.fab.ActionButton;

import java.util.ArrayList;
import java.util.List;

public class Filter extends AppCompatActivity {

    ImageView downarrow;

    String[] suggested = {
            "Pets", "Gaming", "Cooking","Foodie","Pet Lover","Movies","Cricket","Football","Tv","Cat Lover","Pets","Tech","Gaming","Wine tasting"
    };

    private final String DOESNTMATTER = "Doesn't matter";

    EditText enter_interests;
    TextView clearinterests;

    ChipGroup selectedinterests, suggestedinterests;

    RangeBar rangebar_age, rangebar_height;

    TextView height_start,height_end,age_start,age_end;

    EditText filter_lookingfor, filter_cities, filter_languages, filter_relationship, filter_education, filter_college, filter_work, filter_min_annual, filter_max_annual, filter_religion, filter_tattoos , filter_piercings;

    List<String> interestsArary = new ArrayList<>();

    ActionButton actionButton;

    JsonObject filterObject = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        filter_lookingfor = findViewById(R.id.filter_lookingfor);
        filter_cities = findViewById(R.id.filter_cities);
        filter_languages = findViewById(R.id.filter_languages);
        filter_relationship = findViewById(R.id.filter_relationship);
        filter_education = findViewById(R.id.filter_education);
        filter_college = findViewById(R.id.filter_college);
        filter_work = findViewById(R.id.filter_work);
        filter_min_annual = findViewById(R.id.filter_min_annual);
        filter_max_annual = findViewById(R.id.filter_max_annual);
        filter_religion = findViewById(R.id.filter_religion);
        filter_tattoos = findViewById(R.id.filter_tattoos);
        filter_piercings = findViewById(R.id.filter_piercings);

        selectedinterests = findViewById(R.id.selectedinterests);
        suggestedinterests = findViewById(R.id.suggestedinterests);
        clearinterests = findViewById(R.id.clearinterests);

        selectedinterests = findViewById(R.id.selectedinterests);
        suggestedinterests = findViewById(R.id.suggestedinterests);
        clearinterests = findViewById(R.id.clearinterests);

        enter_interests = findViewById(R.id.enter_interests);

        downarrow = findViewById(R.id.downarrow);

        rangebar_age = findViewById(R.id.rangebar_age);
        rangebar_height = findViewById(R.id.rangebar_height);

        height_start= findViewById(R.id.height_start);
        height_end= findViewById(R.id.height_end);

        age_start= findViewById(R.id.age_start);
        age_end= findViewById(R.id.age_end);

        actionButton = findViewById(R.id.action_filters_confirm);

        downarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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

                saveDataAsObject();

                Intent intent = new Intent(Filter.this, HomeScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        filterObject = new JsonObject();

        if(getSPData("filterObject").length() > 0 && !getSPData("filterObject").isEmpty()) {
            JsonParser parser = new JsonParser();
            filterObject = parser.parse(getSPData("filterObject")).getAsJsonObject();
        }

        if(filterObject.has("interests")) {

            String[] interestsStringArray = filterObject.get("interests").getAsString()
                    .replace("[", "")
                    .replace("]", "").split(", ");

            for (String anInterestsStringArray : interestsStringArray) {
                populateSelectedChips(anInterestsStringArray);
            }

        }

        if(filterObject.has("feetMin") && filterObject.has("inchesMin") &&
                filterObject.has("feetMax") && filterObject.has("inchesMax")) {
            height_start.setText(filterObject.get("feetMin").getAsString() + "\'" + filterObject.get("inchesMin").getAsString() + "\"");
            height_end.setText(filterObject.get("feetMax").getAsString() + "\'" + filterObject.get("inchesMax").getAsString() + "\"");
            rangebar_height.setRangePinsByValue((float) Math.floor((filterObject.get("feetMin").getAsInt() * 12
                    + filterObject.get("inchesMin").getAsInt()) * 2.54), (float) Math.floor((filterObject.get("feetMax").getAsInt() * 12
                    + filterObject.get("inchesMax").getAsInt()) * 2.54));
        } else {
            height_start.setText("4'0\"");
            height_end.setText("7'0\"");
        }

        if(filterObject.has("ageMin") && filterObject.has("ageMax")) {
            age_start.setText(String.valueOf(filterObject.get("ageMin").getAsInt()));
            age_end.setText(String.valueOf(filterObject.get("ageMax").getAsInt()));
            rangebar_age.setRangePinsByValue(filterObject.get("ageMin").getAsInt(), filterObject.get("ageMax").getAsInt());
        }

        if(filterObject.has("maritalStatus")) {
            filter_relationship.setText(String.valueOf(filterObject.get("maritalStatus").getAsString()));
        }

        if(filterObject.has("lookingFor")) {
            filter_lookingfor.setText(String.valueOf(filterObject.get("lookingFor").getAsString()));
        }

        if(filterObject.has("tattoo")) {
            filter_tattoos.setText(filterObject.get("tattoo").getAsString());
        }

        if(filterObject.has("piercings")) {
            filter_piercings.setText(filterObject.get("piercings").getAsString());
        }

        if(filterObject.has("religions")) {
            filter_religion.setText(filterObject.get("religions").getAsString());
        }

        if(filterObject.has("education")) {
            filter_education.setText(filterObject.get("education").getAsString());
        }

        if(filterObject.has("colleges")) {
            filter_college.setText(filterObject.get("colleges").getAsString());
        }

        if(filterObject.has("work")) {
            filter_work.setText(filterObject.get("work").getAsString());
        }

        if(filterObject.has("preferredCities")) {
            filter_cities.setText(filterObject.get("preferredCities").getAsString());
        }

        if(filterObject.has("langs")) {
            filter_languages.setText(filterObject.get("langs").getAsString());
        }

        if(filterObject.has("annualIncomeMin")) {
            filter_min_annual.setText(filterObject.get("annualIncomeMin").getAsString());
        }

        if(filterObject.has("annualIncomeMax")) {
            filter_max_annual.setText(filterObject.get("annualIncomeMax").getAsString());
        }

        clearinterests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(Filter.this);
                builder1.setMessage("Clear all selected filters?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                selectedinterests.removeAllViews();
                                interestsArary.clear();

                                rangebar_age.setRangePinsByValue(21,50);
                                rangebar_height.setRangePinsByValue(120,213);

                                height_start.setText("4'0\"");
                                height_end.setText("7'0\"");

                                filter_cities.setText("");
                                filter_languages.setText("");
                                filter_relationship.setText("");
                                filter_education.setText("");
                                filter_min_annual.setText("");
                                filter_max_annual.setText("");
                                filter_religion.setText("");
                                filter_tattoos.setText("");
                                filter_piercings.setText("");
                                filter_lookingfor.setText("");
                                filter_college.setText("");
                                filter_work.setText("");

                                saveDataAsObject();
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



        filter_max_annual.addTextChangedListener(new NumberTextWatcher(filter_max_annual));
        filter_min_annual.addTextChangedListener(new NumberTextWatcher(filter_min_annual));


        populateSuggestedChips(suggested);

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


        rangebar_age.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {

                age_start.setText(String.valueOf(leftPinIndex+21));
                age_end.setText(String.valueOf(rightPinIndex+21));
            }

        });

        rangebar_height.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {

                height_start.setText(centimeterToFeet(leftPinValue));
                height_end.setText(centimeterToFeet(rightPinValue));

            }

        });

        rangebar_height.setFormatter(new IRangeBarFormatter() {
            @Override
            public String format(String value) {
                return centimeterToFeet(value);
            }
        });

        filter_lookingfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lookingfor();
            }
        });

        filter_cities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cities();
            }
        });

        filter_languages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                languages();
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

        filter_college.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                college();
            }
        });

        filter_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                work();
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

        final CharSequence[] items = {"Doesn't matter", "Atheist", "Agnostic", "Spiritual", "Buddhist", "Christian", "Hindu", "Muslim", "Jewish", "Parsi",
                "Sikh", "Jain", "Inter-Religion", "Other"};

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Filter.this);
        alertDialogBuilder.setTitle("Choose Religion");
        int position;
        switch (filter_religion.getText().toString()) {
            case "Doesn't matter": position=0; break;
            case "Atheist": position=1; break;
            case "Agnostic": position=2; break;
            case "Spiritual": position=3; break;
            case "Buddhist": position=4; break;
            case "Christian": position=5; break;
            case "Hindu": position=6; break;
            case "Muslim": position=7; break;
            case "Jewish": position=8; break;
            case "Parsi": position=9; break;
            case "Sikh": position=10; break;
            case "Jain": position=11; break;
            case "Inter-Religion": position=12; break;
            case "Other": position=13; break;
            default: position = -1; break;
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
        final CharSequence[] items = {"Doesn't matter", "Yes", "No", "Planning to get" };

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Filter.this);
        alertDialogBuilder.setTitle("Tattoos?");
        int position;
        switch (filter_tattoos.getText().toString()) {
            case "Doesn't matter": position = 0; break;
            case "Yes": position = 1; break;
            case "No": position = 2; break;
            case "Planning to get": position = 3; break;
            default: position = -1; break;
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

        final CharSequence[] items = {"Doesn't matter", "Yes", "No", "Planning to get" };

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Filter.this);
        alertDialogBuilder.setTitle("Piercing?");
        int position;
        switch (filter_piercings.getText().toString()) {
            case "Doesn't matter": position = 0; break;
            case "Yes": position = 1; break;
            case "No": position = 2; break;
            case "Planning to get": position = 3; break;
            default: position = -1; break;
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
        Utility.getInstance().populateModel(edu, RawData.getInstance().filtereducation_items);

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
        final CharSequence[] items = { "Doesn't matter", "Single", "Single with Children", "Divorced", "Divorced with Children", "Widowed", "Widowed with Children" };

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Filter.this);
        alertDialogBuilder.setTitle("Choose Gender");
        int position;
        switch (filter_relationship.getText().toString()) {
            case "Doesn't matter": position = 0; break;
            case "Single": position = 1; break;
            case "Single with Children": position = 2; break;
            case "Divorced": position = 3; break;
            case "Divorced with Children": position = 4; break;
            case "Widowed": position = 5; break;
            case "Widowed with Children": position = 6; break;
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


    private void cities()
    {

        ArrayList<MultiSelectModel> edu = new ArrayList<>();
        Utility.getInstance().populateModel(edu, RawData.getInstance().filtercities);

        MultiSelectDialog multiSelectDialog = new MultiSelectDialog()
                .title("Select Preferred Cities") //setting title for dialog
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

                        filter_cities.setText(dataString);
                    }

                    @Override
                    public void onCancel() {
                        Log.d("multidialog","Dialog cancelled");
                    }

                });

        multiSelectDialog.show(getSupportFragmentManager(), "multiSelectDialog");

    }

    private void languages()
    {
        ArrayList<MultiSelectModel> edu = new ArrayList<>();
        Utility.getInstance().populateModel(edu, RawData.getInstance().filterlanguageslist);

        MultiSelectDialog multiSelectDialog = new MultiSelectDialog()
                .title("Select Preferred Languages") //setting title for dialog
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

                        filter_languages.setText(dataString);
                    }

                    @Override
                    public void onCancel() {
                        Log.d("multidialog","Dialog cancelled");
                    }

                });

        multiSelectDialog.show(getSupportFragmentManager(), "multiSelectDialog");
    }


    private void lookingfor() {
        final CharSequence[] items = { "Male", "Female", "Both" };

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Filter.this);
        alertDialogBuilder.setTitle("Interested in");
        int position;
        if (filter_lookingfor.getText().toString().equals("Male")){
            position = 0;
        } else if (filter_lookingfor.getText().toString().equals("Female")){
            position = 1;
        } else if (filter_lookingfor.getText().toString().equals("Both")){
            position = 2;
        }
        else {
            position = -1;
        }
        alertDialogBuilder
                .setSingleChoiceItems(items, position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListView lw = ((AlertDialog) dialog).getListView();
                        Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                        String selectedgend = checkedItem.toString();
                        filter_lookingfor.setText(selectedgend);
                        dialog.dismiss();
                    }

                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void work() {

        ArrayList<MultiSelectModel> edu = new ArrayList<>();
        Utility.getInstance().populateModel(edu, RawData.getInstance().filtercompany_items);

        MultiSelectDialog multiSelectDialog = new MultiSelectDialog()
                .title("Select Company") //setting title for dialog
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

                        filter_work.setText(dataString);
                    }

                    @Override
                    public void onCancel() {
                        Log.d("multidialog","Dialog cancelled");
                    }

                });

        multiSelectDialog.show(getSupportFragmentManager(), "multiSelectDialog");

    }

    private void college() {

        ArrayList<MultiSelectModel> edu = new ArrayList<>();
        Utility.getInstance().populateModel(edu, RawData.getInstance().filtercollege_items);

        MultiSelectDialog multiSelectDialog = new MultiSelectDialog()
                .title("Select College") //setting title for dialog
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

                        filter_college.setText(dataString);
                    }

                    @Override
                    public void onCancel() {
                        Log.d("multidialog","Dialog cancelled");
                    }

                });

        multiSelectDialog.show(getSupportFragmentManager(), "multiSelectDialog");
    }




    public static String centimeterToFeet(String centemeter) {
        int feetPart = 0;
        int inchesPart = 0;
        if(!TextUtils.isEmpty(centemeter)) {
            double dCentimeter = Double.valueOf(centemeter);
            feetPart = (int) Math.floor((dCentimeter / 2.54) / 12);
            System.out.println((dCentimeter / 2.54) - (feetPart * 12));
            inchesPart = (int) Math.ceil((dCentimeter / 2.54) - (feetPart * 12));
        }

        if(inchesPart == 12) {
            inchesPart = 0;
            feetPart = feetPart+1;
        }
        return String.format("%d'%d\"", feetPart, inchesPart);
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
        interestsArary.add(text);

        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedinterests.removeView(view);
                if(view instanceof Chip) {
                    Chip temp = (Chip) view;
                    interestsArary.remove(temp.getChipText().toString());
                }
            }
        });

    }

    void saveDataAsObject() {
        filterObject = new JsonObject();

        String[] heightMin = height_start.getText().toString().replace("\"","").split("\'");
        String[] heightMax = height_end.getText().toString().replace("\"","").split("\'");

        filterObject.addProperty("feetMin",heightMin[0]);

        if(heightMin.length == 1) {
            filterObject.addProperty("inchesMin", 0);
        } else filterObject.addProperty("inchesMin", heightMin[1]);

        filterObject.addProperty("feetMax",heightMax[0]);
        if(heightMax.length == 1) {
            filterObject.addProperty("inchesMax", 0);
        } else filterObject.addProperty("inchesMax", heightMax[1]);

        if(interestsArary.size() > 0)
            filterObject.addProperty("interests", interestsArary.toString());

        if(!filter_relationship.getText().toString().isEmpty() && filter_relationship.getText().toString().length() > 0) {
            if(!filter_relationship.getText().toString().trim().equals(DOESNTMATTER))
                filterObject.addProperty("maritalStatus", filter_relationship.getText().toString());
        }

        if(!filter_lookingfor.getText().toString().isEmpty() && filter_lookingfor.getText().toString().length() > 0)
            filterObject.addProperty("lookingFor", filter_lookingfor.getText().toString());

        if(!filter_tattoos.getText().toString().isEmpty() && filter_tattoos.getText().toString().length() > 0)
            if(!filter_tattoos.getText().toString().trim().equals(DOESNTMATTER))
                filterObject.addProperty("tattoo", filter_tattoos.getText().toString());

        if(!filter_piercings.getText().toString().isEmpty() && filter_piercings.getText().toString().length() > 0)
            if(!filter_piercings.getText().toString().trim().equals(DOESNTMATTER))
                filterObject.addProperty("piercings", filter_piercings.getText().toString());

        if(!age_start.getText().toString().isEmpty() && age_start.getText().toString().length() > 0)
            filterObject.addProperty("ageMin", age_start.getText().toString());

        if(!age_end.getText().toString().isEmpty() && age_end.getText().toString().length() > 0)
            filterObject.addProperty("ageMax", age_end.getText().toString());

        if(!filter_education.getText().toString().isEmpty() && filter_education.getText().toString().length() > 0)
            if(!filter_education.getText().toString().trim().equals(DOESNTMATTER))
                filterObject.addProperty("education", filter_education.getText().toString().trim());

        if(!filter_religion.getText().toString().isEmpty() && filter_religion.getText().toString().length() > 0)
            if(!filter_religion.getText().toString().trim().equals(DOESNTMATTER))
                filterObject.addProperty("religions", filter_religion.getText().toString().trim());

        if(!filter_college.getText().toString().isEmpty() && filter_college.getText().toString().length() > 0)
            if(!filter_college.getText().toString().trim().equals(DOESNTMATTER))
                filterObject.addProperty("colleges", filter_college.getText().toString().trim());

        if(!filter_work.getText().toString().isEmpty() && filter_work.getText().toString().length() > 0)
            if(!filter_work.getText().toString().trim().equals(DOESNTMATTER))
                filterObject.addProperty("work", filter_work.getText().toString().trim());

        if(!filter_cities.getText().toString().isEmpty() && filter_cities.getText().toString().length() > 0)
            if(!filter_cities.getText().toString().trim().equals(DOESNTMATTER))
                filterObject.addProperty("preferredCities", filter_cities.getText().toString().trim());

        if(!filter_languages.getText().toString().isEmpty() && filter_languages.getText().toString().length() > 0) {
            if (!filter_languages.getText().toString().trim().equals(DOESNTMATTER))
                filterObject.addProperty("langs", filter_languages.getText().toString().trim());
        }

        if(!filter_min_annual.getText().toString().isEmpty() && filter_min_annual.getText().toString().length() > 0)
            filterObject.addProperty("annualIncomeMin", filter_min_annual.getText().toString().replace(",", ""));

        if(!filter_max_annual.getText().toString().isEmpty() && filter_max_annual.getText().toString().length() > 0)
            filterObject.addProperty("annualIncomeMax", filter_max_annual.getText().toString().replace(",",""));

        Log.e("ASD", filterObject.toString());
        storeSPData("filterObject",filterObject.toString());

    }

    private void storeSPData(String key, String data) {

        SharedPreferences mUserData = this.getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor mUserEditor = mUserData.edit();
        mUserEditor.putString(key, data);
        mUserEditor.commit();

    }

    private String getSPData(String key) {

        SharedPreferences mUserData = this.getSharedPreferences("UserData", MODE_PRIVATE);
        String data = mUserData.getString(key, "");

        return data;
    }

}
