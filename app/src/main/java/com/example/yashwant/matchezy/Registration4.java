package com.example.yashwant.matchezy;

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
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.scalified.fab.ActionButton;

import org.json.JSONObject;

public class Registration4 extends AppCompatActivity {


    EditText editText_work,editText_annual,editText_college,editText_edu,editText_desig;
    private TextInputLayout inputLayoutWorking, inputLayoutCollege, inputLayoutAnnual,inputLayoutEdu, inputLayoutDesig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration4);

        AndroidNetworking.initialize(this);

        
        inputLayoutAnnual=(TextInputLayout)findViewById(R.id.inputLayoutAnnualIncome);
        inputLayoutCollege=(TextInputLayout)findViewById(R.id.inputLayoutCollege);
        inputLayoutEdu=(TextInputLayout)findViewById(R.id.inputLayoutEdu);
        inputLayoutWorking=(TextInputLayout)findViewById(R.id.inputLayoutWorking);
        inputLayoutDesig=(TextInputLayout)findViewById(R.id.inputLayoutDesignation);

        editText_annual=(EditText)findViewById(R.id.editText_annual);
        editText_college=(EditText)findViewById(R.id.editTextCollege);
        editText_edu=(EditText)findViewById(R.id.editTextEdu);
        editText_work=(EditText)findViewById(R.id.editTextWorking);
        editText_desig=(EditText)findViewById(R.id.editTextDesignation);

        editText_edu.setShowSoftInputOnFocus(false);

        editText_edu.addTextChangedListener(new Registration4.MyTextWatcher(editText_edu));
        editText_college.addTextChangedListener(new Registration4.MyTextWatcher(editText_college));
        editText_work.addTextChangedListener(new Registration4.MyTextWatcher(editText_work));
        editText_annual.addTextChangedListener(new Registration4.MyTextWatcher(editText_annual));
        editText_desig.addTextChangedListener(new Registration4.MyTextWatcher(editText_desig));


        //add validation for designation and change up stuff, then make a user on server, fb error handling


        editText_edu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                education();
            }
        });


        editText_edu.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if (hasFocus) {
                    education();
                }

            }
        });


        final ActionButton actionButton = (ActionButton) findViewById(R.id.action_button_next3);
        // actionButton.hide();
        actionButton.setType(ActionButton.Type.DEFAULT);
        //actionButton.setSize(65.0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            actionButton.setButtonColor(Color.parseColor("#EA5251"));
        }
        actionButton.setRippleEffectEnabled(true);
        actionButton.playShowAnimation();
        actionButton.setImageResource(R.drawable.ic_action_arrow);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();

            }
        });

        /*editText_education= (EditText)findViewById(R.id.edit_edu);
        editText_income =(EditText)findViewById(R.id.edit_income);*/
    }

    private void submitForm() {
        if (!validateedu()) {
            return;
        }

        if (!validatecollege()) {
            return;
        }

        if(!validatework()) {
            return;
        }

        if(!validatedesig()) {
            return;
        }

        if(!validateannual()) {
            return;
        }

        storeSPData("education", editText_edu.getText().toString().trim());
        storeSPData("college", editText_college.getText().toString().trim());
        storeSPData("work", editText_work.getText().toString().trim());
        storeSPData("desig", editText_desig.getText().toString().trim());
        storeSPData("annual_income", editText_annual.getText().toString().trim());



        Intent intent = new Intent(getApplicationContext(),Registration_Interests.class);
        startActivity(intent);

    }

    private void education() {

        final CharSequence[] items = {"Aeronautical Engineering", "B.Arch", "BCA", "BE", "B.Plan", "B.Sc IT/ Computer Science", "B.Tech.",
                "Other Bachelor Degree in Engineering / Computers", "B.S.(Engineering)", "M.Arch.", "MCA", "ME", "M.Sc. IT / Computer Science",
                "M.S.(Engg.)", "M.Tech.", "PGDCA", "Other Masters Degree in Engineering / Computers", "Aviation Degree", "B.A.", "B.Com.", "B.Ed.",
                "BFA", "BFT", "BLIS", "B.M.M.", "B.Sc.", "B.S.W", "B.Phil.", "Other Bachelor Degree in Arts / Science / Commerce", "M.A.", "MCom", "M.Ed.",
                "MFA", "MLIS", "M.Sc.", "MSW", "M.Phil.", "Other Master Degree in Arts / Science / Commerce", "BBA", "BFM (Financial Management)",
                "BHM (Hotel Management)", "Other Bachelor Degree in Management", "MBA", "MFM (Financial Management)", "MHM  (Hotel Management)",
                "MHRM (Human Resource Management)", "PGDM", "Other Master Degree in Management", "B.A.M.S.", "BDS", "BHMS", "BSMS", "BPharm", "BPT",
                "BUMS", "BVSc", "MBBS", "B.Sc. Nursing", "Other Bachelor Degree in Medicine", "MDS", "MD / MS (Medical)", "M.Pharm", "MPT", "MVSc",
                "Other Master Degree in Medicine", "BGL", "B.L.", "LL.B.", "Other Bachelor Degree in Legal", "LL.M.", "M.L.", "Other Master Degree in  Legal",
                "CA", "CFA (Chartered Financial Analyst)", "CS", "ICWA", "Other Degree in Finance", "IAS", "IES", "IFS", "IRS", "IPS", "Other Degree in Service",
                "Ph.D.", "Diploma", "Polytechnic", "Trade School", "Others in Diploma"};

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Registration4.this);
        alertDialogBuilder.setTitle("Choose highest educational qualification");
        int position = choosePositionEdu();

        alertDialogBuilder
                .setSingleChoiceItems(items, position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListView lw = ((AlertDialog) dialog).getListView();
                        Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                        String selectedgend = checkedItem.toString();
                        editText_edu.setText(selectedgend);
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private int choosePositionEdu() {
        int position;

        switch (editText_edu.getText().toString()) {
            case "Aeronautical Engineering": position = 0; break;
            case "B.Arch": position = 1; break;
            case "BCA": position = 2; break;
            case "BE": position = 3; break;
            case "B.Plan": position = 4; break;
            case "B.Sc IT/ Computer Science": position = 5; break;
            case "B.Tech.": position = 6; break;
            case "Other Bachelor Degree in Engineering / Computers": position = 7; break;
            case "B.S.(Engineering)": position = 8; break;
            case "M.Arch.": position = 9; break;
            case "MCA": position = 10; break;
            case "ME": position = 11; break;
            case "M.Sc. IT / Computer Science": position = 12; break;
            case "M.S.(Engg.)": position = 13; break;
            case "M.Tech.": position = 14; break;
            case "PGDCA": position = 15; break;
            case "Other Masters Degree in Engineering / Computers": position = 16; break;
            case "Aviation Degree": position = 17; break;
            case "B.A.": position = 18; break;
            case "B.Com.": position = 19; break;
            case "B.Ed.": position = 20; break;
            case "BFA": position = 21; break;
            case "BFT": position = 22; break;
            case "BLIS": position = 23; break;
            case "B.M.M.": position = 24; break;
            case "B.Sc.": position = 25; break;
            case "B.S.W": position = 26; break;
            case "B.Phil.": position = 27; break;
            case "Other Bachelor Degree in Arts / Science / Commerce": position = 28; break;
            case "M.A.": position = 29; break;
            case "MCom": position = 30; break;
            case "M.Ed.": position = 31; break;
            case "MFA": position = 32; break;
            case "MLIS": position = 33; break;
            case "M.Sc.": position = 34; break;
            case "MSW": position = 35; break;
            case "M.Phil.": position = 36; break;
            case "Other Master Degree in Arts / Science / Commerce": position = 37; break;
            case "BBA": position = 38; break;
            case "BFM (Financial Management)": position = 39; break;
            case "BHM (Hotel Management)": position = 40; break;
            case "Other Bachelor Degree in Management": position = 41; break;
            case "MBA": position = 42; break;
            case "MFM (Financial Management)": position = 43; break;
            case "MHM  (Hotel Management)": position = 44; break;
            case "MHRM (Human Resource Management)": position = 45; break;
            case "PGDM": position = 46; break;
            case "Other Master Degree in Management": position = 47; break;
            case "B.A.M.S.": position = 48; break;
            case "BDS": position = 49; break;
            case "BHMS": position = 50; break;
            case "BSMS": position = 51; break;
            case "BPharm": position = 52; break;
            case "BPT": position = 53; break;
            case "BUMS": position = 54; break;
            case "BVSc": position = 55; break;
            case "MBBS": position = 56; break;
            case "B.Sc. Nursing": position = 57; break;
            case "Other Bachelor Degree in Medicine": position = 58; break;
            case "MDS": position = 59; break;
            case "MD / MS (Medical)": position = 60; break;
            case "M.Pharm": position = 61; break;
            case "MPT": position = 62; break;
            case "MVSc": position = 63; break;
            case "Other Master Degree in Medicine": position = 64; break;
            case "BGL": position = 65; break;
            case "B.L.": position = 66; break;
            case "LL.B.": position = 67; break;
            case "Other Bachelor Degree in Legal": position = 68; break;
            case "LL.M.": position = 69; break;
            case "M.L.": position = 70; break;
            case "Other Master Degree in  Legal": position = 71; break;
            case "CA": position = 72; break;
            case "CFA (Chartered Financial Analyst)": position = 73; break;
            case "CS": position = 74; break;
            case "ICWA": position = 75; break;
            case "Other Degree in Finance": position = 76; break;
            case "IAS": position = 77; break;
            case "IES": position = 78; break;
            case "IFS": position = 79; break;
            case "IRS": position = 80; break;
            case "IPS": position = 81; break;
            case "Other Degree in Service": position = 82; break;
            case "Ph.D.": position = 83; break;
            case "Diploma": position = 84; break;
            case "Polytechnic": position = 85; break;
            case "Trade School": position = 86; break;
            case "Others in Diploma": position = 87; break;
            default: position = -1; break;
        }

        return position;
    }

    private boolean validateedu() {
        if (editText_edu.getText().toString().trim().isEmpty()) {
            inputLayoutEdu.setError("Enter education");
            requestFocus(editText_edu);
            return false;
        } else {
            inputLayoutCollege.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatework() {
        if(editText_work.getText().toString().trim().isEmpty()) {
            inputLayoutWorking.setError("Enter work");
            requestFocus(editText_work);
            return false;
        }

        else {
            inputLayoutWorking.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatedesig() {
        if(editText_desig.getText().toString().trim().isEmpty()) {
            inputLayoutDesig.setError("Enter designation");
            requestFocus(editText_desig);
            return false;
        } else {
            inputLayoutDesig.setErrorEnabled(false);
        }
        return true;
    }


    private boolean validateannual() {
        if (editText_annual.getText().toString().trim().isEmpty()) {
            inputLayoutAnnual.setError("Enter annual income");
            requestFocus(editText_annual);
            return false;
        } else {
            inputLayoutAnnual.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatecollege() {
        if (editText_college.getText().toString().trim().isEmpty()) {
            inputLayoutCollege.setError("Enter college");
            requestFocus(editText_college);
            return false;
        } else {
            inputLayoutCollege.setErrorEnabled(false);
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
                case R.id.editTextEdu:
                    validateedu();
                    break;


                case R.id.editTextCollege:
                    validatecollege();
                    break;

                case R.id.editTextWorking:
                    validatework();
                    break;

                case R.id.editTextDesignation:
                    validatedesig();
                    break;


                case R.id.editText_annual:
                    validateannual();
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
