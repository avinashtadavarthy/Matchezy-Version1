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

import java.util.Calendar;

public class Registration extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText dateTextView;

    private EditText inputName, inputEmail, inputPassword,input_Dateofbirth,input_number;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword,inputLayoutNumber,inputLayoutDateofbirth;
    String facebookdata;
    public String name, email, gender, imgurl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        AndroidNetworking.initialize(getApplicationContext());

        inputLayoutName = (TextInputLayout) findViewById(R.id.inputLayoutName);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.inputLayoutEmail);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.inputLayoutPassword);
        inputLayoutDateofbirth= (TextInputLayout)findViewById(R.id.inputLayoutdateofbirth);
        inputLayoutNumber= (TextInputLayout)findViewById(R.id.inputLayoutNumber);

        inputName = (EditText) findViewById(R.id.editText_name);
        inputEmail = (EditText) findViewById(R.id.editText_email);
        inputPassword = (EditText) findViewById(R.id.edit_password);
        input_number=(EditText)findViewById(R.id.editNumber);
        input_Dateofbirth=(EditText)findViewById(R.id.editTextDateofBirth);


        facebookdata = getSPData("facebookdata");
        if(facebookdata != null) {
            try {
                JSONObject response = new JSONObject(facebookdata);
                name = response.optString("name");
                email = response.optString("email");
                gender = response.optString("gender");

                JSONObject picture = response.getJSONObject("picture");
                JSONObject data = picture.getJSONObject("data");
                imgurl = data.optString("url");

                storeSPData("facebookimageurl", imgurl);

                inputName.setText(name);
                inputEmail.setText(email);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }



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

                if(hasFocus) {
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
        String date = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        dateTextView.setText(date);

    }

    private void submitForm() {
        if (!validateName()) {
            return;
        }


        if(!validateDateOfBirth())
        {
            return;
        }

        if(!validateNumber()) {
            return;
        }


        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }


        String date = inputEmail.getText().toString().trim();


        /*AndroidNetworking.post(User.getInstance().BASE_URL+"register")
                .addBodyParameter("username",inputName.getText().toString().trim())
                .addBodyParameter("dob",input_Dateofbirth.getText().toString().trim())
                .addBodyParameter("phone_number",inputPassword.getText().toString().trim())
                .addBodyParameter("email",input_Dateofbirth.getText().toString().trim())
                .addBodyParameter("password",input_number.getText().toString().trim())
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
                });*/

          Intent intent = new Intent(getApplicationContext(),Registration2.class);
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
    if(input_number.getText().toString().trim().isEmpty()) {
        inputLayoutNumber.setError(getString(R.string.err_msg_number));
        requestFocus(input_number);
        return false;
    }


    else {
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


