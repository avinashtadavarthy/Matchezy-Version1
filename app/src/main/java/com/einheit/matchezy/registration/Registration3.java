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

import com.einheit.matchezy.R;
import com.scalified.fab.ActionButton;
import com.webianks.library.scroll_choice.ScrollChoice;

import java.util.ArrayList;
import java.util.List;

public class Registration3 extends AppCompatActivity {

    EditText editText_tattoos,editText_piercing,editText_religion;
    private TextInputLayout inputLayouttattoos, inputLayoutpiercing, inputLayoutbody,inputLayouteye;
    int feet = 5, inches = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration3);


        inputLayouttattoos = (TextInputLayout) findViewById(R.id.inputLayouttattoos);
        inputLayoutpiercing = (TextInputLayout) findViewById(R.id.inputLayoutpiercing);
        inputLayoutbody = (TextInputLayout) findViewById(R.id.inputLayoutreligion);


        editText_religion = (EditText) findViewById(R.id.editText_religion);
        editText_piercing = (EditText) findViewById(R.id.editText_piercing);
        editText_tattoos = (EditText) findViewById(R.id.editText_tattoos);
        editText_religion.setShowSoftInputOnFocus(false);
        editText_piercing.setShowSoftInputOnFocus(false);
        editText_tattoos.setShowSoftInputOnFocus(false);


        editText_tattoos.addTextChangedListener(new Registration3.MyTextWatcher(editText_tattoos));
        editText_piercing.addTextChangedListener(new Registration3.MyTextWatcher(editText_piercing));
        editText_religion.addTextChangedListener(new Registration3.MyTextWatcher(editText_religion));

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


        scrollChoice.setOnItemSelectedListener(new ScrollChoice.OnItemSelectedListener() {
            @Override
            public void onItemSelected(ScrollChoice scrollChoice, int position, String name) {
                Log.e("webi", name);
                String[] m = name.split("\'");

                feet = Integer.parseInt(m[0]);
                if(m.length == 1) {
                    inches = 0;
                } else {
                    inches = Integer.parseInt(m[1].substring(0,m[1].length()-1));
                }

                Log.e("feet", feet + "");
                Log.e("inches", inches +"");
            }
        }); //fa2948


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


        editText_religion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                religion();
            }
        });

        editText_religion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if (hasFocus) {
                    religion();
                }

            }
        });


    }
    


    private void religion()

    {
        final CharSequence[] items = {"Atheist", "Agnostic", "Spiritual", "Buddhist", "Christian", "Hindu", "Muslim", "Jewish", "Parsi",
                "Sikh", "Jain", "Inter-Religion", "Other"};

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Registration3.this);
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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Registration3.this);
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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Registration3.this);
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
    
    

    private void submitForm() {
        if(!validatereligion()) {
            return;
        }

        if (!validatetattoos()) {
            return;
        }


        if(!validatepiercing()) {
            return;
        }

        storeSPData("feet", feet + "");
        storeSPData("inches", inches + "");
        storeSPData("religion", editText_religion.getText().toString().trim());
        storeSPData("tattoos", editText_tattoos.getText().toString().trim());
        storeSPData("piercings", editText_piercing.getText().toString().trim());


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
        } else {
            inputLayoutpiercing.setErrorEnabled(false);
        }
        return true;
    }


    private boolean validatereligion() {
        if (editText_religion.getText().toString().trim().isEmpty()) {
            inputLayoutbody.setError("Enter religion status");
            requestFocus(editText_religion);
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

                case R.id.editText_religion:
                    validatereligion();
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
