package com.globocom.aureogames_2018.Fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AppsFlyerLib;
import com.appsflyer.AppsFlyerProperties;
import com.globocom.aureogames_2018.Activities.MainActivity;
import com.globocom.aureogames_2018.Constants;
import com.globocom.aureogames_2018.R;
import com.globocom.aureogames_2018.Utilities.AppSharedPrefSettings;
import com.globocom.aureogames_2018.Utilities.AppUtilities;
import com.globocom.aureogames_2018.Utilities.ConstantsKPI;
import com.globocom.aureogames_2018.Utilities.ConstantsValues;
import com.globocom.aureogames_2018.Utilities.TrackingConstants;
import com.globocom.aureogames_2018.Utilities.ValidationUtils;
import com.globocom.aureogames_2018.Volley.VolleyObject;
import com.globocom.aureogames_2018.Volley.VolleyResponseInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.globocom.aureogames_2018.Utilities.AppUtilities.getIPAddress;


public class EnterNumberFragment extends Fragment {
    private Context mcontext;
    private EditText phonenoTV;
    private TextView countryCodeTV;
    private Button submitBTN;
    private String msisdn;
    private String msisdnfromsim;
    private String countryCode;
    private String carrierName;
    private int mobVersion;
    private VolleyObject volleyObject;
    private String operator = "";
    private ProgressDialog mProgressDialog;


