package com.globocom.aureogames_2018.Model;

import org.json.JSONObject;

public class CountryCodeModel {

    public String countryName;
    public String countryCode;
    public String countryDialCode;

    public CountryCodeModel (JSONObject jsonObject){
        countryName = jsonObject.optString("name");
        countryCode = jsonObject.optString("code");
        countryDialCode = jsonObject.optString("dial_code");
    }
}
