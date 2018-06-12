package com.example.yashwant.matchezy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.scalified.fab.ActionButton;

public class Registration4 extends AppCompatActivity {


    EditText editText_work,editText_annual,editText_college,editText_edu;
    private TextInputLayout inputLayoutWorking, inputLayoutCollege, inputLayoutAnnual,inputLayoutEdu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration4);

        
        inputLayoutAnnual=(TextInputLayout)findViewById(R.id.inputLayoutAnnualIncome);
        inputLayoutCollege=(TextInputLayout)findViewById(R.id.inputLayoutCollege);
        inputLayoutEdu=(TextInputLayout)findViewById(R.id.inputLayoutEdu);
        inputLayoutWorking=(TextInputLayout)findViewById(R.id.inputLayoutWorking);

        editText_annual=(EditText)findViewById(R.id.editText_annual);
        editText_college=(EditText)findViewById(R.id.editTextCollege);
        editText_edu=(EditText)findViewById(R.id.editTextEdu);
        editText_work=(EditText)findViewById(R.id.editTextWorking);

        editText_edu.addTextChangedListener(new Registration4.MyTextWatcher(editText_edu));
        editText_college.addTextChangedListener(new Registration4.MyTextWatcher(editText_college));
        editText_work.addTextChangedListener(new Registration4.MyTextWatcher(editText_work));
        editText_annual.addTextChangedListener(new Registration4.MyTextWatcher(editText_annual));


        final ActionButton actionButton = (ActionButton) findViewById(R.id.action_button_next3);
        // actionButton.hide();
        actionButton.setType(ActionButton.Type.BIG);
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


        if(!validatework())
        {
            return;
        }

        if(!validateannual()) {
            return;
        }


        if (!validatecollege()) {
            return;
        }


        Toast.makeText(this, "registration done!", Toast.LENGTH_SHORT).show();
/*
        String date = inputEmail.getText().toString().trim();


        AndroidNetworking.post(User.getInstance().BASE_URL+"register")
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
        Intent intent = new Intent(getApplicationContext(),Registration_Interests.class);
        startActivity(intent);

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
                    validatework();
                    break;

                case R.id.editTextWorking:
                    validateannual();
                    break;


                case R.id.editText_annual:
                    validatecollege();
                    break;








            }
        }
    }

}
