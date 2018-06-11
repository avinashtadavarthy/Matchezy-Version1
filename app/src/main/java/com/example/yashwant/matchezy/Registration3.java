package com.example.yashwant.matchezy;

import android.content.DialogInterface;
import android.content.Intent;
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

import com.scalified.fab.ActionButton;
import com.webianks.library.scroll_choice.ScrollChoice;

import java.util.ArrayList;
import java.util.List;

public class Registration3 extends AppCompatActivity {

    EditText editText_tattoos,editText_piercing,editText_eye,editText_body;
    private TextInputLayout inputLayouttattoos, inputLayoutpiercing, inputLayoutbody,inputLayouteye;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration3);


        inputLayouttattoos = (TextInputLayout) findViewById(R.id.inputLayouttattoos);
        inputLayoutpiercing = (TextInputLayout) findViewById(R.id.inputLayoutpiercing);
        inputLayouteye = (TextInputLayout) findViewById(R.id.inputLayouteye);
        inputLayoutbody = (TextInputLayout) findViewById(R.id.inputLayoutbody);


        editText_body = (EditText) findViewById(R.id.editText_body);
        editText_eye = (EditText) findViewById(R.id.editText_eye);
        editText_piercing = (EditText) findViewById(R.id.editText_piercing);
        editText_tattoos = (EditText) findViewById(R.id.editText_tattoos);


        editText_tattoos.addTextChangedListener(new Registration3.MyTextWatcher(editText_tattoos));
        editText_piercing.addTextChangedListener(new Registration3.MyTextWatcher(editText_piercing));
        editText_eye.addTextChangedListener(new Registration3.MyTextWatcher(editText_eye));
        editText_body.addTextChangedListener(new Registration3.MyTextWatcher(editText_body));

        ScrollChoice scrollChoice = (ScrollChoice) findViewById(R.id.scroll_choice);

        List<String> data = new ArrayList<>();

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


        scrollChoice.addItems(data, 14);


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


        scrollChoice.setOnItemSelectedListener(new ScrollChoice.OnItemSelectedListener() {
            @Override
            public void onItemSelected(ScrollChoice scrollChoice, int position, String name) {
                Log.d("webi", name);
            }
        });


        editText_piercing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                piercing();
            }
        });




        editText_piercing.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if (hasFocus) {
                    piercing();
                }

            }
        });


        editText_tattoos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tattoos();
            }
        });


        editText_tattoos.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if (hasFocus) {
                    tattoos();
                }

            }
        });

        editText_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                eye();

            }
        });

        editText_eye.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if (hasFocus) {
                    eye();
                }

            }
        });


        editText_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                body();
            }
        });

        editText_body.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if (hasFocus) {
                    body();
                }

            }
        });


    }
    


    private void body()

    {
        final CharSequence[] items = { "Thin","Athletic","Normal"};

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Registration3.this);
        alertDialogBuilder.setTitle("Choose Body type");
        int position;
        if (editText_body.getText().toString().equals("Thin")){
            position = 0;
        } else if (editText_body.getText().toString().equals("Athletic")){
            position = 1;
        } else if (editText_body.getText().toString().equals("Normal")){
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
                        editText_body.setText(selectedgend);
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void tattoos()

    {
        final CharSequence[] items = { "Yes","No","Planning to get"};

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Registration3.this);
        alertDialogBuilder.setTitle("Tattoos?");
        int position;
        if (editText_body.getText().toString().equals("Yes")){
            position = 0;
        } else if (editText_body.getText().toString().equals("No")){
            position = 1;
        } else if (editText_body.getText().toString().equals("Planning to get")){
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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Registration3.this);
        alertDialogBuilder.setTitle("Piercing?");
        int position;
        if (editText_body.getText().toString().equals("Yes")){
            position = 0;
        } else if (editText_body.getText().toString().equals("No")){
            position = 1;
        } else if (editText_body.getText().toString().equals("Planning to get")){
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
                        editText_piercing.setText(selectedgend);
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void eye()

    {
        final CharSequence[] items = { "Black","Brown","Blue","Green","Other"};

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Registration3.this);
        alertDialogBuilder.setTitle("Choose");
        int position;
        if (editText_body.getText().toString().equals("Black")){
            position = 0;
        } else if (editText_body.getText().toString().equals("Brown")){
            position = 1;
        } else if (editText_body.getText().toString().equals("Blue")){
            position = 2;
        } else if (editText_body.getText().toString().equals("Green")){
            position = 3;
        }else if (editText_body.getText().toString().equals("Other")){
            position = 4;
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
                        editText_eye.setText(selectedgend);
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    
    
    

    private void submitForm() {
        if (!validatetattoos()) {
            return;
        }


        if(!validatepiercing())
        {
            return;
        }

        if(!validatebody()) {
            return;
        }


        if (!validateeye()) {
            return;
        }

     

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
        Intent intent = new Intent(getApplicationContext(),Registration4.class);
        startActivity(intent);

    }

    private boolean validatetattoos() {
        if (editText_tattoos.getText().toString().trim().isEmpty()) {
            inputLayouttattoos.setError("Enter tattoos");
            requestFocus(editText_tattoos);
            return false;
        } else {
            inputLayouttattoos.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatepiercing() {
        if(editText_piercing.getText().toString().trim().isEmpty()) {
            inputLayoutpiercing.setError("Enter piercing");
            requestFocus(editText_piercing);
            return false;
        }


        else {
            inputLayoutpiercing.setErrorEnabled(false);
        }
        return true;
    }


    private boolean validatebody() {
        if (editText_body.getText().toString().trim().isEmpty()) {
            inputLayoutbody.setError("Enter body status");
            requestFocus(editText_body);
            return false;
        } else {
            inputLayoutbody.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateeye() {
        if (editText_eye.getText().toString().trim().isEmpty()) {
            inputLayouteye.setError("Enter eye");
            requestFocus(editText_eye);
            return false;
        } else {
            inputLayoutbody.setErrorEnabled(false);
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
                case R.id.editText_tattoos:
                    validatetattoos();
                    break;


                case R.id.editText_piercing:
                    validatepiercing();
                    break;

                case R.id.editText_eye:
                    validatebody();
                    break;


                case R.id.editText_body:
                    validateeye();
                    break;






            }
        }
    }
}
