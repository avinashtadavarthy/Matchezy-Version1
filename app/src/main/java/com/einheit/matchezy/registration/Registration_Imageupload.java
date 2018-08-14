package com.einheit.matchezy.registration;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.einheit.matchezy.R;
import com.einheit.matchezy.Utility;
import com.einheit.matchezy.messagestab.Chat;
import com.einheit.matchezy.messagestab.ChatImage;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;
import com.scalified.fab.ActionButton;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class Registration_Imageupload extends AppCompatActivity {

    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "Imageupload";
    public int i = 0;

    ImageView imageView1, imageView2, imageView3, imageView4;
    String fb_id = "";
    ArrayList<String> interestsArray;
    String interestsArrayString;
    List<File> files;
    Map<Integer, File> map;
    private ProgressDialog dialog;

    JsonObject requestObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageupload);

        imageView1 = findViewById(R.id.imageview1);
        imageView2 = findViewById(R.id.imageview2);
        imageView3 = findViewById(R.id.imageview3);
        imageView4 = findViewById(R.id.imageview4);

        final OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(1200, TimeUnit.SECONDS)
                .readTimeout(1200, TimeUnit.SECONDS)
                .writeTimeout(1200, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.initialize(this, okHttpClient);


        dialog = new ProgressDialog(Registration_Imageupload.this);

        files = new ArrayList<>();
        map = new HashMap<Integer, File>();

        Intent intent = getIntent();
        interestsArray = intent.getStringArrayListExtra("interestsArray");

        final ActionButton actionButton = (ActionButton) findViewById(com.einheit.matchezy.R.id.action_button_next2);
        actionButton.setType(ActionButton.Type.DEFAULT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            actionButton.setButtonColor(Color.parseColor("#EA5251"));
        }
        actionButton.setRippleEffectEnabled(true);
        actionButton.playShowAnimation();
        actionButton.setImageResource(com.einheit.matchezy.R.drawable.ic_action_arrow);

        checkPermissions();

        SharedPreferences mUserData = this.getSharedPreferences("UserData", MODE_PRIVATE);
        final boolean isLoggedThroughFb = mUserData.getBoolean("isLoggedInThroughFb", false);

        Log.e("qwe", interestsArray.toString());
        interestsArrayString = interestsArray.toString();

        if (isLoggedThroughFb)
            fb_id = getSPData("fb_id");

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (map.get(0) == null) {
                    Toast.makeText(getApplicationContext(), "Please select your profile picture",
                            Toast.LENGTH_SHORT).show();
                } else if (files.size() < 2) {
                    Toast.makeText(getApplicationContext(), "Select atleast two pictures",
                            Toast.LENGTH_SHORT).show();
                } else {

                    Map<String, File> filesMap = new HashMap<>();
                    List<String> fileNames = new ArrayList<>();
                    fileNames.add("profile_pic");
                    fileNames.add("picture_1");
                    fileNames.add("picture_2");
                    fileNames.add("picture_3");

                    for (int i = 0; i < 4; i++) {
                        if(map.get(i) != null)
                            filesMap.put(fileNames.get(i), map.get(i));
                    }


                    dialog.setMessage("Loading, please wait.");
                    dialog.setCancelable(false);
                    dialog.show();

                    requestObject = new JsonObject();
                    if (!getSPData("username").equals(""))
                        requestObject.addProperty("username", getSPData("username"));
                    if (!getSPData("dob").equals(""))
                        requestObject.addProperty("dob", getSPData("dob"));
                    if (!getSPData("phone_number").equals(""))
                        requestObject.addProperty("phone_number", getSPData("phone_number"));
                    if (!getSPData("email").equals(""))
                        requestObject.addProperty("email", getSPData("email"));
                    if (!getSPData("password").equals(""))
                        requestObject.addProperty("password", getSPData("password"));
                    if (!getSPData("gender").equals(""))
                        requestObject.addProperty("gender", getSPData("gender"));
                    if (!getSPData("lookingfor").equals(""))
                        requestObject.addProperty("looking_for", getSPData("lookingfor"));
                    if (!getSPData("maritalstatus").equals(""))
                        requestObject.addProperty("marital_status", getSPData("maritalstatus"));
                    if (!getSPData("city").equals(""))
                        requestObject.addProperty("city", getSPData("city"));
                    if (!getSPData("lang").equals(""))
                        requestObject.addProperty("langs", getSPData("lang"));
                    if (!getSPData("feet").equals(""))
                        requestObject.addProperty("feet", getSPData("feet"));
                    if (!getSPData("inches").equals(""))
                        requestObject.addProperty("inches", getSPData("inches"));
                    if (!getSPData("religion").equals(""))
                        requestObject.addProperty("religion", getSPData("religion"));
                    if (!getSPData("tattoos").equals(""))
                        requestObject.addProperty("tattoos", getSPData("tattoos"));
                    if (!getSPData("piercings").equals(""))
                        requestObject.addProperty("piercings", getSPData("piercings"));
                    if (!getSPData("education").equals(""))
                        requestObject.addProperty("education", getSPData("education"));
                    if (!getSPData("college").equals(""))
                        requestObject.addProperty("college", getSPData("college"));
                    if (!getSPData("work").equals(""))
                        requestObject.addProperty("work", getSPData("work"));
                    if (!getSPData("desig").equals(""))
                        requestObject.addProperty("desig", getSPData("desig"));
                    if (!getSPData("annual_income").equals(""))
                        requestObject.addProperty("annual_income", getSPData("annual_income").replace(",", ""));
                    requestObject.addProperty("fb_id", fb_id);
                    requestObject.addProperty("interests", interestsArrayString);
                    requestObject.addProperty("educationVisibility", getSPBoolean("educationVisibility"));
                    requestObject.addProperty("collegeVisibility", getSPBoolean("collegeVisibility"));
                    requestObject.addProperty("workVisibility", getSPBoolean("workVisibility"));
                    requestObject.addProperty("desigVisibility", getSPBoolean("desigVisibility"));
                    requestObject.addProperty("annualVisibility", getSPBoolean("annualVisibility"));

                    AndroidNetworking.upload(Utility.getInstance().BASE_URL + "register")
                            .addMultipartFile(filesMap)
                            .addMultipartParameter(requestObject)
                            .setPriority(Priority.HIGH)
                            .setOkHttpClient(okHttpClient)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(final JSONObject response) {
                                    // do anything with response
                                    Log.e("check", response.toString());

                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }

                                    if (response.optString("status_code").equals("200")) {

                                        FirebaseMessaging.getInstance().
                                                subscribeToTopic(response.optJSONObject("message").optString("user_id"));

                                        storeSPData("user_id", response.optJSONObject("message").optString("user_id"));

                                        Toast.makeText(getApplicationContext(),
                                                response.optJSONObject("message").optString("message"), Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(Registration_Imageupload.this, OTP.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);

                                    } else {

                                        Toast.makeText(getApplicationContext(),
                                                response.optString("message"), Toast.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onError(ANError error) {
                                    // handle error
                                    error.printStackTrace();

                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                }
                            });
                }
            }
        });

        imageView1 = (ImageView) findViewById(com.einheit.matchezy.R.id.imageview1);
        imageView2 = (ImageView) findViewById(com.einheit.matchezy.R.id.imageview2);
        imageView3 = (ImageView) findViewById(com.einheit.matchezy.R.id.imageview3);
        imageView4 = (ImageView) findViewById(com.einheit.matchezy.R.id.imageview4);

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermissions()) {
                    i = 1;
                    openImageChooser();
                }
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermissions()) {
                    i = 2;
                    openImageChooser();
                }
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermissions()) {
                    i = 3;
                    openImageChooser();
                }
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermissions()) {
                    i = 4;
                    openImageChooser();
                }
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

                    Bitmap bm = null;

                    try {
                        bm = MediaStore.Images.Media.getBitmap(Registration_Imageupload.this.getContentResolver(), selectedImageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    FileOutputStream fileOutputStream = null;

                    String imageFileName = "JPEG_" + System.currentTimeMillis() + ".jpg";
                    final File storageDir = new File(getCacheDir()
                            + "/MatchEzy");

                    boolean success = true;
                    if (!storageDir.exists()) {
                        success = storageDir.mkdirs();
                    }

                    try {
                        bm = Utility.modifyOrientation(bm, Utility.getPath(this, selectedImageUri));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        fileOutputStream = new FileOutputStream(storageDir + "/" + imageFileName);

                        bm.compress(Bitmap.CompressFormat.JPEG, 75, fileOutputStream);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.flush();
                                fileOutputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    map.put(i - 1, new File(storageDir + "/" + imageFileName));
                    if (files.size() < 4)
                        files.add(new File(storageDir + "/" + imageFileName));
                    else files.set(i - 1, new File(storageDir + "/" + imageFileName));

                    Log.e("asd", map.toString());
                    Log.e("ASD", files.toString());
                    Log.e("ASd", String.valueOf(files.size()));

                    if (i == 1) {
                        Glide.with(Registration_Imageupload.this)
                                .load(map.get(i - 1))
                                .into(imageView1);
                    } else if (i == 2) {
                        Glide.with(Registration_Imageupload.this)
                                .load(map.get(i - 1))
                                .into(imageView2);
                    } else if (i == 3) {
                        Glide.with(Registration_Imageupload.this)
                                .load(map.get(i - 1))
                                .into(imageView3);
                    } else if (i == 4) {
                        Glide.with(Registration_Imageupload.this)
                                .load(map.get(i - 1))
                                .into(imageView4);
                    }

                    bm.recycle();
                }
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

    private Boolean getSPBoolean(String key) {

        SharedPreferences mUserData = this.getSharedPreferences("UserData", MODE_PRIVATE);
        Boolean data = mUserData.getBoolean(key, true);

        return data;

    }

    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = getApplicationContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public boolean checkPermissionForWriteExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }


    public void requestPermissionForReadExtertalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions(Registration_Imageupload.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1001);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1001: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(Registration_Imageupload.this, "Accessing gallery requires this permission", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    boolean checkPermissions() {
        if (!checkPermissionForReadExtertalStorage() && !checkPermissionForWriteExtertalStorage()) {
            try {
                requestPermissionForReadExtertalStorage();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        } else return true;
    }

}