    public static EnterNumberFragment newInstance(String cCode, String carName, String mobilenofromsim) {
        EnterNumberFragment f = new EnterNumberFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString("cCode", cCode);
        args.putString("carName", carName);
        args.putString("mobilenofromsim", mobilenofromsim);
        f.setArguments(args);

        return f;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mcontext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enter_number, container, false);
        countryCode = getArguments().getString("cCode", "");
        carrierName = getArguments().getString("carName", "");
        msisdnfromsim = getArguments().getString("mobilenofromsim", "");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
    }

    public void init(View view) {

        VolleyObject.initSDK(mcontext);
        volleyObject = new VolleyObject();
        initProgressbar();
        phonenoTV = (EditText) view.findViewById(R.id.phonenoTV);
        countryCodeTV = (TextView) view.findViewById(R.id.countryCodeTV);
        submitBTN = (Button) view.findViewById(R.id.submitBTN);

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(phonenoTV.getText().toString().trim())) {
                    if (!Pattern.matches("[a-zA-Z]+", phonenoTV.getText().toString().trim())) {
                        if (AppUtilities.isNetworkAvailable(mcontext)) {
                            verifyNumber();
                        } else {
                            AppUtilities.showAlertDialog(mcontext, "No internet!", "Please check your internet connection.");
                        }
                    } else {
                        AppUtilities.showToastMsg(mcontext, "Enter valid Mobile no.");
                    }
                } else {
                    AppUtilities.showToastMsg(mcontext, "Please enter a mobile number.");
                }
            }
        });

        submitBTN.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((MainActivity) mcontext).loadPage(2);
                return false;
            }
        });
    }

    public void verifyNumber() {


        try {
            msisdn = ValidationUtils.validedMobileNumber(phonenoTV.getText().toString().trim());
            mobVersion = android.os.Build.VERSION.SDK_INT;

            try {
                sendAnalytics_MOBILE_SUBMIT_KPI();
                if (ValidationUtils.checkValidMobileNumber(msisdn)) {
                    System.out.println("--------------OTP SENT HERE-------------");
                    sendOTP();

                } else {
                    phonenoTV.setText("");
                    AppUtilities.showToastMsg(mcontext, "Enter valid Mobile no.");
                    sendAnalytics_INVALID_MOBILE_KPI();
                }
            } catch (Exception e) {
                e.printStackTrace();
                AppUtilities.showToastMsg(mcontext, "Something went wrong!");
                System.out.println("-EnterNumberFragment-validedMobileNumber---- " + e);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AppUtilities.showToastMsg(mcontext, "Something went wrong!");
            System.out.println("-EnterNumberFragment-checkValidMobileNumber---- " + e);
        }
    }

    public void sendOTP() {
        final String url = ConstantsValues.GET_OPERATOR.replace("@msisdn", msisdn);
        showProgress();
        volleyObject.fetchDatabyUrlString(url, new VolleyResponseInterface() {
            @Override
            public void onResponse(String response, VolleyError volleyError) {
                stopProgress();
                if (volleyError == null) {
                    resultDATA(response);
                    System.out.println("----sendotpvolley cal url---- " + url);
                    System.out.println("----sendotpvolley cal response---- " + response);
                } else {
                    System.out.println("----sendotpvolley cal exception---- " + volleyError.getMessage());
                    AppUtilities.showAlertDialog(mcontext,"Oops!","Something went wrong! Try again...");
                }
            }
        });
    }

    public void sendAnalytics_MOBILE_SUBMIT_KPI() {
        String url = ConstantsValues.MOBILE_SUBMIT_KPI.replace("@msisdn", msisdn) + "&ip=" + getIPAddress(true) + "&version=" + mobVersion;
        AppUtilities.sendAnalytics(mcontext,
                "enter_number_screen",
                "screen_one",
                "view", ConstantsKPI.PRODUCT_REPEAT_KPI,
                url
                , "2",
                ConstantsKPI.PRODUCT_REPEAT_KPI,
                msisdn,
                2,
                TrackingConstants.eventServiceMap.get(2),
                "", "", "", "");
    }


    public void sendAnalytics_VALID_HLR_MOBILE_KPI() {

        AppUtilities.sendAnalytics(mcontext,
                "enter_number_screen",
                "screen_one",
                "response_received", ConstantsKPI.VALID_HLR_MOBILE_KPI,
                "",
                "2",
                ConstantsKPI.VALID_HLR_MOBILE_KPI,
                msisdn,
                12,
                TrackingConstants.eventServiceMap.get(12),
                "", "", "", "");
    }


    public void sendAnalytics_INVALID_MOBILE_KPI() {
        String url = ConstantsValues.invalidMobileUrl + msisdn + "&ip=" + getIPAddress(true);
        AppUtilities.sendAnalytics(mcontext,
                "enter_number_screen",
                "screen_one",
                "invalid_number", ConstantsKPI.INVALID_MOBILE_KPI,
                url
                , "2",
                ConstantsKPI.INVALID_MOBILE_KPI,
                msisdn,
                8,
                TrackingConstants.eventServiceMap.get(8),
                "", "", "", "");
    }


    public void resultDATA(String result) {
        String hlrStatus = "SUBSCRIBERSTATUS_CONNECTED";
        if (result.trim().startsWith(Constants.CARRIER_CODE_1) || result.trim().startsWith(Constants.CARRIER_CODE_2) || result.trim().startsWith("INVALID_OPERATOR")) {
            try {
                hlrStatus = result.split(",")[1];
            } catch (Exception e) {
                //Toast.makeText(getApplicationContext(), "HLR Status Exception:" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        //TODO: DU MOBILE SUBSCRIBER
        if (result.trim().startsWith(Constants.CARRIER_CODE_1) && hlrStatus.trim().equals("SUBSCRIBERSTATUS_CONNECTED")) {

            operator = Constants.CARRIER_CODE_1;
            sendOTPfromOperator( ConstantsValues.sentDUOTP + msisdn+"&ip="+getIPAddress(true));
            sendAnalytics_VALID_HLR_MOBILE_KPI();
            AppUtilities.sendAnalytics(mcontext,
                    "enter_number_screen",
                    "screen_one",
                    "du_otp_api", ConstantsKPI.PIN_SEND_KPI,
                    "",
                    "2",
                    ConstantsKPI.PIN_SEND_KPI,
                    msisdn,
                    3,
                   "PIN_SEND_KPI",
                    "", "", "", "");


        } else if (result.trim().startsWith(Constants.CARRIER_CODE_2) && hlrStatus.trim().equals("SUBSCRIBERSTATUS_CONNECTED")) {

            operator = Constants.CARRIER_CODE_2;
            sendOTPfromOperator(ConstantsValues.sentEtislatOTP + msisdn + "&ip=" + getIPAddress(true));
            sendAnalytics_VALID_HLR_MOBILE_KPI();
            AppUtilities.sendAnalytics(mcontext,
                    "enter_number_screen",
                    "screen_one",
                    "etisalat_otp_api", ConstantsKPI.PIN_SEND_KPI,
                    "",
                    "2",
                    ConstantsKPI.PIN_SEND_KPI,
                    msisdn,
                    3,
                    "PIN_SEND_KPI",
                    "", "", "", "");


        } else if ((hlrStatus.trim().equals("SUBSCRIBERSTATUS_UNDETERMINED") || hlrStatus.trim().equals("SUBSCRIBERSTATUS_ABSENT")) || hlrStatus.trim().equals("SUBSCRIBERSTATUS_UNKNOWN_MSISDN")) {
            phonenoTV.setText("");

            AppUtilities.showAlertDialog(mcontext,"Oops!","Enter valid mobile no.");
            AppUtilities.sendAnalytics(mcontext,
                    "enter_number_screen",
                    "screen_one",
                    "response_received_invalid_hlr", ConstantsKPI.INVALID_HLR_MOBILE_KPI,
                    "",
                    "2",
                    ConstantsKPI.INVALID_HLR_MOBILE_KPI,
                    msisdn,
                    11,
                    TrackingConstants.eventServiceMap.get(11),
                    "", "", "", "");


        } else {
            System.out.println("-----unexpected response-----" + result);
        }
    }


    public void initProgressbar() {
        mProgressDialog = new ProgressDialog(mcontext);
        mProgressDialog.setMessage("Loading, please wait...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    public void showProgress() {
        try {
            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopProgress() {

        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendOTPfromOperator(final String url) {
        showProgress();
        volleyObject.fetchDatabyUrlString(url, new VolleyResponseInterface() {
            @Override
            public void onResponse(String response, VolleyError volleyError) {
                stopProgress();
                if (volleyError == null) {

                    System.out.println("----sendOTPfromOperator cal url---- " + url);
                    System.out.println("----sendOTPfromOperator cal response---- " + response);
                    AppSharedPrefSettings.setIsUserCountryCode(mcontext,countryCode);
                    ((MainActivity) mcontext).loadScreenTwo(msisdn, operator,url);
                } else {
                    System.out.println("----sendOTPfromOperator cal exception---- " + volleyError.getMessage());
                    AppUtilities.showAlertDialog(mcontext,"Oops!","Something went wrong! Try again...");
                }
            }
        });
    }
}
