package com.einheit.matchezy;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class StaticTextPage extends AppCompatActivity {

    TextView statictext, pageheader;
    ImageView exitpage;
    ListView lisencelist;

    public String[] lisence_text = {
            "Licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License. You may obtain a copy of the License at\n\n\t\thttp://www.apache.org/licenses/LICENSE-2.0\n\nUnless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.",
            "Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the \"Software\"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:\n\nThe above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.\n\nTHE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.",
            "Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:\n\n\t\t1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.\n\t\t2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.\n\nTHIS SOFTWARE IS PROVIDED BY GOOGLE, INC. ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GOOGLE, INC. OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\n\nThe views and conclusions contained in the software and documentation are those of the authors and should not be interpreted as representing official policies, either expressed or implied, of Google, Inc.",
            "You are hereby granted a non-exclusive, worldwide, royalty-free license to use, copy, modify, and distribute this software in source code or binary form for use in connection with the web services and APIs provided by Facebook.\n\nAs with any software that integrates with the Facebook platform, your use of this software is subject to the Facebook Developer Principles and Policies [http://developers.facebook.com/policy/]. This copyright notice shall be included in all copies or substantial portions of the software.\n\nTHE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE."
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_text_page);

        statictext = findViewById(R.id.statictext);
        pageheader = findViewById(R.id.pageheader);
        exitpage = findViewById(R.id.exitpage);
        lisencelist = findViewById(R.id.lisencelist);

        String type = getIntent().getStringExtra("type");

        switch(type) {
            case "terms":
                pageheader.setText("Terms and Conditions");
                statictext.setText(Html.fromHtml(getString(R.string.terms_and_conditions)));
                break;
            case "privacy":
                pageheader.setText("Privacy Policy");
                statictext.setText(Html.fromHtml(getString(R.string.privacy_policy)));
                break;
            case "about":
                pageheader.setText("About Us");
                statictext.setText(getString(R.string.about_us));
                break;
            case "help":
                pageheader.setText("Help and Support");
                statictext.setText(Html.fromHtml(getString(R.string.help_support)));
                break;
            case "opensource":
                pageheader.setText("Open Source Licenses");
                ArrayList<DataModel> licenselist = new ArrayList<>();

                licenselist.add(new DataModel("Material Range Bar", "Copyright 2015  AppyVet, Inc.\n\n", "apache"));
                licenselist.add(new DataModel("Scroll Choice", "MIT License\n\nCopyright (c) 2017 Ramankit Singh\n\n", "mit"));
                licenselist.add(new DataModel("Fab", "Copyright 2016 Scalified <http://www.scalified.com>\n\n", "apache"));
                licenselist.add(new DataModel("Material Date Time Picker", "Copyright (c) 2015 Wouter Dullaert\n\n", "apache"));
                licenselist.add(new DataModel("Glide", "Copyright 2014 Google, Inc. All rights reserved.\n\n", "google"));
                licenselist.add(new DataModel("Android Multi Select Dialog", "Copyright 2017 Abubakker Moallim\n\n", "apache"));
                licenselist.add(new DataModel("Circle ImageView", "Copyright 2014 - 2018 Henning Dodenhof\n\n", "apache"));
                licenselist.add(new DataModel("Android Sliding Up Panel", "Copyright 2014 SoThree, Inc.\n\n", "apache"));
                licenselist.add(new DataModel("Circle Indicator", "Copyright (C) 2014 relex\n\n", "apache"));
                licenselist.add(new DataModel("Rounded ImageView", "Copyright 2017 Vincent Mi\n\n", "apache"));
                licenselist.add(new DataModel("Fast Android Networking", "Copyright (C) 2016 Amit Shekhar\nCopyright (C) 2011 Android Open Source Project\n\n", "apache"));
                licenselist.add(new DataModel("Facebook SDK", "Copyright (c) 2014-present, Facebook, Inc. All rights reserved.\n\n", "facebook"));

                LicenseAdapter mAdapter = new LicenseAdapter(this,licenselist);
                lisencelist.setAdapter(mAdapter);
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



    //listview for lisence

    public class DataModel {

        private String mName;
        private String mRelease;
        private String mType;

        // Constructor that is used to create an instance of the DataModel object
        public DataModel(String mName, String mRelease, String mType) {
            this.mName = mName;
            this.mRelease = mRelease;
            this.mType = mType;
        }

        public String getmName() {
            return mName;
        }

        public String getmRelease() {
            return mRelease;
        }

        public String getmType() {
            return mType;
        }
    }

    public class LicenseAdapter extends ArrayAdapter<DataModel> {

        private Context mContext;
        private List<DataModel> DataModelsList = new ArrayList<>();

        public LicenseAdapter(@NonNull Context context, ArrayList<DataModel> list) {
            super(context, 0 , list);
            mContext = context;
            DataModelsList = list;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View listItem = convertView;
            if(listItem == null)
                listItem = LayoutInflater.from(mContext).inflate(R.layout.lisenceitem, parent, false);

            DataModel currentDataModel = DataModelsList.get(position);

            TextView name = (TextView) listItem.findViewById(R.id.name);
            name.setText(currentDataModel.getmName());

            TextView release = (TextView) listItem.findViewById(R.id.copyright);
            release.setText(currentDataModel.getmRelease());

            TextView textView = (TextView) listItem.findViewById(R.id.license_text);
            switch (currentDataModel.getmType()) {
                case "apache": textView.setText(lisence_text[0]); break;
                case "mit": textView.setText(lisence_text[1]); break;
                case "google": textView.setText(lisence_text[2]); break;
                case "facebook": textView.setText(lisence_text[3]); break;
            }

            return listItem;
        }
    }

}
