package com.globocom.aureogames_2018.Volley;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface VolleyResponseInterface {

    void onResponse(String response, VolleyError volleyError);

}
