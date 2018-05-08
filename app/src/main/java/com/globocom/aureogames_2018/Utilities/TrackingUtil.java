package com.globocom.aureogames_2018.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TrackingUtil {

    /*      1.	INSTALL KPI -LEVEL_ACHIEVED-- level 1 score 100
            2.	MOBILE SUBMIT KPI - INITIATED_CHECKOUT– level 2 score 200
            3.	PIN SEND KPI - ADD_TO_CART – level 3 score 300
            4.	PIN READ KPI – ADD_PAYMENT_INFO - level 4 score 400
            5.	PIN VERIFY KPI - COMPLETE_REGISTRATION – level 5 score 500
            6.	PRODUCT PAGE KPI - PURCHASE – level 6 score 600
    */

    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "geniogames";

//    public static void trackEvents(Context context, Integer level, Object score) {
//
//        Map<String, Object> eventValue = new HashMap<String, Object>();
//        eventValue.put(AFInAppEventParameterName.LEVEL, level);
//        eventValue.put(AFInAppEventParameterName.SCORE, score);
//        AppsFlyerLib.getInstance().trackEvent(context, AFInAppEventType.LEVEL_ACHIEVED, eventValue);
//    }


    public synchronized static String id(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }
        return uniqueID;
    }

//    public static void logFeatureSelectedEvent(String id, String name,String msisdn, String kpi,FirebaseAnalytics mFirebaseAnalytics) {
//        Bundle bundle = new Bundle();
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
//        bundle.putString(FirebaseAnalytics.Param.VALUE,msisdn);
//        bundle.putString(FirebaseAnalytics.Param.LEVEL,kpi);
//        mFirebaseAnalytics.logEvent(kpi, bundle);
//    }
}
