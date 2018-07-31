package com.einheit.matchezy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class StaticTextPage extends AppCompatActivity {

    TextView statictext, pageheader;
    ImageView exitpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_text_page);

        statictext = findViewById(R.id.statictext);
        pageheader = findViewById(R.id.pageheader);
        exitpage = findViewById(R.id.exitpage);

        String type = getIntent().getStringExtra("type");

        switch(type) {
            case "terms":
                pageheader.setText("Terms and Conditions");
                statictext.setText(Html.fromHtml(getString(R.string.sample_terms_conditions)));
                break;
            case "privacy":
                pageheader.setText("Privacy Policy");
                statictext.setText(Html.fromHtml(getString(R.string.sample_privacy_policy)));
                break;
            case "about":
                pageheader.setText("About Us");
                statictext.setText(getString(R.string.sample_about_us));
                break;
            case "help":
                pageheader.setText("Help and Support");
                statictext.setText(getString(R.string.sample_help_support));
                break;
            case "privsettings":
                pageheader.setText("Privacy Settings");
                statictext.setText("Have to include privacy settings after discussion with the team!");
                break;
        }


        exitpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
