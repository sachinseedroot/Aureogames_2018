package com.globocom.aureogames_2018.Utilities;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerLib;
import com.globocom.aureogames_2018.Controller.MainApplication;
import com.globocom.aureogames_2018.Controller.MyVolleySingleton;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class AppUtilities {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static void sendAnalytics(Context mcontext, String screenName, String category, String action,String label,String url) {
        SendAnalyticsInBackground sendAnalyticsInBackground = new SendAnalyticsInBackground(mcontext,screenName,category,action,label,url);
        sendAnalyticsInBackground.execute();
    }


    public static void sendFirebaseAnalytics(Context context, String screenName, String category, String action, String label) {
        if (MainApplication.firebaseAnalytics == null) {
            MainApplication.firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        }

        Bundle bundle = new Bundle();
        bundle.putString("screenName", screenName);
        bundle.putString("category", category);
        bundle.putString("action", action);
        bundle.putString("kpi", label);

        MainApplication.firebaseAnalytics.logEvent(label, bundle);


        System.out.println("-analytics-sent----- Firebase");
    }

    public static void sendAppsFlyer(Context context, String screenName, String category, String action, String label) {
        Map<String, Object> eventValue = new HashMap<String, Object>();
        eventValue.put("screenName", screenName);
        eventValue.put("category", category);
        eventValue.put("action", action);
        eventValue.put("label", label);
        AppsFlyerLib.getInstance().trackEvent(context, label, eventValue);

        System.out.println("-analytics-sent----- AppsFlyerLib");
    }

    public static void sendInternalAnalytics(Context context, String screenName, String category, String action, String label,String url) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MyVolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    public static Typeface applyTypeFace(Context context, String fontName) {
        Typeface typeface = Typeface.createFromAsset(context.getApplicationContext().getAssets(), fontName);
        return typeface;
    }


    public static class SendAnalyticsInBackground extends AsyncTask<Void, Void, Void> {
        Context context;
        String screenName,  category,  action,  label, url;
        SendAnalyticsInBackground(Context mcontext, String mscreenName, String mcategory, String maction, String mlabel,String murl) {
            context = mcontext;
            screenName=mscreenName;
            category=mcategory;
            action=maction;
            label=mlabel;
            url=murl;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            sendFirebaseAnalytics(context,screenName,category,action,label);
            sendAppsFlyer(context,screenName,category,action,label);
            sendInternalAnalytics(context,screenName,category,action,label,url);
            return null;
        }
    }
}
