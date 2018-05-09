package com.globocom.aureogames_2018.Controller;

import android.app.Application;
import android.util.Log;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.chartboost.sdk.Chartboost;
import com.crashlytics.android.Crashlytics;
import com.globocom.aureogames_2018.Constants;
import com.globocom.aureogames_2018.Utilities.AppConstants;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Map;

import io.fabric.sdk.android.Fabric;

public class MainApplication extends Application {

    public static FirebaseAnalytics firebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            Fabric.with(this, new Crashlytics());

            firebaseAnalytics = FirebaseAnalytics.getInstance(this);

            AppsFlyerConversionListener conversionDataListener =
                    new AppsFlyerConversionListener() {
                        @Override
                        public void onInstallConversionDataLoaded(Map<String, String> conversionData) {
                            for (String attrName : conversionData.keySet()) {
                                Log.d(AppsFlyerLib.LOG_TAG, "attribute: " + attrName + " = " + conversionData.get(attrName));
                            }
                        }

                        @Override
                        public void onInstallConversionFailure(String errorMessage) {

                            Log.d(AppsFlyerLib.LOG_TAG, "error getting conversion data: " + errorMessage);
                        }

                        @Override
                        public void onAppOpenAttribution(Map<String, String> attributionData) {
                            for (String attrName : attributionData.keySet()) {
                                Log.d(AppsFlyerLib.LOG_TAG, "onAppOpenAttribution - attribute: " + attrName + " = " + attributionData.get(attrName));
                            }
                        }

                        @Override
                        public void onAttributionFailure(String errorMessage) {

                            Log.d(AppsFlyerLib.LOG_TAG, "error onAttributionFailure : " + errorMessage);
                        }
                    };
            AppsFlyerLib.getInstance().init(Constants.AF_DEV_KEY, conversionDataListener, getApplicationContext());
            AppsFlyerLib.getInstance().startTracking(this);

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("-Mainapplication---- "+ex);
        }

    }
}
