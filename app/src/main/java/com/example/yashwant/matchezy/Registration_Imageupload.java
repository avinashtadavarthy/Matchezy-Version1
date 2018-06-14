package com.example.yashwant.matchezy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.scalified.fab.ActionButton;
import org.json.JSONObject;

public class Registration_Imageupload extends AppCompatActivity {

    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "Imageupload";
    public int i = 0;

    ImageView imageView1, imageView2, imageView3, imageView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageupload);

        AndroidNetworking.initialize(this);

        final ActionButton actionButton = (ActionButton) findViewById(R.id.action_button_next2);
        // actionButton.hide();
        actionButton.setType(ActionButton.Type.BIG);
        //actionButton.setSize(65.0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            actionButton.setButtonColor(Color.parseColor("#EA5251"));
        }
        actionButton.setRippleEffectEnabled(true);
        actionButton.playShowAnimation();
        actionButton.setImageResource(R.drawable.ic_action_arrow);

        Log.e("asdf", getSPData("username") + " - " + getSPData("dob") + " - " +
                getSPData("phone_number") + " - " + getSPData("email") + " - " + getSPData("password")
                + " - " + getSPData("gender") + " - " + getSPData("lookingfor") + " - " +
                getSPData("maritalstatus") + " - " + getSPData("city") + " - " + getSPData("lang") +
                " - " + getSPData("feet") + " - " + getSPData("inches") + " - " + getSPData("religion") +
                " - " + getSPData("tattoos") + " - " + getSPData("piercings") + " - " + getSPData("education") +
                " - " + getSPData("college") + " - " + getSPData("work") + " - " + getSPData("desig") + " - "
                + getSPData("annual_income"));

        if(getSPData("fb_id").equals("")) storeSPData("fb_id", "null");

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidNetworking.post(User.getInstance().BASE_URL + "register")
                        .addBodyParameter("username", getSPData("username"))
                        .addBodyParameter("dob", getSPData("dob"))
                        .addBodyParameter("phone_number", getSPData("phone_number"))
                        .addBodyParameter("email", getSPData("email"))
                        .addBodyParameter("password", getSPData("password"))
                        .addBodyParameter("gender", getSPData("gender"))
                        .addBodyParameter("looking_for", getSPData("lookingfor"))
                        .addBodyParameter("marital_status", getSPData("maritalstatus"))
                        .addBodyParameter("city", getSPData("city"))
                        .addBodyParameter("langs", getSPData("lang"))
                        .addBodyParameter("feet", getSPData("feet"))
                        .addBodyParameter("inches", getSPData("inches"))
                        .addBodyParameter("religion", getSPData("religion"))
                        .addBodyParameter("tattoos", getSPData("tattoos"))
                        .addBodyParameter("piercings", getSPData("piercings"))
                        .addBodyParameter("education", getSPData("education"))
                        .addBodyParameter("college", getSPData("college"))
                        .addBodyParameter("work", getSPData("work"))
                        .addBodyParameter("desig", getSPData("desig"))
                        .addBodyParameter("annual_income", getSPData("annual_income"))
                        .addBodyParameter("fb_id", getSPData("fb_id"))
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // do anything with response
                                Log.e("check", response.toString());

                                if(response.optString("message").equals("Registration successful")) {

                                    Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();

                                    Intent intent =  new Intent(Registration_Imageupload.this, OTP.class);
                                    startActivity(intent);

                                } else {

                                    Toast.makeText(getApplicationContext(), "User not registered successfully!", Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onError(ANError error) {
                                // handle error
                                error.printStackTrace();
                            }
                        });
            }
        });

        imageView1 = (ImageView) findViewById(R.id.imageview1);
        imageView2 = (ImageView) findViewById(R.id.imageview2);
        imageView3 = (ImageView) findViewById(R.id.imageview3);
        imageView4 = (ImageView) findViewById(R.id.imageview4);

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = 1;
                openImageChooser();
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = 2;
                openImageChooser();
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = 3;
                openImageChooser();
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = 4;
                openImageChooser();
            }
        });


    }

    /* Choose an image from Gallery */
    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                // Get the url from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // Get the path from the Uri
                    String path = getPathFromURI(selectedImageUri);
                    Log.i(TAG, "Image Path : " + path);
                    // Set the image in ImageView


                    if (i == 1)
                        ((ImageView) findViewById(R.id.imageview1)).setImageURI(selectedImageUri);

                    else if (i == 2)
                        ((ImageView) findViewById(R.id.imageview2)).setImageURI(selectedImageUri);

                    else if (i == 3)
                        ((ImageView) findViewById(R.id.imageview3)).setImageURI(selectedImageUri);

                    else if (i == 4)
                        ((ImageView) findViewById(R.id.imageview4)).setImageURI(selectedImageUri);
                }
            }
        }
    }

    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
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
