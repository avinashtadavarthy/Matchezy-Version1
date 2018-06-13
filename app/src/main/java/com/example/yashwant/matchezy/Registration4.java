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


        AndroidNetworking.post(User.getInstance().BASE_URL+"register")
                .addBodyParameter("username", getSPData("username"))
                .addBodyParameter("dob", getSPData("dob"))
                .addBodyParameter("phone_number", getSPData("phone_number"))
                .addBodyParameter("email", getSPData("email"))
                .addBodyParameter("password", getSPData("password"))
                .addBodyParameter("gender", getSPData("gender"))
                .addBodyParameter("lookingfor", getSPData("lookingfor"))
                .addBodyParameter("marital_status", getSPData("maritalstatus"))
                .addBodyParameter("city", getSPData("city"))
                .addBodyParameter("langs", getSPData("lang"))
                .addBodyParameter("feet", getSPData("feet"))
                .addBodyParameter("inches", getSPData("inches"))
                .addBodyParameter("religion", getSPData("religion"))
                .addBodyParameter("tattoos", getSPData("tattoos"))
                .addBodyParameter("piercings", getSPData("piercings"))
                .addBodyParameter("education", getSPData("education"))
                .addBodyParameter("college", getSPData("college"))
                .addBodyParameter("work", getSPData("work"))
                .addBodyParameter("desig", getSPData("desig"))
                .addBodyParameter("annual_income", getSPData("annual_income"))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        Log.e("check",response.toString());
                        Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        error.printStackTrace();
                    }
                });


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

        if(editText_edu.getText().toString().equals("Aeronautical Engineering")) {
            position = 0;
        } else if(editText_edu.getText().toString().equals("B.Arch")) {
            position = 1;
        } else if(editText_edu.getText().toString().equals("BCA")) {
            position = 2;
        } else if(editText_edu.getText().toString().equals("BE")) {
            position = 3;
        } else if(editText_edu.getText().toString().equals("B.Plan")) {
            position = 4;
        } else if(editText_edu.getText().toString().equals("B.Sc IT/ Computer Science")) {
            position = 5;
        } else if(editText_edu.getText().toString().equals("B.Tech.")) {
            position = 6;
        } else if(editText_edu.getText().toString().equals("Other Bachelor Degree in Engineering / Computers")) {
            position = 7;
        } else if(editText_edu.getText().toString().equals("B.S.(Engineering)")) {
            position = 8;
        } else if(editText_edu.getText().toString().equals("M.Arch.")) {
            position = 9;
        } else if(editText_edu.getText().toString().equals("MCA")) {
            position = 10;
        } else if(editText_edu.getText().toString().equals("ME")) {
            position = 11;
        } else if(editText_edu.getText().toString().equals("M.Sc. IT / Computer Science")) {
            position = 12;
        } else if(editText_edu.getText().toString().equals("M.S.(Engg.)")) {
            position = 13;
        } else if(editText_edu.getText().toString().equals("M.Tech.")) {
            position = 14;
        } else if(editText_edu.getText().toString().equals("PGDCA")) {
            position = 15;
        } else if(editText_edu.getText().toString().equals("Other Masters Degree in Engineering / Computers")) {
            position = 16;
        } else if(editText_edu.getText().toString().equals("Aviation Degree")) {
            position = 17;
        } else if(editText_edu.getText().toString().equals("B.A.")) {
            position = 18;
        } else if(editText_edu.getText().toString().equals("B.Com.")) {
            position = 19;
        } else if(editText_edu.getText().toString().equals("B.Ed.")) {
            position = 20;
        } else if(editText_edu.getText().toString().equals("BFA")) {
            position = 21;
        } else if(editText_edu.getText().toString().equals("BFT")) {
            position = 22;
        } else if(editText_edu.getText().toString().equals("BLIS")) {
            position = 23;
        } else if(editText_edu.getText().toString().equals("B.M.M.")) {
            position = 24;
        } else if(editText_edu.getText().toString().equals("B.Sc.")) {
            position = 25;
        } else if(editText_edu.getText().toString().equals("B.S.W")) {
            position = 26;
        } else if(editText_edu.getText().toString().equals("B.Phil.")) {
            position = 27;
        } else if(editText_edu.getText().toString().equals("Other Bachelor Degree in Arts / Science / Commerce")) {
            position = 28;
        } else if(editText_edu.getText().toString().equals("M.A.")) {
            position = 29;
        } else if(editText_edu.getText().toString().equals("MCom")) {
            position = 30;
        } else if(editText_edu.getText().toString().equals("M.Ed.")) {
            position = 31;
        } else if(editText_edu.getText().toString().equals("MFA")) {
            position = 32;
        } else if(editText_edu.getText().toString().equals("MLIS")) {
            position = 33;
        } else if(editText_edu.getText().toString().equals("M.Sc.")) {
            position = 34;
        } else if(editText_edu.getText().toString().equals("MSW")) {
            position = 35;
        } else if(editText_edu.getText().toString().equals("M.Phil.")) {
            position = 36;
        } else if(editText_edu.getText().toString().equals("Other Master Degree in Arts / Science / Commerce")) {
            position = 37;
        } else if(editText_edu.getText().toString().equals("BBA")) {
            position = 38;
        } else if(editText_edu.getText().toString().equals("BFM (Financial Management)")) {
            position = 39;
        } else if(editText_edu.getText().toString().equals("BHM (Hotel Management)")) {
            position = 40;
        } else if(editText_edu.getText().toString().equals("Other Bachelor Degree in Management")) {
            position = 41;
        } else if(editText_edu.getText().toString().equals("MBA")) {
            position = 42;
        } else if(editText_edu.getText().toString().equals("MFM (Financial Management)")) {
            position = 43;
        } else if(editText_edu.getText().toString().equals("MHM  (Hotel Management)")) {
            position = 44;
        } else if(editText_edu.getText().toString().equals("MHRM (Human Resource Management)")) {
            position = 45;
        } else if(editText_edu.getText().toString().equals("PGDM")) {
            position = 46;
        } else if(editText_edu.getText().toString().equals("Other Master Degree in Management")) {
            position = 47;
        } else if(editText_edu.getText().toString().equals("B.A.M.S.")) {
            position = 48;
        } else if(editText_edu.getText().toString().equals("BDS")) {
            position = 49;
        } else if(editText_edu.getText().toString().equals("BHMS")) {
            position = 50;
        } else if(editText_edu.getText().toString().equals("BSMS")) {
            position = 51;
        } else if(editText_edu.getText().toString().equals("BPharm")) {
            position = 52;
        } else if(editText_edu.getText().toString().equals("BPT")) {
            position = 53;
        } else if(editText_edu.getText().toString().equals("BUMS")) {
            position = 54;
        } else if(editText_edu.getText().toString().equals("BVSc")) {
            position = 55;
        } else if(editText_edu.getText().toString().equals("MBBS")) {
            position = 56;
        } else if(editText_edu.getText().toString().equals("B.Sc. Nursing")) {
            position = 57;
        } else if(editText_edu.getText().toString().equals("Other Bachelor Degree in Medicine")) {
            position = 58;
        } else if(editText_edu.getText().toString().equals("MDS")) {
            position = 59;
        } else if(editText_edu.getText().toString().equals("MD / MS (Medical)")) {
            position = 60;
        } else if(editText_edu.getText().toString().equals("M.Pharm")) {
            position = 61;
        } else if(editText_edu.getText().toString().equals("MPT")) {
            position = 62;
        } else if(editText_edu.getText().toString().equals("MVSc")) {
            position = 63;
        } else if(editText_edu.getText().toString().equals("Other Master Degree in Medicine")) {
            position = 64;
        } else if(editText_edu.getText().toString().equals("BGL")) {
            position = 65;
        } else if(editText_edu.getText().toString().equals("B.L.")) {
            position = 66;
        } else if(editText_edu.getText().toString().equals("LL.B.")) {
            position = 67;
        } else if(editText_edu.getText().toString().equals("Other Bachelor Degree in Legal")) {
            position = 68;
        } else if(editText_edu.getText().toString().equals("LL.M.")) {
            position = 69;
        } else if(editText_edu.getText().toString().equals("M.L.")) {
            position = 70;
        } else if(editText_edu.getText().toString().equals("Other Master Degree in  Legal")) {
            position = 71;
        } else if(editText_edu.getText().toString().equals("CA")) {
            position = 72;
        } else if(editText_edu.getText().toString().equals("CFA (Chartered Financial Analyst)")) {
            position = 73;
        } else if(editText_edu.getText().toString().equals("CS")) {
            position = 74;
        } else if(editText_edu.getText().toString().equals("ICWA")) {
            position = 75;
        } else if(editText_edu.getText().toString().equals("Other Degree in Finance")) {
            position = 76;
        } else if(editText_edu.getText().toString().equals("IAS")) {
            position = 77;
        } else if(editText_edu.getText().toString().equals("IES")) {
            position = 78;
        } else if(editText_edu.getText().toString().equals("IFS")) {
            position = 79;
        } else if(editText_edu.getText().toString().equals("IRS")) {
            position = 80;
        } else if(editText_edu.getText().toString().equals("IPS")) {
            position = 81;
        } else if(editText_edu.getText().toString().equals("Other Degree in Service")) {
            position = 82;
        } else if(editText_edu.getText().toString().equals("Ph.D.")) {
            position = 83;
        } else if(editText_edu.getText().toString().equals("Diploma")) {
            position = 84;
        } else if(editText_edu.getText().toString().equals("Polytechnic")) {
            position = 85;
        } else if(editText_edu.getText().toString().equals("Trade School")) {
            position = 86;
        } else if(editText_edu.getText().toString().equals("Others in Diploma" )) {
            position = 87;
        } else {
            position = -1;
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
