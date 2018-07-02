package com.einheit.matchezy.profilescreen;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.bumptech.glide.Glide;
import com.einheit.matchezy.R;
import com.einheit.matchezy.Utility;
import com.einheit.matchezy.hometab.CustomDialogClass;
import com.einheit.matchezy.hometab.Filter;
import com.einheit.matchezy.registration.ChooseCity;
import com.einheit.matchezy.registration.LanguagesPopUp;
import com.einheit.matchezy.registration.Registration2;
import com.einheit.matchezy.registration.Registration3;
import com.einheit.matchezy.registration.Registration4;
import com.makeramen.roundedimageview.RoundedImageView;
import com.scalified.fab.ActionButton;
import com.webianks.library.scroll_choice.ScrollChoice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditProfile extends AppCompatActivity {

    ImageView backbtn;
    TextView name, age, email, phonenumber;
    RoundedImageView profileimage;
    ImageView edit_interests;
    ChipGroup chipgroup_interests;
    EditText edit_gender, edit_interested, edit_relationship, edit_city, edit_lang, editText_religion, editText_tattoos, editText_piercing, editTextEdu, editTextCollege, editTextWorking, editTextDesignation, editText_annual;
    ScrollChoice scroll_choice;
    ActionButton actionButton;

    ArrayList<String> interestsarr = new ArrayList<>();
    List<String> data = new ArrayList<>();
    String ft, inch, langs="", quali="", college="";

    int i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        backbtn = findViewById(R.id.backbtn);
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        email = findViewById(R.id.email);
        phonenumber = findViewById(R.id.phonenumber);
        profileimage = findViewById(R.id.profileimage);
        edit_interests = findViewById(R.id.edit_interests);
        chipgroup_interests = findViewById(R.id.chipgroup_interests);
        edit_gender = findViewById(R.id.edit_gender);
        edit_interested = findViewById(R.id.edit_interested);
        edit_relationship = findViewById(R.id.edit_relationship);
        edit_city = findViewById(R.id.edit_city);
        edit_lang = findViewById(R.id.edit_lang);
        editText_religion = findViewById(R.id.editText_religion);
        editText_tattoos = findViewById(R.id.editText_tattoos);
        editText_piercing = findViewById(R.id.editText_piercing);
        editTextEdu = findViewById(R.id.editTextEdu);
        editTextCollege = findViewById(R.id.editTextCollege);
        editTextWorking = findViewById(R.id.editTextWorking);
        editTextDesignation = findViewById(R.id.editTextDesignation);
        editText_annual = findViewById(R.id.editText_annual);
        scroll_choice = findViewById(R.id.scroll_choice);
        actionButton = findViewById(R.id.action_edit_done);


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


        actionButton.setType(ActionButton.Type.DEFAULT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            actionButton.setButtonColor(Color.parseColor("#EA5251"));
        }
        actionButton.setRippleEffectEnabled(true);
        actionButton.playShowAnimation();
        actionButton.setImageResource(R.drawable.done);


        try {
            setExistingdata();
        } catch (JSONException e) {
            e.printStackTrace();
        }



        edit_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender();
            }
        });

        edit_interested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Interested();
            }
        });

        edit_relationship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relationship();
            }
        });

        edit_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                city();
            }
        });

        edit_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lang();
            }
        });

        editText_religion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                religion();
            }
        });

        editText_tattoos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tattoos();
            }
        });

        editText_piercing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                piercing();
            }
        });

        editTextEdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                education();
            }
        });

        editTextCollege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                college();
            }
        });

        editTextWorking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                company();
            }
        });

        editTextDesignation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                designation();
            }
        });


        edit_interests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InterestSelectorDialog isd = new InterestSelectorDialog(EditProfile.this, interestsarr);
                isd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                isd.show();
                isd.setDialogResult(new InterestSelectorDialog.OnMyDialogResult() {
                    public void finish(ArrayList<String> result) {
                        chipgroup_interests.removeAllViews();
                        populateSuggestedChips(result);
                    }
                });

            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(EditProfile.this);
                builder1.setMessage("You will lose all your edits (if any). \n\nDo you want to continue?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                               finish();
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


        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Toast.makeText(EditProfile.this, "Have to send fields to server and edit!!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setExistingdata() throws JSONException {

        JSONObject userData = new JSONObject(getSPData("userdata"));

        String[] firstname = userData.optString("name").split(" ");
        name.setText(firstname[0]);
        email.setText(userData.optString("email"));
        phonenumber.setText(userData.optString("phoneNumber"));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date date = format.parse(String.valueOf(userData.optString("dob")));
            age.setText(", " + Utility.getInstance().getAge(date.getYear() + 1900, date.getMonth(), date.getDay()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Glide.with(getApplicationContext()).load(userData.optString("profileImageURL")).into(profileimage);

        for(i = 0; i<userData.optJSONArray("interests").length(); i++)
            interestsarr.add(userData.optJSONArray("interests").getString(i));
        populateSuggestedChips(interestsarr);

        edit_gender.setText(userData.optString("gender"));
        edit_interested.setText(userData.optString("lookingFor"));
        edit_relationship.setText(userData.optString("maritalStatus"));
        edit_city.setText(userData.optString("currentCity"));

        for(i = 0; i<userData.optJSONArray("languagesKnown").length(); i++)
            langs = langs + userData.optJSONArray("languagesKnown").getString(i) + ", ";
        langs = langs.substring(0,langs.length()-2);
        edit_lang.setText(langs);

        editText_religion.setText(userData.optString("religion"));
        editText_tattoos.setText(userData.optString("tattoo"));
        editText_piercing.setText(userData.optString("piercings"));

        for(i = 0; i<userData.optJSONArray("qualification").length(); i++)
            quali = quali + userData.optJSONArray("qualification").getString(i) + ", ";
        quali = quali.substring(0, quali.length()-2);
        editTextEdu.setText(quali);

        editTextCollege.setText(userData.optString("collegeName"));
        editTextWorking.setText(userData.optString("organisationWorked"));
        editTextDesignation.setText(userData.optString("currentDesignation"));
        editText_annual.setText(userData.optString("annualIncome"));

        ft = userData.optJSONObject("height").optString("feet");
        inch = userData.optJSONObject("height").optString("inches");
        int index = data.indexOf(ft + "'" + inch + "\"");
        scroll_choice.addItems(data, index);

    }



    private void populateSuggestedChips(ArrayList<String> arr) {

        for (int i=0;i<arr.size();i++) {
            Chip chip = new Chip(EditProfile.this);
            chip.setChipText(arr.get(i));
            chip.setTextAppearanceResource(R.style.ChipTextStyle);

            chipgroup_interests.addView(chip);
        }

    }


    private void gender()
    {

        final CharSequence[] items = { "Male", "Female", "Other" };

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditProfile.this);
        alertDialogBuilder.setTitle("Choose Gender");
        int position;
        if (edit_gender.getText().toString().equals("Male")){
            position = 0;
        } else if (edit_gender.getText().toString().equals("Female")){
            position = 1;
        } else if (edit_gender.getText().toString().equals("Other")){
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
                        edit_gender.setText(selectedgend);
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }



    private void Interested()

    {
        final CharSequence[] items = { "Male", "Female", "Both" };

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditProfile.this);
        alertDialogBuilder.setTitle("Interested in");
        int position;
        if (edit_interested.getText().toString().equals("Male")){
            position = 0;
        } else if (edit_interested.getText().toString().equals("Female")){
            position = 1;
        } else if (edit_interested.getText().toString().equals("Both")){
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
                        edit_interested.setText(selectedgend);
                        dialog.dismiss();
                    }

                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    private void relationship()

    {
        final CharSequence[] items = { "Single", "Single with Children", "Divorced", "Divorced with Children", "Widowed", "Widowed with Children" };

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditProfile.this);
        alertDialogBuilder.setTitle("Choose Gender");
        int position;
        switch (edit_relationship.getText().toString()) {
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
                edit_relationship.setText(selectedgend);
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void city()
    {
        Intent intnt = new Intent(getApplicationContext(), ChooseCity.class);
        startActivityForResult(intnt, 12345);
    }

    private void lang()
    {
        Intent i = new Intent(getApplicationContext(), LanguagesPopUp.class).putExtra("languagesspoken", Utility.getInstance().languagesspokendirty);
        startActivityForResult(i, 1010);
    }


    private void religion()

    {
        final CharSequence[] items = {"Hindu", "Muslim", "Sikh", "Christian", "Jain", "Parsi", "Buddhist", "Inter-Religion"};

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditProfile.this);
        alertDialogBuilder.setTitle("Choose Religion");
        int position;
        switch (editText_religion.getText().toString()) {
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
                        editText_religion.setText(selectedgend);
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void tattoos()

    {
        final CharSequence[] items = { "Yes","No","Planning to get"};

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditProfile.this);
        alertDialogBuilder.setTitle("Tattoos?");
        int position;
        switch (editText_tattoos.getText().toString()) {
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
                        editText_tattoos.setText(selectedgend);
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void piercing()

    {
        final CharSequence[] items = { "Yes","No","Planning to get"};

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditProfile.this);
        alertDialogBuilder.setTitle("Piercing?");
        int position;
        switch (editText_piercing.getText().toString()) {
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
                        editText_piercing.setText(selectedgend);
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void education() {

        ArrayList<MultiSelectModel> edu = new ArrayList<>();
        Utility.getInstance().populateModel(edu, Utility.getInstance().education_items);

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

                        editTextEdu.setText(dataString);
                    }

                    @Override
                    public void onCancel() {
                        Log.d("multidialog","Dialog cancelled");
                    }

                });

        multiSelectDialog.show(getSupportFragmentManager(), "multiSelectDialog");

    }


    private void company() {

        ArrayList<MultiSelectModel> edu = new ArrayList<>();
        Utility.getInstance().populateModel(edu, Utility.getInstance().company_items);

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

                        editTextWorking.setText(dataString);
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
        Utility.getInstance().populateModel(edu, Utility.getInstance().college_items);

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

                        editTextCollege.setText(dataString);
                    }

                    @Override
                    public void onCancel() {
                        Log.d("multidialog","Dialog cancelled");
                    }

                });

        multiSelectDialog.show(getSupportFragmentManager(), "multiSelectDialog");
    }

    private void designation() {

        final CharSequence[] items = {
                "Chairman", "Vice Chairman", "Chairman cum Managing Director", "Managing Director", "Sr. Vice president ", "Vice President", "General Manager",
                "Joint General Manager", "Deputy General Manager", "Asst. General Manager", "Chief Manager", "Sr. Manager", "Manager", "Joint Manager", "Deputy Manager",
                "Asst. Manager", "Sr. Officer", "Officer", "Jr. Officer", "Sr. Associate", "Associate", "Jr. Associate", "Assistant ", "Trainee Engineer", "Software Engineer",
                "Programmer Analyst", "Senior Software Engineer", "System Analyst", "Project Lead", "Project Manager", "Program Manager ", "Team Lead", "Senior Team Lead",
                "Account Manager", "Architect", "Technical Specialist", "Deliver Manager", "Delivery Head", "Business Analyst", "Delivery Partner"
        };

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditProfile.this);
        alertDialogBuilder.setTitle("Choose Designation");
        int position;
        switch (editTextDesignation.getText().toString()) {
            case "Chairman":
                position = 0;
                break;
            case "Vice Chairman":
                position = 1;
                break;
            case "Chairman cum Managing Director":
                position = 2;
                break;
            case "Managing Director":
                position = 3;
                break;
            case "Sr. Vice president ":
                position = 4;
                break;
            case "Vice President":
                position = 5;
                break;
            case "General Manager":
                position = 6;
                break;
            case "Joint General Manager":
                position = 7;
                break;
            case "Deputy General Manager":
                position = 8;
                break;
            case "Asst. General Manager":
                position = 9;
                break;
            case "Chief Manager":
                position = 10;
                break;
            case "Sr. Manager":
                position = 11;
                break;
            case "Manager":
                position = 12;
                break;
            case "Joint Manager":
                position = 13;
                break;
            case "Deputy Manager":
                position = 14;
                break;
            case "Asst. Manager":
                position = 15;
                break;
            case "Sr. Officer":
                position = 16;
                break;
            case "Officer":
                position = 17;
                break;
            case "Jr. Officer":
                position = 18;
                break;
            case "Sr. Associate":
                position = 19;
                break;
            case "Associate":
                position = 20;
                break;
            case "Jr. Associate":
                position = 21;
                break;
            case "Assistant ":
                position = 22;
                break;
            case "Trainee Engineer":
                position = 23;
                break;
            case "Software Engineer":
                position = 24;
                break;
            case "Programmer Analyst":
                position = 25;
                break;
            case "Senior Software Engineer":
                position = 26;
                break;
            case "System Analyst":
                position = 27;
                break;
            case "Project Lead":
                position = 28;
                break;
            case "Project Manager":
                position = 29;
                break;
            case "Program Manager ":
                position = 30;
                break;
            case "Team Lead":
                position = 31;
                break;
            case "Senior Team Lead":
                position = 32;
                break;
            case "Account Manager":
                position = 33;
                break;
            case "Architect":
                position = 34;
                break;
            case "Technical Specialist":
                position = 35;
                break;
            case "Deliver Manager":
                position = 36;
                break;
            case "Delivery Head":
                position = 37;
                break;
            case "Business Analyst":
                position = 38;
                break;
            case "Delivery Partner":
                position = 39;
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
                        editTextDesignation.setText(selectedgend);
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 12345) {
            if (resultCode == RESULT_OK) {

                String chosencity = data.getStringExtra("chosencity");
                edit_city.setText(chosencity);

            }
        }

        if(requestCode == 1010) {
            if(resultCode == Activity.RESULT_OK) {

                edit_lang.setText("Languages Spoken");

                String languagesspoken = data.getStringExtra("languagesspoken");
                if(languagesspoken.equals("")) {
                    edit_lang.setText("Select Languages Spoken");
                    edit_lang.setText("");
                }

                else edit_lang.setText(languagesspoken);

            }
        }

    }


    //Shared Preferences
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
