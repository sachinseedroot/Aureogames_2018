package com.globocom.aureogames_2018.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSharedPrefSettings {

    public static void setuserMSISDN(Context context, String msisdn) {
        SharedPreferences prefs = Prefs.get(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userMSISDN", msisdn);
        editor.commit();
    }

    public static String getuserMSISDN(Context context) {
        SharedPreferences prefs = Prefs.get(context);
        return prefs.getString("userMSISDN", "");
    }


    public static void setIsUserVerified(Context context, boolean IsUserVerified) {
        SharedPreferences prefs = Prefs.get(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("IsUserVerified", IsUserVerified);
        editor.commit();
    }

    public static boolean getIsUserVerified(Context context) {
        SharedPreferences prefs = Prefs.get(context);
        return prefs.getBoolean("IsUserVerified", false);
    }
}
