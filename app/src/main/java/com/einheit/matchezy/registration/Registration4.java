package com.einheit.matchezy.registration;

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
import android.widget.Switch;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.androidnetworking.AndroidNetworking;
import com.einheit.matchezy.NumberTextWatcher;
import com.einheit.matchezy.R;
import com.einheit.matchezy.RawData;
import com.einheit.matchezy.Utility;
import com.scalified.fab.ActionButton;

import java.util.ArrayList;

public class Registration4 extends AppCompatActivity {


    EditText editText_work, editText_annual, editText_college, editText_edu, editText_desig;
    Switch switchEdu, switchCollege, switchWork, switchAnnual, switchDesig;
    private TextInputLayout inputLayoutWorking, inputLayoutCollege, inputLayoutAnnual, inputLayoutEdu, inputLayoutDesig;


    ArrayList<Integer> existingdataedu = new ArrayList<>(),
            existingdatawork = new ArrayList<>(),
            existingdatacollege = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration4);

        AndroidNetworking.initialize(this);


        inputLayoutAnnual = (TextInputLayout) findViewById(R.id.inputLayoutAnnualIncome);
        inputLayoutCollege = (TextInputLayout) findViewById(R.id.inputLayoutCollege);
        inputLayoutEdu = (TextInputLayout) findViewById(R.id.inputLayoutEdu);
        inputLayoutWorking = (TextInputLayout) findViewById(R.id.inputLayoutWorking);
        inputLayoutDesig = (TextInputLayout) findViewById(R.id.inputLayoutDesignation);

        editText_annual = (EditText) findViewById(R.id.editText_annual);
        editText_college = (EditText) findViewById(R.id.editTextCollege);
        editText_edu = (EditText) findViewById(R.id.editTextEdu);
        editText_work = (EditText) findViewById(R.id.editTextWorking);
        editText_desig = (EditText) findViewById(R.id.editTextDesignation);

        switchEdu = findViewById(R.id.switchButtonEdu);
        switchCollege = findViewById(R.id.switchButtonCollege);
        switchAnnual = findViewById(R.id.switchButtonAnnual);
        switchWork = findViewById(R.id.switchButtonWorking);
        switchDesig = findViewById(R.id.switchButtonDesignation);

        editText_edu.setShowSoftInputOnFocus(false);
        editText_desig.setShowSoftInputOnFocus(false);

        editText_edu.addTextChangedListener(new Registration4.MyTextWatcher(editText_edu));
        editText_college.addTextChangedListener(new Registration4.MyTextWatcher(editText_college));
        editText_work.addTextChangedListener(new Registration4.MyTextWatcher(editText_work));
        editText_annual.addTextChangedListener(new Registration4.MyTextWatcher(editText_annual));
        editText_desig.addTextChangedListener(new Registration4.MyTextWatcher(editText_desig));


        //add validation for designation and change up stuff, then make a user on server, fb error handling


        editText_annual.addTextChangedListener(new NumberTextWatcher(editText_annual));


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


        editText_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                company();
            }
        });

        editText_work.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if (hasFocus) {
                    company();
                }

            }
        });


        editText_college.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                college();
            }
        });

        editText_college.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if (hasFocus) {
                    college();
                }

            }
        });


        editText_desig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                designation();
            }
        });

        editText_desig.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if (hasFocus) {
                    designation();
                }

            }
        });


        final ActionButton actionButton = (ActionButton) findViewById(R.id.action_button_next3);
        actionButton.setType(ActionButton.Type.DEFAULT);
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

        if (!validatework()) {
            return;
        }

        /*if (!validatedesig()) {
            return;
        }*/

        /*if (!validateannual()) {
            return;
        }*/

        storeSPData("education", editText_edu.getText().toString().trim());
        storeSPData("college", editText_college.getText().toString().trim());
        storeSPData("work", editText_work.getText().toString().trim());
        if(!editText_desig.getText().toString().trim().isEmpty()
                && editText_desig.getText().toString().trim().length() > 0)
            storeSPData("desig", editText_desig.getText().toString().trim());
        storeSPData("desig", editText_desig.getText().toString().trim());
        if(!editText_annual.getText().toString().trim().isEmpty()
                && editText_annual.getText().toString().trim().length() > 0)
            storeSPData("annual_income", editText_annual.getText().toString().trim());

        storeSPData("educationVisibility", switchEdu.isChecked());
        storeSPData("collegeVisibility", switchCollege.isChecked());
        storeSPData("workVisibility", switchWork.isChecked());
        storeSPData("desigVisibility", switchDesig.isChecked());
        storeSPData("annualVisibility", switchAnnual.isChecked());


        Intent intent = new Intent(getApplicationContext(), Registration_Interests.class);
        startActivity(intent);

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

                        editText_edu.setText(datadisplayed);
                        existingdataedu = selectedIds;
                    }

                    @Override
                    public void onCancel() {
                        Log.d("multidialog", "Dialog cancelled");
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

                        editText_work.setText(datadisplayed);
                        existingdatawork = selectedIds;
                    }

                    @Override
                    public void onCancel() {
                        Log.d("multidialog", "Dialog cancelled");
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

                        editText_college.setText(datadisplayed);
                        existingdatacollege = selectedIds;
                    }

                    @Override
                    public void onCancel() {
                        Log.d("multidialog", "Dialog cancelled");
                    }

                });

        multiSelectDialog.show(getSupportFragmentManager(), "multiSelectDialog");
    }

    private void designation() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Registration4.this);
        alertDialogBuilder.setTitle("Choose Designation");

        alertDialogBuilder
                .setSingleChoiceItems(RawData.getInstance().designationItems,
                        RawData.getPositionDesignation(editText_desig.getText().toString()), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListView lw = ((AlertDialog) dialog).getListView();
                        Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                        String selectedgend = checkedItem.toString();
                        editText_desig.setText(selectedgend);
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

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
        if (editText_work.getText().toString().trim().isEmpty()) {
            inputLayoutWorking.setError("Enter work");
            requestFocus(editText_work);
            return false;
        } else {
            inputLayoutWorking.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatedesig() {
        if (editText_desig.getText().toString().trim().isEmpty()) {
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

                /*case R.id.editTextDesignation:
                    validatedesig();
                    break;*/


                /*case R.id.editText_annual:
                    validateannual();
                    break;*/

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

    private void storeSPData(String key, boolean data) {

        SharedPreferences mUserData = this.getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor mUserEditor = mUserData.edit();
        mUserEditor.putBoolean(key, data);
        mUserEditor.commit();

    }

    private String getSPData(String key) {

        SharedPreferences mUserData = this.getSharedPreferences("UserData", MODE_PRIVATE);
        String data = mUserData.getString(key, "");

        return data;

    }

}
