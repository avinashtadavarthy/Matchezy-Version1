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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.einheit.matchezy.HomeScreen;
import com.einheit.matchezy.R;
import com.einheit.matchezy.RawData;
import com.einheit.matchezy.Utility;
import com.einheit.matchezy.hometab.CustomDialogClass;
import com.einheit.matchezy.hometab.Filter;
import com.einheit.matchezy.login.Login;
import com.einheit.matchezy.registration.ChooseCity;
import com.einheit.matchezy.registration.LanguagesPopUp;
import com.einheit.matchezy.registration.Registration2;
import com.einheit.matchezy.registration.Registration3;
import com.einheit.matchezy.registration.Registration4;
import com.google.gson.JsonObject;
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
    Switch switchEdu, switchCollege, switchWork, switchAnnual, switchDesig;
    ScrollChoice scroll_choice;
    ActionButton actionButton;

    ArrayList<Integer> existingdataedu = new ArrayList<>(),
            existingdatawork = new ArrayList<>(),
            existingdatacollege = new ArrayList<>();

    ArrayList<String> interestsarr = new ArrayList<>();
    List<String> data = new ArrayList<>();
    String ft, inch, langs="", quali="", colleges="", orgWorked = "";

    int i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        AndroidNetworking.initialize(this);

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

        switchEdu = findViewById(R.id.switchButtonEdu);
        switchCollege = findViewById(R.id.switchButtonCollege);
        switchAnnual = findViewById(R.id.switchButtonAnnual);
        switchWork = findViewById(R.id.switchButtonWorking);
        switchDesig = findViewById(R.id.switchButtonDesignation);


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
                        interestsarr = result;
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
                JsonObject object = new JsonObject();
                object.addProperty("user_id", getSPData("user_id"));
                object.addProperty("user_token", getSPData("user_token"));

                if(edit_gender.getText().toString().trim().isEmpty() || edit_gender.getText().toString().length() == 0 ||
                        edit_relationship.getText().toString().trim().isEmpty() || edit_relationship.getText().toString().length() == 0 ||
                        edit_interested.getText().toString().trim().isEmpty() || edit_interested.getText().toString().length() == 0 ||
                        edit_city.getText().toString().trim().isEmpty() || edit_city.getText().toString().length() == 0 ||
                        edit_lang.getText().toString().trim().isEmpty() || edit_lang.getText().toString().length() == 0 ||
                        editText_religion.getText().toString().trim().isEmpty() || editText_religion.getText().toString().length() == 0 ||
                        editText_tattoos.getText().toString().trim().isEmpty() || editText_tattoos.getText().toString().length() == 0 ||
                        editText_piercing.getText().toString().trim().isEmpty() || editText_piercing.getText().toString().length() == 0 ||
                        editTextEdu.getText().toString().trim().isEmpty() || editTextEdu.getText().toString().length() == 0 ||
                        editTextCollege.getText().toString().trim().isEmpty() || editTextCollege.getText().toString().length() == 0 ||
                        editTextWorking.getText().toString().trim().isEmpty() || editTextWorking.getText().toString().length() == 0 ||
                        editTextDesignation.getText().toString().trim().isEmpty() || editTextDesignation.getText().toString().length() == 0 ||
                        editText_annual.getText().toString().trim().isEmpty() || editText_annual.getText().toString().length() == 0 ||
                        interestsarr.size() == 0) {
                    Toast.makeText(EditProfile.this, "Please fill all the fields properly", Toast.LENGTH_SHORT).show();
                } else {

                    if (!edit_gender.getText().toString().trim().isEmpty() && edit_gender.getText().toString().length() > 0)
                        object.addProperty("gender", edit_gender.getText().toString().trim());

                    if (!edit_relationship.getText().toString().trim().isEmpty() && edit_relationship.getText().toString().length() > 0)
                        object.addProperty("maritalStatus", edit_relationship.getText().toString().trim());

                    if (!edit_interested.getText().toString().trim().isEmpty() && edit_interested.getText().toString().length() > 0)
                        object.addProperty("lookingFor", edit_interested.getText().toString().trim());

                    if (!edit_city.getText().toString().trim().isEmpty() && edit_city.getText().toString().length() > 0)
                        object.addProperty("city", edit_city.getText().toString().trim());

                    if (!edit_lang.getText().toString().trim().isEmpty() && edit_lang.getText().toString().length() > 0)
                        object.addProperty("langs", edit_lang.getText().toString().trim());

                    if (!editText_religion.getText().toString().trim().isEmpty() && editText_religion.getText().toString().length() > 0)
                        object.addProperty("religion", editText_religion.getText().toString().trim());

                    if (!editText_tattoos.getText().toString().trim().isEmpty() && editText_tattoos.getText().toString().length() > 0)
                        object.addProperty("tattoos", editText_tattoos.getText().toString().trim());

                    if (!editText_piercing.getText().toString().trim().isEmpty() && editText_piercing.getText().toString().length() > 0)
                        object.addProperty("piercings", editText_piercing.getText().toString().trim());

                    if (!editTextEdu.getText().toString().trim().isEmpty() && editTextEdu.getText().toString().length() > 0)
                        object.addProperty("education", editTextEdu.getText().toString().trim());

                    if (!editTextCollege.getText().toString().trim().isEmpty() && editTextCollege.getText().toString().length() > 0)
                        object.addProperty("college", editTextCollege.getText().toString().trim());

                    if (!editTextWorking.getText().toString().trim().isEmpty() && editTextWorking.getText().toString().length() > 0)
                        object.addProperty("work", editTextWorking.getText().toString().trim());

                    if (!editTextDesignation.getText().toString().trim().isEmpty() && editTextDesignation.getText().toString().length() > 0)
                        object.addProperty("desig", editTextDesignation.getText().toString().trim());

                    if (!editText_annual.getText().toString().trim().isEmpty() && editText_annual.getText().toString().length() > 0)
                        object.addProperty("annualIncome", editText_annual.getText().toString().trim());

                    if (interestsarr.size() > 0)
                        object.addProperty("interests", interestsarr.toString());

                    if(!scroll_choice.getCurrentSelection().isEmpty()) {
                        String[] height = scroll_choice.getCurrentSelection().trim().split("'");
                        object.addProperty("feet", height[0]);
                        if(height.length == 1)
                            object.addProperty("inches", "0");
                        else object.addProperty("inches", height[1].replace("\"",""));
                    }

                    object.addProperty("educationVisibility", switchEdu.isChecked());
                    object.addProperty("collegeVisibility", switchCollege.isChecked());
                    object.addProperty("workVisibility", switchWork.isChecked());
                    object.addProperty("desigVisibility", switchDesig.isChecked());
                    object.addProperty("annualVisibility", switchAnnual.isChecked());

                    Log.e("ASd", object.toString());

                    AndroidNetworking.post(Utility.getInstance().BASE_URL + "editProfile")
                            .addBodyParameter(object)
                            .setPriority(Priority.HIGH)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject res) {

                                    Log.e("ASD", res.toString());

                                    if (res.optInt("status_code") == 200) {AndroidNetworking.post(Utility.getInstance().BASE_URL + "getUserData")
                                            .addBodyParameter("user_id", getSPData("user_id"))
                                            .addBodyParameter("user_token", getSPData("user_token"))
                                            .addBodyParameter("user_id_2", getSPData("user_id"))
                                            .setPriority(Priority.HIGH)
                                            .build()
                                            .getAsJSONObject(new JSONObjectRequestListener() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    // do anything with response

                                                    if(response.optInt("status_code") == 200) {
                                                        //Log.e("userdata", response.toString());
                                                        storeSPData("userdata", response.optJSONObject("message").toString());
                                                    }
                                                    else {
                                                        Toast.makeText(EditProfile.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                @Override
                                                public void onError(ANError error) {

                                                    error.printStackTrace();

                                                }
                                            });



                                        finish();
                                        Toast.makeText(EditProfile.this, res.optString("message"), Toast.LENGTH_SHORT).show();

                                    } else
                                        Toast.makeText(EditProfile.this, res.optString("message"), Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onError(ANError error) {
                                    error.printStackTrace();
                                }
                            });
                }

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

        for(i = 0; i<userData.optJSONArray("collegeName").length(); i++)
            colleges = colleges + userData.optJSONArray("collegeName").getString(i) + ", ";
        colleges = colleges.substring(0, colleges.length()-2);
        editTextCollege.setText(colleges);

        for(i = 0; i<userData.optJSONArray("organisationWorked").length(); i++)
            orgWorked = orgWorked + userData.optJSONArray("organisationWorked").getString(i) + ", ";
        orgWorked = orgWorked.substring(0, orgWorked.length()-2);
        editTextWorking.setText(orgWorked);

        editTextDesignation.setText(userData.optString("currentDesignation"));
        editText_annual.setText(userData.optString("annualIncome"));

        switchEdu.setChecked(userData.optBoolean("qualificationVisibility"));
        switchCollege.setChecked(userData.optBoolean("collegeNameVisibility"));
        switchWork.setChecked(userData.optBoolean("organisationWorkedVisibility"));
        switchDesig.setChecked(userData.optBoolean("currentDesignationVisibility"));
        switchAnnual.setChecked(userData.optBoolean("annualIncomeVisibility"));

        ft = userData.optJSONObject("height").optString("feet");
        inch = userData.optJSONObject("height").optString("inches");
        int index = -1;
        if(!inch.equals("0"))
            index = data.indexOf(ft + "'" + inch + "\"");
        else index = data.indexOf(ft + "'");
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
        final CharSequence[] items = {"Atheist", "Agnostic", "Spiritual", "Buddhist", "Christian", "Hindu", "Muslim", "Jewish", "Parsi",
                "Sikh", "Jain", "Inter-Religion", "Other"};

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditProfile.this);
        alertDialogBuilder.setTitle("Choose Religion");
        int position;
        switch (editText_religion.getText().toString()) {
            case "Atheist": position=0; break;
            case "Agnostic": position=1; break;
            case "Spiritual": position=2; break;
            case "Buddhist": position=3; break;
            case "Christian": position=4; break;
            case "Hindu": position=5; break;
            case "Muslim": position=6; break;
            case "Jewish": position=7; break;
            case "Parsi": position=8; break;
            case "Sikh": position=9; break;
            case "Jain": position=10; break;
            case "Inter-Religion": position=11; break;
            case "Other": position=12; break;
            default: position = -1; break;
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
        Utility.getInstance().populateModel(edu, RawData.getInstance().education_items);

        MultiSelectDialog multiSelectDialog = new MultiSelectDialog()
                .title("Select Education") //setting title for dialog
                .titleSize(25)
                .positiveText("Done")
                .negativeText("Cancel")
                .setMinSelectionLimit(1) //you can set minimum checkbox selection limit (Optional)
                .preSelectIDsList(existingdataedu)
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

                         String datadisplayed = "";
                        for(int i = 0; i<=selectedNames.size()-1; i++) {
                            if(i!=selectedNames.size()-1) datadisplayed = datadisplayed + selectedNames.get(i) + ", ";
                            else datadisplayed = datadisplayed + selectedNames.get(i);
                        }

                        editTextEdu.setText(datadisplayed);
                        existingdataedu = selectedIds;
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
        Utility.getInstance().populateModel(edu, RawData.getInstance().company_items);

        MultiSelectDialog multiSelectDialog = new MultiSelectDialog()
                .title("Select Company") //setting title for dialog
                .titleSize(25)
                .positiveText("Done")
                .negativeText("Cancel")
                .setMinSelectionLimit(1) //you can set minimum checkbox selection limit (Optional)
                .preSelectIDsList(existingdatawork)
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

                       String datadisplayed = "";
                        for(int i = 0; i<=selectedNames.size()-1; i++) {
                            if(i!=selectedNames.size()-1) datadisplayed = datadisplayed + selectedNames.get(i) + "," + '\n';
                            else datadisplayed = datadisplayed + selectedNames.get(i);
                        }

                        editTextWorking.setText(datadisplayed);
                        existingdatawork = selectedIds;
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
        Utility.getInstance().populateModel(edu, RawData.getInstance().college_items);

        MultiSelectDialog multiSelectDialog = new MultiSelectDialog()
                .title("Select College") //setting title for dialog
                .titleSize(25)
                .positiveText("Done")
                .negativeText("Cancel")
                .setMinSelectionLimit(1) //you can set minimum checkbox selection limit (Optional)
                .preSelectIDsList(existingdatacollege)
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

                       String datadisplayed = "";
                        for(int i = 0; i<=selectedNames.size()-1; i++) {
                            if(i!=selectedNames.size()-1) datadisplayed = datadisplayed + selectedNames.get(i) + "," + '\n';
                            else datadisplayed = datadisplayed + selectedNames.get(i);
                        }

                        editTextCollege.setText(datadisplayed);
                        existingdatacollege = selectedIds;
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
