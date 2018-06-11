package com.example.yashwant.matchezy;

import java.util.Calendar;

/**
 * Created by avinash on 27/12/17.
 */

public class User {

    private static User mInstance= null;

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
            case "01": month = "JAN";break;
            case "02": month = "FEB";break;
            case "03": month = "MAR";break;
            case "04": month = "APR";break;
            case "05": month = "MAY";break;
            case "06": month = "JUN";break;
            case "07": month = "JUL";break;
            case "08": month = "AUG";break;
            case "09": month = "SEP";break;
            case "10": month = "OCT";break;
            case "11": month = "NOV";break;
            case "12": month = "DEC";break;
            default:   month = "HELLO";

        }

        String dateFinal = day + " " + month +  " " + year;

        return dateFinal;
    }


    public String getFormattedDate(String date) {
        return date.substring(0,10);
    }


    ///url///
    public String BASE_URL = "http://192.168.29.75:3000/api/";
    /////////

    protected User(){}

    public static synchronized User getInstance(){
        if(null == mInstance){
            mInstance = new User();
        }
        return mInstance;
    }


}
