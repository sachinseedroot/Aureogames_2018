package com.globocom.aureogames_2018.Model;

import org.json.JSONObject;

public class UserDetailsModel {

    public String userMSISDN;
    public String userCountryCode;
    public String userKPI;
    public String userCarrierName;

    public String userAndroidID;
    public String userUUID;
    public String userReferrer;
    public String userDateTime;
    public boolean userIsFirstInstall;
    public boolean userIsSubscribed;
    private String userHRLdata;


    public  UserDetailsModel(JSONObject jsonObject){
        userMSISDN = jsonObject.optString("msisdn");
        userCountryCode = jsonObject.optString("countrycode");
        userKPI = jsonObject.optString("kpi");
        userCarrierName = jsonObject.optString("carriername");

        userAndroidID = jsonObject.optString("userAndroidID");
        userUUID = jsonObject.optString("userUUID");
        userReferrer = jsonObject.optString("userReferrer");
        userDateTime = jsonObject.optString("userDateTime");
        userHRLdata = jsonObject.optString("userHRLdata");
        userIsFirstInstall = jsonObject.optBoolean("userIsFirstInstall");
        userIsSubscribed = jsonObject.optBoolean("userIsSubscribed");

    }
}
