package com.globocom.aureogames_2018.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

public final class Prefs {

    public static SharedPreferences get(Context context){
        return context.getSharedPreferences("aureogames_2018" , Context.MODE_PRIVATE);
    }
}