package com.globocom.aureogames_2018.Utilities;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;

public class AppUtilities {

    public void sendAnalytics(Context mcontext,String screenName,String category,String action) {
        SendAnalyticsInBackground sendAnalyticsInBackground = new SendAnalyticsInBackground();
        sendAnalyticsInBackground.execute();
    }



    public void sendFirebaseAnalytics(){

    }

    public void sendAppsFlyer(){

    }

    public void sendInternalAnalytics(){

    }

    public void applyTypeFace(String fontName){

    }




    public class SendAnalyticsInBackground extends AsyncTask<Void, Void, Void> {

        SendAnalyticsInBackground() {

        }

        @Override
        protected Void doInBackground(Void... voids) {

            sendFirebaseAnalytics();
            sendAppsFlyer();
            sendInternalAnalytics();
            return null;
        }
    }
}
