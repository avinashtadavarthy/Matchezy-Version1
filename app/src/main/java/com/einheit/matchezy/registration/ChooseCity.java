package com.einheit.matchezy.registration;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.einheit.matchezy.R;
import com.einheit.matchezy.RawData;
import com.einheit.matchezy.Utility;

public class ChooseCity extends AppCompatActivity {

    private EditText filterText;
    private ListView itemList;
    private ArrayAdapter<String> listAdapter;
    private String category;

    String selectedcategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_city);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.7));


        filterText = (EditText)findViewById(R.id.editText);
        itemList = (ListView)findViewById(R.id.listView);

        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, RawData.getInstance().cities);

        itemList.setAdapter(listAdapter);

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                filterText.setText(listAdapter.getItem(position));

                Intent intent = new Intent();
                intent.putExtra("chosencity", listAdapter.getItem(position));
                setResult(Activity.RESULT_OK, intent);

                finish();

            }
        });

        filterText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                ChooseCity.this.listAdapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }




    //shared pref
    private void storeSPData(String key, String data) {

        SharedPreferences mUserData = this.getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor mUserEditor = mUserData.edit();
        mUserEditor.putString(key, data);
        mUserEditor.commit();

    }

}
