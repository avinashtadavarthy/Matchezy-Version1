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
import android.support.v7.view.ContextThemeWrapper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.einheit.matchezy.R;
import com.einheit.matchezy.Utility;
import com.scalified.fab.ActionButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Registration extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText dateTextView;

    private EditText inputName, inputEmail, inputPassword, input_Dateofbirth, input_number, input_countrycode;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword, inputLayoutNumber, inputLayoutDateofbirth, inputLayoutCountrycode;

    boolean isLoggedThroughFb = false;

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
        inputLayoutCountrycode = (TextInputLayout) findViewById(R.id.inputlayoutcountrycode);

        inputName = (EditText) findViewById(R.id.editText_name);
        inputEmail = (EditText) findViewById(R.id.editText_email);
        inputPassword = (EditText) findViewById(R.id.edit_password);
        input_number = (EditText) findViewById(R.id.editNumber);
        input_Dateofbirth = (EditText) findViewById(R.id.editTextDateofBirth);

        /* AndroidNetworking.post(Utility.getInstance().BASE_URL + "fbLogin")
                .addBodyParameter("fb_id", )
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {



                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        error.printStackTrace();
                    }
                });
*/

        input_countrycode = (EditText) findViewById(R.id.edit_countrycode);

        input_countrycode.setShowSoftInputOnFocus(false);

        input_countrycode.setText("+91");
        SharedPreferences mUserData = this.getSharedPreferences("UserData", MODE_PRIVATE);
        isLoggedThroughFb = mUserData.getBoolean("isLoggedInThroughFb", false);

        if(isLoggedThroughFb) {
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
                inputPassword.setText(fbJsonObj.optString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            inputLayoutPassword.setVisibility(View.INVISIBLE);
        }


        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        input_Dateofbirth.addTextChangedListener(new MyTextWatcher(input_Dateofbirth));
        input_number.addTextChangedListener(new MyTextWatcher(input_number));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));

        final ActionButton actionButton = (ActionButton) findViewById(R.id.action_button_next1);
        actionButton.setType(ActionButton.Type.DEFAULT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            actionButton.setButtonColor(Color.parseColor("#EA5251"));
        }
        actionButton.setRippleEffectEnabled(true);
        actionButton.playShowAnimation();
        actionButton.setImageResource(R.drawable.ic_action_arrow);


        dateTextView = input_Dateofbirth;
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
                    dpd.setVersion(DatePickerDialog.Version.VERSION_1);
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
                dpd.setVersion(DatePickerDialog.Version.VERSION_1);
                dpd.show(getFragmentManager(), "Datepickerdialog");

            }
        });



        input_countrycode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus) {
                    countrycode();
                }

            }
        });

        input_countrycode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countrycode();
            }
        });



        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submitForm();


            }
        });
    }

    private void countrycode() {

        final CharSequence[] items = { "Afghanistan, +93", "Albania, +355", "Algeria, +213", "American Samoa, +1684", "Andorra, +376", "Angola, +244",
                "Anguilla, +1264", "Antarctica, +672", "Antigua and Barbuda, +1268", "Argentina, +54", "Armenia, +374", "Aruba, +297",
                "Australia, +61", "Austria, +43", "Azerbaijan, +994", "Bahamas, +1242", "Bahrain, +973", "Bangladesh, +880",
                "Barbados, +1246", "Belarus, +375", "Belgium, +32", "Belize, +501", "Benin, +229", "Bermuda, +1441",
                "Bhutan, +975", "Bolivia, +591", "Bosnia and Herzegovina, +387", "Botswana, +267", "Brazil, +55", "British Indian Ocean Territory, +246",
                "British Virgin Islands, +1284", "Brunei, +673", "Bulgaria, +359", "Burkina Faso, +226", "Burundi, +257", "Cambodia, +855",
                "Cameroon, +237", "Canada, +1", "Cape Verde, +238", "Cayman Islands, +1345", "Central African Republic, +236", "Chad, +235",
                "Chile, +56", "China, +86", "Christmas Island, +61", "Cocos Islands, +61", "Colombia, +57", "Comoros, +269",
                "Cook Islands, +682", "Costa Rica, +506", "Croatia, +385", "Cuba, +53", "Curacao, +599", "Cyprus, +357",
                "Czech Republic, +420", "Democratic Republic of the Congo, +243", "Denmark, +45", "Djibouti, +253", "Dominica, +1767", "Dominican Republic, +1809",
                "Dominican Republic, +1829", "Dominican Republic, +1849", "East Timor, +670", "Ecuador, +593", "Egypt, +20", "El Salvador, +503",
                "Equatorial Guinea, +240", "Eritrea, +291", "Estonia, +372", "Ethiopia, +251", "Falkland Islands, +500", "Faroe Islands, +298",
                "Fiji, +679", "Finland, +358", "France, +33", "French Polynesia, +689", "Gabon, +241", "Gambia, +220",
                "Georgia, +995", "Germany, +49", "Ghana, +233", "Gibraltar, +350", "Greece, +30", "Greenland, +299",
                "Grenada, +1473", "Guam, +1671", "Guatemala, +502", "Guernsey, +441481", "Guinea, +224", "GuineaBissau, +245",
                "Guyana, +592", "Haiti, +509", "Honduras, +504", "Hong Kong, +852", "Hungary, +36", "Iceland, +354",
                "India, +91", "Indonesia, +62", "Iran, +98", "Iraq, +964", "Ireland, +353", "Isle of Man, +441624",
                "Israel, +972", "Italy, +39", "Ivory Coast, +225", "Jamaica, +1876", "Japan, +81", "Jersey, +441534",
                "Jordan, +962", "Kazakhstan, +7", "Kenya, +254", "Kiribati, +686", "Kosovo, +383", "Kuwait, +965",
                "Kyrgyzstan, +996", "Laos, +856", "Latvia, +371", "Lebanon, +961", "Lesotho, +266", "Liberia, +231",
                "Libya, +218", "Liechtenstein, +423", "Lithuania, +370", "Luxembourg, +352", "Macau, +853", "Macedonia, +389",
                "Madagascar, +261", "Malawi, +265", "Malaysia, +60", "Maldives, +960", "Mali, +223", "Malta, +356",
                "Marshall Islands, +692", "Mauritania, +222", "Mauritius, +230", "Mayotte, +262", "Mexico, +52", "Micronesia, +691",
                "Moldova, +373", "Monaco, +377", "Mongolia, +976", "Montenegro, +382", "Montserrat, +1664", "Morocco, +212",
                "Mozambique, +258", "Myanmar, +95", "Namibia, +264", "Nauru, +674", "Nepal, +977", "Netherlands, +31",
                "Netherlands Antilles, +599", "New Caledonia, +687", "New Zealand, +64", "Nicaragua, +505", "Niger, +227", "Nigeria, +234",
                "Niue, +683", "North Korea, +850", "Northern Mariana Islands, +1670", "Norway, +47", "Oman, +968", "Pakistan, +92",
                "Palau, +680", "Palestine, +970", "Panama, +507", "Papua New Guinea, +675", "Paraguay, +595", "Peru, +51",
                "Philippines, +63", "Pitcairn, +64", "Poland, +48", "Portugal, +351", "Puerto Rico, +1787", "Puerto Rico, +1939",
                "Qatar, +974", "Republic of the Congo, +242", "Reunion, +262", "Romania, +40", "Russia, +7", "Rwanda, +250",
                "Saint Barthelemy, +590", "Saint Helena, +290", "Saint Kitts and Nevis, +1869", "Saint Lucia, +1758", "Saint Martin, +590", "Saint Pierre and Miquelon, +508",
                "Saint Vincent and the Grenadines, +1784", "Samoa, +685", "San Marino, +378", "Sao Tome and Principe, +239", "Saudi Arabia, +966", "Senegal, +221",
                "Serbia, +381", "Seychelles, +248", "Sierra Leone, +232", "Singapore, +65", "Sint Maarten, +1721", "Slovakia, +421",
                "Slovenia, +386", "Solomon Islands, +677", "Somalia, +252", "South Africa, +27", "South Korea, +82", "South Sudan, +211",
                "Spain, +34", "Sri Lanka, +94", "Sudan, +249", "Suriname, +597", "Svalbard and Jan Mayen, +47", "Swaziland, +268",
                "Sweden, +46", "Switzerland, +41", "Syria, +963", "Taiwan, +886", "Tajikistan, +992", "Tanzania, +255",
                "Thailand, +66", "Togo, +228", "Tokelau, +690", "Tonga, +676", "Trinidad and Tobago, +1868", "Tunisia, +216",
                "Turkey, +90", "Turkmenistan, +993", "Turks and Caicos Islands, +1649", "Tuvalu, +688", "U.S. Virgin Islands, +1340", "Uganda, +256",
                "Ukraine, +380", "United Arab Emirates, +971", "United Kingdom, +44", "United States, +1", "Uruguay, +598", "Uzbekistan, +998",
                "Vanuatu, +678", "Vatican, +379", "Venezuela, +58", "Vietnam, +84", "Wallis and Futuna, +681", "Western Sahara, +212",
                "Yemen, +967", "Zambia, +260", "Zimbabwe, +263" };

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Registration.this, R.style.SSAlertDialogStyle);
        alertDialogBuilder.setTitle("Choose Country Code");
        alertDialogBuilder.setSingleChoiceItems(items, 96, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListView lw = ((AlertDialog) dialog).getListView();
                        Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                        String selectedgend = checkedItem.toString();
                        String code_to_display = converttocode(selectedgend);
                        input_countrycode.setText(code_to_display);
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

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


        AndroidNetworking.post(Utility.getInstance().BASE_URL + "checkExists")
                .addBodyParameter("phone_number", input_countrycode.getText().toString().trim() + input_number.getText().toString().trim())
                .addBodyParameter("email", inputEmail.getText().toString().trim())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("hjv", response.toString());

                        switch(response.optString("status_code")) {

                            case "200": {
                                String[] dd = input_Dateofbirth.getText().toString().split("/", 3);
                                String dobstr = dd[2] + "/" + dd[1] + "/" + dd[0];

                                storeSPData("username", inputName.getText().toString().trim());
                                storeSPData("dob", dobstr);
                                storeSPData("phone_number", input_countrycode.getText().toString().trim() + input_number.getText().toString().trim());
                                storeSPData("email", inputEmail.getText().toString().trim());
                                storeSPData("password", inputPassword.getText().toString().trim());

                                Log.e("date", input_countrycode.getText().toString().trim() + input_number.getText().toString().trim());
                                Log.e("mdate", dobstr);

                                Intent intent = new Intent(getApplicationContext(), Registration2.class);
                                startActivity(intent);
                            } break;

                            case "400":
                                Toast.makeText(Registration.this, response.optString("message"), Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        error.printStackTrace();
                    }
                });

    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {

            if (inputName.getText().toString().contains("@")) {
                inputLayoutName.setError("Not a valid name");
                return false;
            } else {
                inputLayoutName.setErrorEnabled(false);
            }
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

        if (input_Dateofbirth.getText().toString().trim().equals("")) {

            inputLayoutDateofbirth.setError(getString(R.string.err_msg_dateofbirth));
            requestFocus(input_Dateofbirth);
            return false;

        } else {

            String[] dobarr = input_Dateofbirth.getText().toString().split("/",3);
            String age = Utility.getInstance().getAge(Integer.parseInt(dobarr[2]), Integer.parseInt(dobarr[1]), Integer.parseInt(dobarr[0]));

            if (Integer.parseInt(age) < 18) {
                inputLayoutDateofbirth.setError("Get back after you're 18, please?");
                return false;
            } else {
                inputLayoutDateofbirth.setErrorEnabled(false);
            }

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

                case R.id.editTextDateofBirth:
                    validateDateOfBirth();
                    break;


            }
        }
    }

    private String converttocode(String s) {

        String rs = "+91";

        if(s.equals("Afghanistan, +93")) rs = "+93";
        else if(s.equals("Albania, +355")) rs = "+355";
        else if(s.equals("Algeria, +213")) rs = "+213";
        else if(s.equals("American Samoa, +1684")) rs = "+1684";
        else if(s.equals("Andorra, +376")) rs = "+376";
        else if(s.equals("Angola, +244")) rs = "+244";
        else if(s.equals("Anguilla, +1264")) rs = "+1264";
        else if(s.equals("Antarctica, +672")) rs = "+672";
        else if(s.equals("Antigua and Barbuda, +1268")) rs = "+1268";
        else if(s.equals("Argentina, +54")) rs = "+54";
        else if(s.equals("Armenia, +374")) rs = "+374";
        else if(s.equals("Aruba, +297")) rs = "+297";
        else if(s.equals("Australia, +61")) rs = "+61";
        else if(s.equals("Austria, +43")) rs = "+43";
        else if(s.equals("Azerbaijan, +994")) rs = "+994";
        else if(s.equals("Bahamas, +1242")) rs = "+1242";
        else if(s.equals("Bahrain, +973")) rs = "+973";
        else if(s.equals("Bangladesh, +880")) rs = "+880";
        else if(s.equals("Barbados, +1246")) rs = "+1246";
        else if(s.equals("Belarus, +375")) rs = "+375";
        else if(s.equals("Belgium, +32")) rs = "+32";
        else if(s.equals("Belize, +501")) rs = "+501";
        else if(s.equals("Benin, +229")) rs = "+229";
        else if(s.equals("Bermuda, +1441")) rs = "+1441";
        else if(s.equals("Bhutan, +975")) rs = "+975";
        else if(s.equals("Bolivia, +591")) rs = "+591";
        else if(s.equals("Bosnia and Herzegovina, +387")) rs = "+387";
        else if(s.equals("Botswana, +267")) rs = "+267";
        else if(s.equals("Brazil, +55")) rs = "+55";
        else if(s.equals("British Indian Ocean Territory, +246")) rs = "+246";
        else if(s.equals("British Virgin Islands, +1284")) rs = "+1284";
        else if(s.equals("Brunei, +673")) rs = "+673";
        else if(s.equals("Bulgaria, +359")) rs = "+359";
        else if(s.equals("Burkina Faso, +226")) rs = "+226";
        else if(s.equals("Burundi, +257")) rs = "+257";
        else if(s.equals("Cambodia, +855")) rs = "+855";
        else if(s.equals("Cameroon, +237")) rs = "+237";
        else if(s.equals("Canada, +1")) rs = "+1";
        else if(s.equals("Cape Verde, +238")) rs = "+238";
        else if(s.equals("Cayman Islands, +1345")) rs = "+1345";
        else if(s.equals("Central African Republic, +236")) rs = "+236";
        else if(s.equals("Chad, +235")) rs = "+235";
        else if(s.equals("Chile, +56")) rs = "+56";
        else if(s.equals("China, +86")) rs = "+86";
        else if(s.equals("Christmas Island, +61")) rs = "+61";
        else if(s.equals("Cocos Islands, +61")) rs = "+61";
        else if(s.equals("Colombia, +57")) rs = "+57";
        else if(s.equals("Comoros, +269")) rs = "+269";
        else if(s.equals("Cook Islands, +682")) rs = "+682";
        else if(s.equals("Costa Rica, +506")) rs = "+506";
        else if(s.equals("Croatia, +385")) rs = "+385";
        else if(s.equals("Cuba, +53")) rs = "+53";
        else if(s.equals("Curacao, +599")) rs = "+599";
        else if(s.equals("Cyprus, +357")) rs = "+357";
        else if(s.equals("Czech Republic, +420")) rs = "+420";
        else if(s.equals("Democratic Republic of the Congo, +243")) rs = "+243";
        else if(s.equals("Denmark, +45")) rs = "+45";
        else if(s.equals("Djibouti, +253")) rs = "+253";
        else if(s.equals("Dominica, +1767")) rs = "+1767";
        else if(s.equals("Dominican Republic, +1809")) rs = "+1809";
        else if(s.equals("Dominican Republic, +1829")) rs = "+1829";
        else if(s.equals("Dominican Republic, +1849")) rs = "+1849";
        else if(s.equals("East Timor, +670")) rs = "+670";
        else if(s.equals("Ecuador, +593")) rs = "+593";
        else if(s.equals("Egypt, +20")) rs = "+20";
        else if(s.equals("El Salvador, +503")) rs = "+503";
        else if(s.equals("Equatorial Guinea, +240")) rs = "+240";
        else if(s.equals("Eritrea, +291")) rs = "+291";
        else if(s.equals("Estonia, +372")) rs = "+372";
        else if(s.equals("Ethiopia, +251")) rs = "+251";
        else if(s.equals("Falkland Islands, +500")) rs = "+500";
        else if(s.equals("Faroe Islands, +298")) rs = "+298";
        else if(s.equals("Fiji, +679")) rs = "+679";
        else if(s.equals("Finland, +358")) rs = "+358";
        else if(s.equals("France, +33")) rs = "+33";
        else if(s.equals("French Polynesia, +689")) rs = "+689";
        else if(s.equals("Gabon, +241")) rs = "+241";
        else if(s.equals("Gambia, +220")) rs = "+220";
        else if(s.equals("Georgia, +995")) rs = "+995";
        else if(s.equals("Germany, +49")) rs = "+49";
        else if(s.equals("Ghana, +233")) rs = "+233";
        else if(s.equals("Gibraltar, +350")) rs = "+350";
        else if(s.equals("Greece, +30")) rs = "+30";
        else if(s.equals("Greenland, +299")) rs = "+299";
        else if(s.equals("Grenada, +1473")) rs = "+1473";
        else if(s.equals("Guam, +1671")) rs = "+1671";
        else if(s.equals("Guatemala, +502")) rs = "+502";
        else if(s.equals("Guernsey, +441481")) rs = "+441481";
        else if(s.equals("Guinea, +224")) rs = "+224";
        else if(s.equals("GuineaBissau, +245")) rs = "+245";
        else if(s.equals("Guyana, +592")) rs = "+592";
        else if(s.equals("Haiti, +509")) rs = "+509";
        else if(s.equals("Honduras, +504")) rs = "+504";
        else if(s.equals("Hong Kong, +852")) rs = "+852";
        else if(s.equals("Hungary, +36")) rs = "+36";
        else if(s.equals("Iceland, +354")) rs = "+354";
        else if(s.equals("India, +91")) rs = "+91";
        else if(s.equals("Indonesia, +62")) rs = "+62";
        else if(s.equals("Iran, +98")) rs = "+98";
        else if(s.equals("Iraq, +964")) rs = "+964";
        else if(s.equals("Ireland, +353")) rs = "+353";
        else if(s.equals("Isle of Man, +441624")) rs = "+441624";
        else if(s.equals("Israel, +972")) rs = "+972";
        else if(s.equals("Italy, +39")) rs = "+39";
        else if(s.equals("Ivory Coast, +225")) rs = "+225";
        else if(s.equals("Jamaica, +1876")) rs = "+1876";
        else if(s.equals("Japan, +81")) rs = "+81";
        else if(s.equals("Jersey, +441534")) rs = "+441534";
        else if(s.equals("Jordan, +962")) rs = "+962";
        else if(s.equals("Kazakhstan, +7")) rs = "+7";
        else if(s.equals("Kenya, +254")) rs = "+254";
        else if(s.equals("Kiribati, +686")) rs = "+686";
        else if(s.equals("Kosovo, +383")) rs = "+383";
        else if(s.equals("Kuwait, +965")) rs = "+965";
        else if(s.equals("Kyrgyzstan, +996")) rs = "+996";
        else if(s.equals("Laos, +856")) rs = "+856";
        else if(s.equals("Latvia, +371")) rs = "+371";
        else if(s.equals("Lebanon, +961")) rs = "+961";
        else if(s.equals("Lesotho, +266")) rs = "+266";
        else if(s.equals("Liberia, +231")) rs = "+231";
        else if(s.equals("Libya, +218")) rs = "+218";
        else if(s.equals("Liechtenstein, +423")) rs = "+423";
        else if(s.equals("Lithuania, +370")) rs = "+370";
        else if(s.equals("Luxembourg, +352")) rs = "+352";
        else if(s.equals("Macau, +853")) rs = "+853";
        else if(s.equals("Macedonia, +389")) rs = "+389";
        else if(s.equals("Madagascar, +261")) rs = "+261";
        else if(s.equals("Malawi, +265")) rs = "+265";
        else if(s.equals("Malaysia, +60")) rs = "+60";
        else if(s.equals("Maldives, +960")) rs = "+960";
        else if(s.equals("Mali, +223")) rs = "+223";
        else if(s.equals("Malta, +356")) rs = "+356";
        else if(s.equals("Marshall Islands, +692")) rs = "+692";
        else if(s.equals("Mauritania, +222")) rs = "+222";
        else if(s.equals("Mauritius, +230")) rs = "+230";
        else if(s.equals("Mayotte, +262")) rs = "+262";
        else if(s.equals("Mexico, +52")) rs = "+52";
        else if(s.equals("Micronesia, +691")) rs = "+691";
        else if(s.equals("Moldova, +373")) rs = "+373";
        else if(s.equals("Monaco, +377")) rs = "+377";
        else if(s.equals("Mongolia, +976")) rs = "+976";
        else if(s.equals("Montenegro, +382")) rs = "+382";
        else if(s.equals("Montserrat, +1664")) rs = "+1664";
        else if(s.equals("Morocco, +212")) rs = "+212";
        else if(s.equals("Mozambique, +258")) rs = "+258";
        else if(s.equals("Myanmar, +95")) rs = "+95";
        else if(s.equals("Namibia, +264")) rs = "+264";
        else if(s.equals("Nauru, +674")) rs = "+674";
        else if(s.equals("Nepal, +977")) rs = "+977";
        else if(s.equals("Netherlands, +31")) rs = "+31";
        else if(s.equals("Netherlands Antilles, +599")) rs = "+599";
        else if(s.equals("New Caledonia, +687")) rs = "+687";
        else if(s.equals("New Zealand, +64")) rs = "+64";
        else if(s.equals("Nicaragua, +505")) rs = "+505";
        else if(s.equals("Niger, +227")) rs = "+227";
        else if(s.equals("Nigeria, +234")) rs = "+234";
        else if(s.equals("Niue, +683")) rs = "+683";
        else if(s.equals("North Korea, +850")) rs = "+850";
        else if(s.equals("Northern Mariana Islands, +1670")) rs = "+1670";
        else if(s.equals("Norway, +47")) rs = "+47";
        else if(s.equals("Oman, +968")) rs = "+968";
        else if(s.equals("Pakistan, +92")) rs = "+92";
        else if(s.equals("Palau, +680")) rs = "+680";
        else if(s.equals("Palestine, +970")) rs = "+970";
        else if(s.equals("Panama, +507")) rs = "+507";
        else if(s.equals("Papua New Guinea, +675")) rs = "+675";
        else if(s.equals("Paraguay, +595")) rs = "+595";
        else if(s.equals("Peru, +51")) rs = "+51";
        else if(s.equals("Philippines, +63")) rs = "+63";
        else if(s.equals("Pitcairn, +64")) rs = "+64";
        else if(s.equals("Poland, +48")) rs = "+48";
        else if(s.equals("Portugal, +351")) rs = "+351";
        else if(s.equals("Puerto Rico, +1787")) rs = "+1787";
        else if(s.equals("Puerto Rico, +1939")) rs = "+1939";
        else if(s.equals("Qatar, +974")) rs = "+974";
        else if(s.equals("Republic of the Congo, +242")) rs = "+242";
        else if(s.equals("Reunion, +262")) rs = "+262";
        else if(s.equals("Romania, +40")) rs = "+40";
        else if(s.equals("Russia, +7")) rs = "+7";
        else if(s.equals("Rwanda, +250")) rs = "+250";
        else if(s.equals("Saint Barthelemy, +590")) rs = "+590";
        else if(s.equals("Saint Helena, +290")) rs = "+290";
        else if(s.equals("Saint Kitts and Nevis, +1869")) rs = "+1869";
        else if(s.equals("Saint Lucia, +1758")) rs = "+1758";
        else if(s.equals("Saint Martin, +590")) rs = "+590";
        else if(s.equals("Saint Pierre and Miquelon, +508")) rs = "+508";
        else if(s.equals("Saint Vincent and the Grenadines, +1784")) rs = "+1784";
        else if(s.equals("Samoa, +685")) rs = "+685";
        else if(s.equals("San Marino, +378")) rs = "+378";
        else if(s.equals("Sao Tome and Principe, +239")) rs = "+239";
        else if(s.equals("Saudi Arabia, +966")) rs = "+966";
        else if(s.equals("Senegal, +221")) rs = "+221";
        else if(s.equals("Serbia, +381")) rs = "+381";
        else if(s.equals("Seychelles, +248")) rs = "+248";
        else if(s.equals("Sierra Leone, +232")) rs = "+232";
        else if(s.equals("Singapore, +65")) rs = "+65";
        else if(s.equals("Sint Maarten, +1721")) rs = "+1721";
        else if(s.equals("Slovakia, +421")) rs = "+421";
        else if(s.equals("Slovenia, +386")) rs = "+386";
        else if(s.equals("Solomon Islands, +677")) rs = "+677";
        else if(s.equals("Somalia, +252")) rs = "+252";
        else if(s.equals("South Africa, +27")) rs = "+27";
        else if(s.equals("South Korea, +82")) rs = "+82";
        else if(s.equals("South Sudan, +211")) rs = "+211";
        else if(s.equals("Spain, +34")) rs = "+34";
        else if(s.equals("Sri Lanka, +94")) rs = "+94";
        else if(s.equals("Sudan, +249")) rs = "+249";
        else if(s.equals("Suriname, +597")) rs = "+597";
        else if(s.equals("Svalbard and Jan Mayen, +47")) rs = "+47";
        else if(s.equals("Swaziland, +268")) rs = "+268";
        else if(s.equals("Sweden, +46")) rs = "+46";
        else if(s.equals("Switzerland, +41")) rs = "+41";
        else if(s.equals("Syria, +963")) rs = "+963";
        else if(s.equals("Taiwan, +886")) rs = "+886";
        else if(s.equals("Tajikistan, +992")) rs = "+992";
        else if(s.equals("Tanzania, +255")) rs = "+255";
        else if(s.equals("Thailand, +66")) rs = "+66";
        else if(s.equals("Togo, +228")) rs = "+228";
        else if(s.equals("Tokelau, +690")) rs = "+690";
        else if(s.equals("Tonga, +676")) rs = "+676";
        else if(s.equals("Trinidad and Tobago, +1868")) rs = "+1868";
        else if(s.equals("Tunisia, +216")) rs = "+216";
        else if(s.equals("Turkey, +90")) rs = "+90";
        else if(s.equals("Turkmenistan, +993")) rs = "+993";
        else if(s.equals("Turks and Caicos Islands, +1649")) rs = "+1649";
        else if(s.equals("Tuvalu, +688")) rs = "+688";
        else if(s.equals("U.S. Virgin Islands, +1340")) rs = "+1340";
        else if(s.equals("Uganda, +256")) rs = "+256";
        else if(s.equals("Ukraine, +380")) rs = "+380";
        else if(s.equals("United Arab Emirates, +971")) rs = "+971";
        else if(s.equals("United Kingdom, +44")) rs = "+44";
        else if(s.equals("United States, +1")) rs = "+1";
        else if(s.equals("Uruguay, +598")) rs = "+598";
        else if(s.equals("Uzbekistan, +998")) rs = "+998";
        else if(s.equals("Vanuatu, +678")) rs = "+678";
        else if(s.equals("Vatican, +379")) rs = "+379";
        else if(s.equals("Venezuela, +58")) rs = "+58";
        else if(s.equals("Vietnam, +84")) rs = "+84";
        else if(s.equals("Wallis and Futuna, +681")) rs = "+681";
        else if(s.equals("Western Sahara, +212")) rs = "+212";
        else if(s.equals("Yemen, +967")) rs = "+967";
        else if(s.equals("Zambia, +260")) rs = "+260";
        else if(s.equals("Zimbabwe, +263")) rs = "+263";

        return rs;

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


