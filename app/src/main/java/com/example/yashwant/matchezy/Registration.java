package com.example.yashwant.matchezy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.scalified.fab.ActionButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Registration extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText dateTextView;

    private EditText inputName, inputEmail, inputPassword, input_Dateofbirth, input_number;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword, inputLayoutNumber, inputLayoutDateofbirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        AndroidNetworking.initialize(getApplicationContext());

        inputLayoutName = (TextInputLayout) findViewById(R.id.inputLayoutName);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.inputLayoutEmail);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.inputLayoutPassword);
        inputLayoutDateofbirth = (TextInputLayout) findViewById(R.id.inputLayoutdateofbirth);
        inputLayoutNumber = (TextInputLayout) findViewById(R.id.inputLayoutNumber);

        inputName = (EditText) findViewById(R.id.editText_name);
        inputEmail = (EditText) findViewById(R.id.editText_email);
        inputPassword = (EditText) findViewById(R.id.edit_password);
        input_number = (EditText) findViewById(R.id.editNumber);
        input_Dateofbirth = (EditText) findViewById(R.id.editTextDateofBirth);


        String name, email, pass, dob, ph;

        String facebookData = getSPData("facebookdata");
        try {
            JSONObject fbJsonObj = new JSONObject(facebookData);
            inputName.setText(fbJsonObj.optString("name"));
            inputEmail.setText(fbJsonObj.optString("email"));
            SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
            String d = fbJsonObj.optString("birthday");
            Date dt1 = format1.parse(d);
            DateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
            String finalDay = format2.format(dt1);
            input_Dateofbirth.setText(finalDay);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        name = inputName.getText().toString().trim();
        email = inputEmail.getText().toString().trim();
        pass = inputPassword.getText().toString().trim();
        dob = input_Dateofbirth.getText().toString().trim();
        ph = input_number.getText().toString().trim();


        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        input_Dateofbirth.addTextChangedListener(new MyTextWatcher(input_Dateofbirth));
        input_number.addTextChangedListener(new MyTextWatcher(input_number));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));
        final ActionButton actionButton = (ActionButton) findViewById(R.id.action_button_next1);
        // actionButton.hide();
        actionButton.setType(ActionButton.Type.BIG);
        //actionButton.setSize(65.0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            actionButton.setButtonColor(Color.parseColor("#EA5251"));
        }
        actionButton.setRippleEffectEnabled(true);
        actionButton.playShowAnimation();
        actionButton.setImageResource(R.drawable.ic_action_arrow);
        dateTextView = input_Dateofbirth;
        dateTextView.setInputType(InputType.TYPE_NULL);

        dateTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if (hasFocus) {
                    Calendar now = Calendar.getInstance();
                    DatePickerDialog dpd = DatePickerDialog.newInstance(
                            Registration.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                    dpd.show(getFragmentManager(), "Datepickerdialog");

                }

            }
        });

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        Registration.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");

            }
        });


        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submitForm();


            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
        dateTextView.setText(date);

    }

    private void submitForm() {
        if (!validateName()) {
            return;
        }


        if (!validateDateOfBirth()) {
            return;
        }

        if (!validateNumber()) {
            return;
        }


        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        String[] dd = input_Dateofbirth.getText().toString().split("/", 3);
        String dobstr = dd[2] + "/" + dd[1] + "/" + dd[0];

        storeSPData("username", inputName.getText().toString().trim());
        storeSPData("dob", dobstr);
        storeSPData("phone_number", inputPassword.getText().toString().trim());
        storeSPData("email", inputEmail.getText().toString().trim());
        storeSPData("password", input_number.getText().toString().trim());

        Log.e("date", input_Dateofbirth.getText().toString());
        Log.e("mdate", dobstr);

        Intent intent = new Intent(getApplicationContext(), Registration2.class);
        startActivity(intent);

    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateNumber() {
        if (input_number.getText().toString().trim().isEmpty()) {
            inputLayoutNumber.setError(getString(R.string.err_msg_number));
            requestFocus(input_number);
            return false;
        } else if (input_number.getText().toString().length() < 10) {
            inputLayoutNumber.setError("Number not valid");
            requestFocus(input_number);
            return false;
        } else {
            inputLayoutNumber.setErrorEnabled(false);
        }
        return true;
    }


    private boolean validateDateOfBirth() {
        if (input_Dateofbirth.getText().toString().trim().isEmpty()) {
            inputLayoutDateofbirth.setError(getString(R.string.err_msg_dateofbirth));
            requestFocus(input_Dateofbirth);
            return false;
        } else {
            inputLayoutDateofbirth.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else if (inputPassword.getText().toString().trim().length() < 8) {
            inputLayoutPassword.setError("Password must be above 8 characters");
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
                case R.id.editText_name:
                    validateName();
                    break;


                case R.id.editNumber:
                    validateNumber();
                    break;

                case R.id.editText_email:
                    validateEmail();
                    break;


                case R.id.edit_password:
                    validatePassword();
                    break;


                    /*case R.id.editTextDateofBirth:
                    validateDateOfBirth();
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

    private String getSPData(String key) {

        SharedPreferences mUserData = this.getSharedPreferences("UserData", MODE_PRIVATE);
        String data = mUserData.getString(key, "");

        return data;

    }


}


