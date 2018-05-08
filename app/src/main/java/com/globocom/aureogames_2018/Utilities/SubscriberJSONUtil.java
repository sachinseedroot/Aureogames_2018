package com.globocom.aureogames_2018.Utilities;

import com.globocom.aureogames_2018.Model.Subscriber;

import org.json.JSONException;
import org.json.JSONObject;

public class SubscriberJSONUtil {

    public static String toJSon(Subscriber subscriber) {
        try {
            // Here we convert Java Object to JSON
            JSONObject jsonObj = new JSONObject();
            //jsonObj.put("id", subscriber.getId());
            jsonObj.put("androidId", subscriber.getAndroidId());
            //jsonObj.put("referer", subscriber.getReferer());
            jsonObj.put("mobile", subscriber.getMobile());
            jsonObj.put("kpi", subscriber.getKpi());
            jsonObj.put("timestamp", subscriber.getTimestamp());
            jsonObj.put("datetime", subscriber.getDatetime());
            jsonObj.put("isFirstInstalled", subscriber.isFirstInstalled());
            jsonObj.put("isSubscribed", subscriber.isSubscribed());
            //jsonObj.put("hlrdata", subscriber.getHlrdata());

            return jsonObj.toString();
        }
        catch(JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Subscriber getSubscriber(String data) throws JSONException {
        Subscriber subscriber = new Subscriber();
        try {
            JSONObject jObj = new JSONObject(data);
            //subscriber.setId(jObj.getString("id"));
            //subscriber.setAndroidId(jObj.getString("androidId"));
            //subscriber.setReferer(jObj.getString("referer"));
            subscriber.setMobile(jObj.getString("mobile"));
            subscriber.setKpi(jObj.getString("kpi"));
            subscriber.setTimestamp(jObj.getString("timestamp"));
            subscriber.setDatetime(jObj.getString("datetime"));
            subscriber.setFirstInstalled(jObj.getBoolean("isFirstInstalled"));
            subscriber.setSubscribed(jObj.getBoolean("isSubscribed"));
            //subscriber.setHlrdata(jObj.getString("hlrdata"));

        } catch(Exception e){
         e.printStackTrace();
        }
        return subscriber;
    }
}
