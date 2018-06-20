package com.example.yashwant.matchezy;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.scalified.fab.ActionButton;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.OkHttpClient;

public class Registration_Imageupload extends AppCompatActivity {

    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "Imageupload";
    public int i = 0;

    ImageView imageView1, imageView2, imageView3, imageView4;
    String[] paths = {"","","",""};
    String fb_id = "";
    ArrayList<String> interestsArray;
    String interestsArrayString;
    List<File> files = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageupload);


        final OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(1200, TimeUnit.SECONDS)
                .readTimeout(1200, TimeUnit.SECONDS)
                . writeTimeout(1200, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.initialize(this, okHttpClient);



        Intent intent = getIntent();
        interestsArray = intent.getStringArrayListExtra("interestsArray");

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

        if(!checkPermissionForReadExtertalStorage()) {
            try {
                requestPermissionForReadExtertalStorage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        SharedPreferences mUserData = this.getSharedPreferences("UserData", MODE_PRIVATE);
        final boolean isLoggedThroughFb = mUserData.getBoolean("isLoggedInThroughFb", false);

        Log.e("qwe", interestsArray.toString());
        interestsArrayString = interestsArray.toString();

        if(isLoggedThroughFb)
            fb_id = getSPData("fb_id");

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        Log.e("asdf", getSPData("username") + " - " + getSPData("dob") + " - " +
                        getSPData("phone_number") + " - " + getSPData("email") + " - " + getSPData("password")
                        + " - " + getSPData("gender") + " - " + getSPData("lookingfor") + " - " +
                        getSPData("maritalstatus") + " - " + getSPData("city") + " - " + getSPData("lang") +
                        " - " + getSPData("feet") + " - " + getSPData("inches") + " - " + getSPData("religion") +
                        " - " + getSPData("tattoos") + " - " + getSPData("piercings") + " - " + getSPData("education") +
                        " - " + getSPData("college") + " - " + getSPData("work") + " - " + getSPData("desig") + " - "
                                        + getSPData("annual_income") + " - " + interestsArrayString + " - " +
                        new File(paths[0]).getAbsoluteFile() + " - " + new File(paths[1]).exists() + " - " + new File(paths[2]).exists() + " - " +
                        new File(paths[3]).exists()) ;
                if((paths[0].isEmpty() || paths[1].isEmpty() || paths[2].isEmpty() || paths[3].isEmpty())) {
                    Toast.makeText(getApplicationContext(), "Select four images for your profile",
                            Toast.LENGTH_SHORT).show();
                }
                else {

                    files.add(new File(paths[0]));
                    files.add(new File(paths[1]));
                    files.add(new File(paths[2]));
                    files.add(new File(paths[3]));

                    AndroidNetworking.upload(User.getInstance().BASE_URL + "register")
                            .addMultipartFile("profile_pic", files.get(0))
                            .addMultipartFile("pictures", files.get(1))
                            .addMultipartFile("pictures_2", files.get(2))
                            .addMultipartFile("pictures_3", files.get(3))
                            .addMultipartParameter("username", getSPData("username"))
                            .addMultipartParameter("dob", getSPData("dob"))
                            .addMultipartParameter("phone_number", getSPData("phone_number"))
                            .addMultipartParameter("email", getSPData("email"))
                            .addMultipartParameter("password", getSPData("password"))
                            .addMultipartParameter("gender", getSPData("gender"))
                            .addMultipartParameter("looking_for", getSPData("lookingfor"))
                            .addMultipartParameter("marital_status", getSPData("maritalstatus"))
                            .addMultipartParameter("city", getSPData("city"))
                            .addMultipartParameter("langs", getSPData("lang"))
                            .addMultipartParameter("feet", getSPData("feet"))
                            .addMultipartParameter("inches", getSPData("inches"))
                            .addMultipartParameter("religion", getSPData("religion"))
                            .addMultipartParameter("tattoos", getSPData("tattoos"))
                            .addMultipartParameter("piercings", getSPData("piercings"))
                            .addMultipartParameter("education", getSPData("education"))
                            .addMultipartParameter("college", getSPData("college"))
                            .addMultipartParameter("work", getSPData("work"))
                            .addMultipartParameter("desig", getSPData("desig"))
                            .addMultipartParameter("annual_income", getSPData("annual_income"))
                            .addMultipartParameter("fb_id", fb_id)
                            .addMultipartParameter("interests", interestsArrayString)
                            .setPriority(Priority.HIGH)
                            .setOkHttpClient(okHttpClient)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(final JSONObject response) {
                                    // do anything with response
                                    Log.e("check", response.toString());

                                    if (response.optString("status_code").equals("200")) {

                                        FirebaseMessaging.getInstance().
                                                subscribeToTopic(response.optJSONObject("message").optString("user_id"));

                                        new android.os.Handler().postDelayed(
                                                new Runnable() {
                                                    public void run() {
                                                        AndroidNetworking.post(User.getInstance().BASE_URL + "approveUser")
                                                                .addBodyParameter("user_id", response.optJSONObject("message").optString("user_id"))
                                                                .setPriority(Priority.HIGH)
                                                                .build()
                                                                .getAsJSONObject(new JSONObjectRequestListener() {
                                                                    @Override
                                                                    public void onResponse(JSONObject response) {
                                                                        // do anything with response
                                                                        Log.e("check", response.toString());
                                                                    }

                                                                    @Override
                                                                    public void onError(ANError error) {
                                                                        // handle error
                                                                        error.printStackTrace();
                                                                    }
                                                                });
                                                    }
                                                },
                                                3000);


                                        Toast.makeText(getApplicationContext(),
                                                response.optJSONObject("message").optString("message"), Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(Registration_Imageupload.this, OTP.class);
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
                                }
                            });
                }
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

                        Log.i(TAG, "Image Path : " + getPath(Registration_Imageupload.this, selectedImageUri));

                    // Set the image in ImageView

                    paths[i - 1] = getPath(Registration_Imageupload.this, selectedImageUri);


                    if (i == 1) {
                        ((ImageView) findViewById(R.id.imageview1)).setImageURI(selectedImageUri);
                    }

                    else if (i == 2) {
                        ((ImageView) findViewById(R.id.imageview2)).setImageURI(selectedImageUri);
                    }

                    else if (i == 3) {
                        ((ImageView) findViewById(R.id.imageview3)).setImageURI(selectedImageUri);
                    }

                    else if (i == 4) {
                        ((ImageView) findViewById(R.id.imageview4)).setImageURI(selectedImageUri);
                    }
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

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = getApplicationContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void requestPermissionForReadExtertalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions(Registration_Imageupload.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1001);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
