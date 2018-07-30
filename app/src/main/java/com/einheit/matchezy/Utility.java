package com.einheit.matchezy;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.DrawableMarginSpan;
import android.text.style.ImageSpan;
import android.text.style.TextAppearanceSpan;
import android.widget.TextView;

import com.abdeveloper.library.MultiSelectModel;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by avinash on 27/12/17.
 */

public class Utility {

    private static Utility mInstance= null;

    public static final int FROM_PROFILE_PAGE = 1;
    public static final int FROM_HOMESCREEN = 2;
    public static final int FROM_BOOKMARKED = 3;
    public static final int FROM_LIKED = 4;
    public static final int FROM_MATCHED = 5;
    public static final int FROM_DISLIKED = 6;
    public static final int FROM_BLOCKED = 7;
    public static final int FROM_SHARED_PROFILE = 8;

    public static final String PASS = "QAZwsxEDCrfvTGByhnUJMik";

    //variables or functions
    public static String
            languagesspoken = "", languagesspokendirty = "";/**/


    public String getAge(int year,int month,int day)
    {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year,month,day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if(today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR))
            age--;

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }


    public String getDate(String date)
    {
        String year = date.substring(0,4);
        String day = date.substring(8,10);
        String month = "";
        switch(date.substring(5,7))
        {
            case "01": month = "JAN"; break;
            case "02": month = "FEB"; break;
            case "03": month = "MAR"; break;
            case "04": month = "APR"; break;
            case "05": month = "MAY"; break;
            case "06": month = "JUN"; break;
            case "07": month = "JUL"; break;
            case "08": month = "AUG"; break;
            case "09": month = "SEP"; break;
            case "10": month = "OCT"; break;
            case "11": month = "NOV"; break;
            case "12": month = "DEC"; break;
            default:   month = "HELLO"; break;

        }

        String dateFinal = day + " " + month +  " " + year;

        return dateFinal;
    }


    public String getFormattedDate(String date) {
        return date.substring(0,10);
    }


    ///url///
    public String BASE_URL = "http://ec2-18-218-131-186.us-east-2.compute.amazonaws.com/api/";
    /////////

    protected Utility(){}

    public static synchronized Utility getInstance(){
        if(null == mInstance){
            mInstance = new Utility();
        }
        return mInstance;
    }


    /* public void highlightTextPart(Context c, TextView textView, int index, String regularExpression) {
        String fullText = textView.getText().toString();
        int startPos = 0;
        int endPos = fullText.length();
        String[] textParts = fullText.split(regularExpression);
        if (index < 0 || index > textParts.length - 1) {
            return;
        }
        if (textParts.length > 1) {
            startPos = fullText.indexOf(textParts[index]);
            endPos = fullText.indexOf(regularExpression, startPos);
            if (endPos == -1) {
                endPos = fullText.length();
            }
        }
        Spannable spannable = new SpannableString(fullText);
        ColorStateList whiteColor = new ColorStateList(new int[][] { new int[] {}}, new int[] { Color.WHITE });
        TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, whiteColor, null);
        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.RED);
        spannable.setSpan(textAppearanceSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(backgroundColorSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannable);
    }*/


    public void populateModel(ArrayList<MultiSelectModel> multi, String[] arr) {

        for(int i=0;i<arr.length;i++) {
            multi.add(i, new MultiSelectModel(i, arr[i]));
        }
    }

}






/*

*****sample post request*****

AndroidNetworking.post(Utility.getInstance().BASE_URL + "")
                 .addBodyParameter("firstname", "Amit")
                 .addBodyParameter("lastname", "Shekhar")
                 .setPriority(Priority.MEDIUM)
                 .build()
                 .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                      // do anything with response
                    }
                    @Override
                    public void onError(ANError error) {
                      // handle error
                    }
                });



        */
