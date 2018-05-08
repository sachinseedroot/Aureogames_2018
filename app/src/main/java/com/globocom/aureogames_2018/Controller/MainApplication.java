package com.globocom.aureogames_2018.Controller;

import android.app.Application;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        System.out.println("-------------init-----mainapplication");
    }
}
