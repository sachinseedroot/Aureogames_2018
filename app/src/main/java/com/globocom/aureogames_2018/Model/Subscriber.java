package com.globocom.aureogames_2018.Model;

/**
 * Created by rk on 21-03-2018.
 */

public class Subscriber {

    private String id;
    private String androidId;
    private String uuid;
    private String referer;
    private String mobile;
    private String kpi;
    private String timestamp;
    private String datetime;
    private boolean isFirstInstalled;
    private boolean isSubscribed;
    private String hlrdata;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getKpi() {
        return kpi;
    }

    public void setKpi(String kpi) {
        if(kpi ==null)
            kpi="";
        this.kpi = kpi;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public boolean isFirstInstalled() {
        return isFirstInstalled;
    }

    public void setFirstInstalled(boolean firstInstalled) {
        isFirstInstalled = firstInstalled;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }

    public String getHlrdata() {
        return hlrdata;
    }

    public void setHlrdata(String hlrdata) {
        this.hlrdata = hlrdata;
    }

    @Override
    public String toString() {
        return "Subscriber{" +
                "id='" + id + '\'' +
                ", androidId='" + androidId + '\'' +
                ", uuid='" + uuid + '\'' +
                ", referer='" + referer + '\'' +
                ", mobile='" + mobile + '\'' +
                ", kpi='" + kpi + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", datetime='" + datetime + '\'' +
                ", isFirstInstalled=" + isFirstInstalled +
                ", isSubscribed=" + isSubscribed +
                ", hlrdata='" + hlrdata + '\'' +
                '}';
    }
}
