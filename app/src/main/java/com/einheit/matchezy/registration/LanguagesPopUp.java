package com.einheit.matchezy.registration;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;


import com.einheit.matchezy.R;
import com.einheit.matchezy.Utility;

public class LanguagesPopUp extends AppCompatActivity {

    CheckBox Assamese, Bengali, Bhojpuri, Bodo, Chhattisgarhi, English, Gujarati, Haryanvi, Hindi, Kannada, Konkani, Maithali, Malayalam, Marathi, Odia, Punjabi, Rajasthani, Sindhi, Tamil, Telugu, Tulu, Urdu;

    int lang[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

    String languageslist[] = {"Assamese", "Bengali", "Bhojpuri", "Bodo", "Chhattisgarhi", "English", "Gujarati", "Haryanvi", "Hindi", "Kannada", "Konkani", "Maithali", "Malayalam", "Marathi", "Odia", "Punjabi", "Rajasthani", "Sindhi", "Tamil", "Telugu", "Tulu", "Urdu"};

    Button langbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_languages_pop_up);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.9));


        CheckBox Assamese = (CheckBox) findViewById(R.id.Assamese);
        CheckBox Bengali = (CheckBox) findViewById(R.id.Bengali);
        CheckBox Bhojpuri = (CheckBox) findViewById(R.id.Bhojpuri);
        CheckBox Bodo = (CheckBox) findViewById(R.id.Bodo);
        CheckBox Chhattisgarhi = (CheckBox) findViewById(R.id.Chhattisgarhi);
        CheckBox English = (CheckBox) findViewById(R.id.English);
        CheckBox Gujarati = (CheckBox) findViewById(R.id.Gujarati);
        CheckBox Haryanvi = (CheckBox) findViewById(R.id.Haryanvi);
        CheckBox Hindi = (CheckBox) findViewById(R.id.Hindi);
        CheckBox Kannada = (CheckBox) findViewById(R.id.Kannada);
        CheckBox Konkani = (CheckBox) findViewById(R.id.Konkani);
        CheckBox Maithali = (CheckBox) findViewById(R.id.Maithali);
        CheckBox Malayalam = (CheckBox) findViewById(R.id.Malayalam);
        CheckBox Marathi = (CheckBox) findViewById(R.id.Marathi);
        CheckBox Odia = (CheckBox) findViewById(R.id.Odia);
        CheckBox Punjabi = (CheckBox) findViewById(R.id.Punjabi);
        CheckBox Rajasthani = (CheckBox) findViewById(R.id.Rajasthani);
        CheckBox Sindhi = (CheckBox) findViewById(R.id.Sindhi);
        CheckBox Tamil = (CheckBox) findViewById(R.id.Tamil);
        CheckBox Telugu = (CheckBox) findViewById(R.id.Telugu);
        CheckBox Tulu = (CheckBox) findViewById(R.id.Tulu);
        CheckBox Urdu = (CheckBox) findViewById(R.id.Urdu);


        String languagesspoken = getIntent().getExtras().getString("languagesspoken");

        if(!languagesspoken.equals("")) {
            String[] temp_langs = Utility.getInstance().languagesspokendirty.split(", ");

           for(int i=0; i<=temp_langs.length-1; i++) {
               for(int j=0; j<lang.length-1; j++) {
                   if(temp_langs[i].equals(languageslist[j])) lang[j] = 1;
               }
           }

        }



        if(lang[0] == 1) Assamese.setChecked(true);
        if(lang[1] == 1) Bengali.setChecked(true);
        if(lang[2] == 1) Bhojpuri.setChecked(true);
        if(lang[3] == 1) Bodo.setChecked(true);
        if(lang[4] == 1) Chhattisgarhi.setChecked(true);
        if(lang[5] == 1) English.setChecked(true);
        if(lang[6] == 1) Gujarati.setChecked(true);
        if(lang[7] == 1) Haryanvi.setChecked(true);
        if(lang[8] == 1) Hindi.setChecked(true);
        if(lang[9] == 1) Kannada.setChecked(true);
        if(lang[10] == 1) Konkani.setChecked(true);
        if(lang[11] == 1) Maithali.setChecked(true);
        if(lang[12] == 1) Malayalam.setChecked(true);
        if(lang[13] == 1) Marathi.setChecked(true);
        if(lang[14] == 1) Odia.setChecked(true);
        if(lang[15] == 1) Punjabi.setChecked(true);
        if(lang[16] == 1) Rajasthani.setChecked(true);
        if(lang[17] == 1) Sindhi.setChecked(true);
        if(lang[18] == 1) Tamil.setChecked(true);
        if(lang[19] == 1) Telugu.setChecked(true);
        if(lang[20] == 1) Tulu.setChecked(true);
        if(lang[21] == 1) Urdu.setChecked(true);






        Assamese.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) lang[0] = 1;
                else lang[0] = 0;
            }
        });

        Bengali.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) lang[1] = 1;
                else lang[1] = 0;
            }
        });

        Bhojpuri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) lang[2] = 1;
                else lang[2] = 0;
            }
        });

        Bodo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) lang[3] = 1;
                else lang[3] = 0;
            }
        });

        Chhattisgarhi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) lang[4] = 1;
                else lang[4] = 0;
            }
        });

        English.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) lang[5] = 1;
                else lang[5] = 0;
            }
        });

        Gujarati.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) lang[6] = 1;
                else lang[6] = 0;
            }
        });

        Haryanvi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) lang[7] = 1;
                else lang[7] = 0;
            }
        });

        Hindi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) lang[8] = 1;
                else lang[8] = 0;
            }
        });

        Kannada.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) lang[9] = 1;
                else lang[9] = 0;
            }
        });

        Konkani.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) lang[10] = 1;
                else lang[10] = 0;
            }
        });

        Maithali.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) lang[11] = 1;
                else lang[11] = 0;
            }
        });

        Malayalam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) lang[12] = 1;
                else lang[12] = 0;
            }
        });

        Marathi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) lang[13] = 1;
                else lang[13] = 0;
            }
        });

        Odia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) lang[14] = 1;
                else lang[14] = 0;
            }
        });

        Punjabi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) lang[15] = 1;
                else lang[15] = 0;
            }
        });

        Rajasthani.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) lang[16] = 1;
                else lang[16] = 0;
            }
        });

        Sindhi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) lang[17] = 1;
                else lang[17] = 0;
            }
        });

        Tamil.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) lang[18] = 1;
                else lang[18] = 0;
            }
        });

        Telugu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) lang[19] = 1;
                else lang[19] = 0;
            }
        });

        Tulu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) lang[20] = 1;
                else lang[20] = 0;
            }
        });

        Urdu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) lang[21] = 1;
                else lang[21] = 0;
            }
        });




        langbutton = (Button) findViewById(R.id.langbutton);

        langbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringBuilder sb = new StringBuilder();

                for (int i=0; i <= lang.length-1; i++) {

                    if(lang[i]!=0) sb.append(languageslist[i]).append(", ");

                }

                String hello;

                if(sb.toString() != null && sb.toString().length() > 1) {
                    hello = (sb.toString()).substring(0, (sb.toString()).length() - 2);
                } else {
                    hello = "";
                }

                Utility.getInstance().languagesspoken = hello;
                Utility.getInstance().languagesspokendirty = sb.toString();

                storeSPData("languagesspoken_dirty", sb.toString());
                storeSPData("languagesspoken_neat", hello);

                Intent intent = new Intent();
                intent.putExtra("languagesspoken", hello);
                intent.putExtra("languagesspokendirty", sb.toString());
                setResult(Activity.RESULT_OK, intent);

                finish();

            }
        });

    }




    //Shared Preferences
    private void storeSPData(String key, String data) {

        SharedPreferences mUserData = this.getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor mUserEditor = mUserData.edit();
        mUserEditor.putString(key, data);
        mUserEditor.commit();

    }

}
