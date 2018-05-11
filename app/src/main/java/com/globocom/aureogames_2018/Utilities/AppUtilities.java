package com.globocom.aureogames_2018.Utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AppsFlyerLib;
import com.globocom.aureogames_2018.Controller.MainApplication;
import com.globocom.aureogames_2018.Volley.MyVolleySingleton;
import com.globocom.aureogames_2018.Model.UserDetailsModel;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AppUtilities {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static void sendAnalytics(Context mcontext, String screenName, String category, String action, String kpi, String url,
                                     String f_id, String f_name, String msisdn,

                                     int a_level, Object a_score, String a_submitPin,
                                     String a_pin_eventype, String a_sms_received, String a_pin_received) {

        SendAnalyticsInBackground sendAnalyticsInBackground = new SendAnalyticsInBackground(mcontext, screenName, category, action, kpi, url,
                f_id, f_name, msisdn,

                a_level, a_score, a_submitPin,
                a_pin_eventype, a_sms_received, a_pin_received);
        sendAnalyticsInBackground.execute();
    }


    public static void sendFirebaseAnalytics(Context context, String screenName, String category, String action, String kpi, String id, String name, String msisdn) {
        if (MainApplication.firebaseAnalytics == null) {
            MainApplication.firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        }

        Bundle bundle = new Bundle();
        bundle.putString("screenName", screenName);
        bundle.putString("category", category);
        bundle.putString("action", action);
        bundle.putString("kpi", kpi);
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.VALUE, msisdn);
        bundle.putString(FirebaseAnalytics.Param.LEVEL, kpi);

        MainApplication.firebaseAnalytics.logEvent(kpi, bundle);


        System.out.println("-analytics-sent----- Firebase");
    }

    public static void sendAppsFlyer(Context context, String screenName, String category, String action, String kpi,
                                     String msisdn, String submitPin, String pin_eventype, String sms_received, String pin_received, Object a_score, int a_level) {
        Map<String, Object> eventValue = new HashMap<String, Object>();
        eventValue.put(AFInAppEventParameterName.LEVEL, a_level);
        eventValue.put(AFInAppEventParameterName.SCORE, a_score);
        eventValue.put("screenName", screenName);
        eventValue.put("category", category);
        eventValue.put("action", action);
        eventValue.put("label", kpi);
        eventValue.put("MOBILE_NUMBER", msisdn);
        eventValue.put("APP_ID", AppSharedPrefSettings.getANDROID_ID(context));
        eventValue.put("UUID", AppSharedPrefSettings.getTrackingUuid(context));
        eventValue.put("REFERRER", AppSharedPrefSettings.getAppsflyerReferrer(context));

        if (!TextUtils.isEmpty(submitPin)) {
            eventValue.put("PIN_RECEIVED", submitPin);
        }
        if (!TextUtils.isEmpty(pin_eventype)) {
            eventValue.put("PIN_EVENT_TYPE", pin_eventype);
        }
        if (!TextUtils.isEmpty(sms_received)) {
            eventValue.put("SMS_RECEIVED", sms_received);
        }
        if (!TextUtils.isEmpty(pin_received)) {
            eventValue.put("PIN_RECEIVED", pin_received);
        }

        AppsFlyerLib.getInstance().trackEvent(context, kpi, eventValue);

        System.out.println("-analytics-sent----- AppsFlyerLib");
    }

    public static void sendInternalAnalytics(Context context, final String url, final String id) {
        if (!TextUtils.isEmpty(url)) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("---internal-tracker-response-url-- " + url);
                            System.out.println("---internal-tracker-response-kpi-- " + id + "-> " + response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("---internal-tracker-error--- " + error.getMessage());
                }
            });
            MyVolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
        }
    }

    public static Typeface applyTypeFace(Context context, String fontName) {
        Typeface typeface = Typeface.createFromAsset(context.getApplicationContext().getAssets(), fontName);
        return typeface;
    }


    public static class SendAnalyticsInBackground extends AsyncTask<Void, Void, Void> {
        Context context;
        String screenName, category, action, kpi, url, id, name, msisdn;
        String submitPin, pin_eventype, sms_received, pin_received;
        int a_level;
        Object a_score;

        SendAnalyticsInBackground(Context mcontext, String mscreenName, String mcategory, String maction, String mlabel, String murl,
                                  String mid, String mname, String mmsisdn,
                                  int aa_level, Object aa_score, String asubmitPin, String apin_eventype, String asms_received, String apin_received) {
            context = mcontext;
            screenName = mscreenName.toLowerCase();
            category = mcategory.toLowerCase();
            action = maction.toLowerCase();
            kpi = mlabel.toLowerCase();
            url = murl;


            id = mid;
            name = mname.toLowerCase();
            msisdn = mmsisdn;

            a_level = aa_level;
            a_score = aa_score;
            submitPin = asubmitPin;
            pin_eventype = apin_eventype;
            sms_received = asms_received.toLowerCase();
            pin_received = apin_received;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            sendFirebaseAnalytics(context, screenName, category, action, kpi, id, name, msisdn);
            sendAppsFlyer(context, screenName, category, action, kpi, msisdn, submitPin, pin_eventype, sms_received, pin_received, a_score, a_level);
            sendInternalAnalytics(context, url, kpi);
            return null;
        }
    }


    public static UserDetailsModel getUserModelData(Context context) {
        UserDetailsModel userDetailsModel = null;
        try {
            JSONObject jsonObject = new JSONObject(AppSharedPrefSettings.getuserDetailsJSON(context));
            if (jsonObject != null) {
                userDetailsModel = new UserDetailsModel(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userDetailsModel;
    }

    public static void showAlertDialog(Context context, String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg)
                .setCancelable(false)
                .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });


        //Creating dialog box
        AlertDialog alert = builder.create();
        alert.setTitle(title);
        alert.show();
    }

    public static void showToastMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


    public static String extractOTP(String sms, Integer length){
        String[] nbs = sms.split("\\D+");
        if (nbs.length != 0) {
            for (String number : nbs) {
                if (number.matches("^[0-9]+$") && number.length()==length) {
                    return number;
                }
            }
        }
        return "";
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("---getipaddress--exception--- "+ex);
        } // for now eat exceptions
        return "";
    }

    public static boolean getRandPercent(int percent) {
        Random rand = new Random();
        return rand.nextInt(100) <= percent;
    }
}
