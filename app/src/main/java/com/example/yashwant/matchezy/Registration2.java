package com.example.yashwant.matchezy;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.scalified.fab.ActionButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class Registration2 extends AppCompatActivity {

    EditText editText_gender,editText_interested,editText_city,editText_relationship,editText_lang;
    private TextInputLayout inputLayoutgender, inputLayoutinterested, inputLayoutrelationship,inputLayoutlang,inputLayoutcity;

    private static final int LANGUAGES_SPOKEN = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration2);

        inputLayoutgender = (TextInputLayout) findViewById(R.id.inputlayout_gender);
        inputLayoutinterested = (TextInputLayout) findViewById(R.id.inputlayout_interested);
        inputLayoutrelationship = (TextInputLayout) findViewById(R.id.inputlayout_relationship);
        inputLayoutlang= (TextInputLayout)findViewById(R.id.inputlayout_lang);
        inputLayoutcity= (TextInputLayout)findViewById(R.id.inputlayout_city);




        editText_gender = (EditText)findViewById(R.id.edit_gender);
        editText_city=(EditText)findViewById(R.id.edit_city);
        editText_lang=(EditText)findViewById(R.id.edit_lang);
        editText_interested=(EditText)findViewById(R.id.edit_interested);
        editText_relationship =(EditText)findViewById(R.id.edit_relationship);
        editText_gender.setShowSoftInputOnFocus(false);
        editText_lang.setShowSoftInputOnFocus(false);
        editText_relationship.setShowSoftInputOnFocus(false);
        editText_interested.setShowSoftInputOnFocus(false);

        String facebookData = getSPData("facebookdata");
        try {
            JSONObject fbJsonObj = new JSONObject(facebookData);
            editText_gender.setText(fbJsonObj.optString("gender"));
            JSONObject locationDataOnj = fbJsonObj.optJSONObject("location");
            editText_city.setText(locationDataOnj.optString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        editText_gender.addTextChangedListener(new Registration2.MyTextWatcher(editText_gender));
        editText_city.addTextChangedListener(new Registration2.MyTextWatcher(editText_city));
        editText_interested.addTextChangedListener(new Registration2.MyTextWatcher(editText_interested));
        editText_relationship.addTextChangedListener(new Registration2.MyTextWatcher(editText_relationship));
        editText_lang.addTextChangedListener(new Registration2.MyTextWatcher(editText_lang));




        final ActionButton actionButton = (ActionButton) findViewById(R.id.action_button_next2);
        // actionButton.hide();
        actionButton.setType(ActionButton.Type.DEFAULT);
        //actionButton.setSize(65.0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            actionButton.setButtonColor(Color.parseColor("#EA5251"));
        }
        actionButton.setRippleEffectEnabled(true);
        actionButton.playShowAnimation();
        actionButton.setImageResource(R.drawable.ic_action_arrow);




        editText_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                city();
            }
        });

        editText_city.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if(hasFocus) {
                    city();
                }

            }
        });



        editText_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lang();
            }
        });

        editText_lang.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if(hasFocus) {
                    lang();
                }

            }
        });



        editText_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               gender();
            }
        });


        editText_gender.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if(hasFocus) {
                    gender();
                }

            }
        });

        editText_interested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Interested();

            }
        });

        editText_interested.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if(hasFocus) {
                 Interested();
                }

            }
        });



        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submitForm();
            }
        });


        editText_relationship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                relationship();
            }
        });

        editText_relationship.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if(hasFocus) {
                    relationship();
                }

            }
        });



    }


    private void Interested()

    {
        final CharSequence[] items = { "Male", "Female", "Both" };

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Registration2.this);
        alertDialogBuilder.setTitle("Interested in");
        int position;
        if (editText_interested.getText().toString().equals("Male")){
            position = 0;
        } else if (editText_interested.getText().toString().equals("Female")){
            position = 1;
        } else if (editText_interested.getText().toString().equals("Both")){
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
                        editText_interested.setText(selectedgend);
                        dialog.dismiss();
                    }

                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void gender()
    {

        final CharSequence[] items = { "Male", "Female", "Other" };

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Registration2.this);
        alertDialogBuilder.setTitle("Choose Gender");
        int position;
        if (editText_gender.getText().toString().equals("Male")){
            position = 0;
        } else if (editText_gender.getText().toString().equals("Female")){
            position = 1;
        } else if (editText_gender.getText().toString().equals("Other")){
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
                        editText_gender.setText(selectedgend);
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }




    private void relationship()

    {
        final CharSequence[] items = { "Single", "Single with Children", "Divorced", "Divorced with Children", "Widowed", "Widowed with Children"};

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Registration2.this);
        alertDialogBuilder.setTitle("Choose Gender");
        int position;
        if (editText_relationship.getText().toString().equals("Single")){
            position = 0;
        } else if (editText_relationship.getText().toString().equals("Single with Children")){
            position = 1;
        } else if (editText_relationship.getText().toString().equals("Divorced")){
            position = 2;
        }

        else if (editText_relationship.getText().toString().equals("Divorced with Children")){
            position = 3;
        }

        else if (editText_relationship.getText().toString().equals("Widowed")){
            position = 4;
        }

        else if (editText_relationship.getText().toString().equals("Widowed with Children")){
            position = 5;
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
                        editText_relationship.setText(selectedgend);
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void city()
    {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(Registration2.this);
            startActivityForResult(intent, 1000);

        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private void lang()
    {
        Intent i = new Intent(getApplicationContext(), LanguagesPopUp.class).putExtra("languagesspoken", User.getInstance().languagesspokendirty);
        startActivityForResult(i, LANGUAGES_SPOKEN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            if (resultCode == RESULT_OK) {

                Place place = PlaceAutocomplete.getPlace(this, data);
                editText_city.setText(place.getName());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode == LANGUAGES_SPOKEN) {
            if(resultCode == Activity.RESULT_OK) {

                editText_lang.setText("Languages Spoken");

                String languagesspoken = data.getStringExtra("languagesspoken");
                if(languagesspoken.equals("")) {
                    editText_lang.setText("Select Languages Spoken");
                    editText_lang.setText("");
                }

                else editText_lang.setText(languagesspoken);

            }
        }

    }

    private void submitForm() {
        if (!validateGender()) {
            return;
        }

        if(!validateInterested()) {
            return;
        }

        if(!validateRelationship()) {
            return;
        }

        if (!validateCity()) {
            return;
        }

        if (!validateLang()) {
            return;
        }


        storeSPData("gender", editText_gender.getText().toString().trim());
        storeSPData("lookingfor", editText_interested.getText().toString().trim());
        storeSPData("maritalstatus", editText_relationship.getText().toString().trim());
        storeSPData("city", editText_city.getText().toString().trim());
        storeSPData("lang", editText_lang.getText().toString().trim());

          Intent intent = new Intent(getApplicationContext(),Registration3.class);
                startActivity(intent);

    }

    private boolean validateGender() {
        if (editText_gender.getText().toString().trim().isEmpty()) {
            inputLayoutgender.setError("Enter gender");
            requestFocus(editText_gender);
            return false;
        } else {
            inputLayoutgender.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateInterested() {
        if(editText_interested.getText().toString().trim().isEmpty()) {
            inputLayoutinterested.setError("Enter interested");
            requestFocus(editText_interested);
            return false;
        } else {
            inputLayoutinterested.setErrorEnabled(false);
        }
        return true;
    }


    private boolean validateRelationship() {
        if (editText_relationship.getText().toString().trim().isEmpty()) {
            inputLayoutrelationship.setError("Enter relationship status");
            requestFocus(editText_relationship);
            return false;
        } else {
            inputLayoutrelationship.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateCity() {
        if (editText_city.getText().toString().trim().isEmpty()) {
            inputLayoutcity.setError("Enter City");
            requestFocus(editText_city);
            return false;
        } else {
            inputLayoutrelationship.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateLang() {
        if (editText_lang.getText().toString().trim().isEmpty()) {
            inputLayoutlang.setError("Enter the langauges known");
            requestFocus(editText_lang);
            return false;
        } else {
            inputLayoutlang.setErrorEnabled(false);
        }

        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edit_gender:
                    validateGender();
                    break;


                case R.id.edit_interested:
                    validateInterested();
                    break;

                case R.id.edit_relationship:
                    validateRelationship();
                    break;


                case R.id.edit_city:
                    validateCity();
                    break;


                case R.id.edit_lang:
                    validateLang();
                    break;





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
