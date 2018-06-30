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

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.androidnetworking.AndroidNetworking;
import com.einheit.matchezy.R;
import com.einheit.matchezy.User;
import com.scalified.fab.ActionButton;

import java.util.ArrayList;

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

                        editText_edu.setText(dataString);
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
        User.getInstance().populateModel(edu,User.getInstance().company_items);

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

                        editText_work.setText(dataString);
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
        User.getInstance().populateModel(edu,User.getInstance().college_items);

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

                        editText_college.setText(dataString);
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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Registration4.this);
        alertDialogBuilder.setTitle("Choose Designation");
        int position;
        switch (editText_desig.getText().toString()) {
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
