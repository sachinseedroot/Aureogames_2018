package com.globocom.aureogames_2018.Fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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
import com.globocom.aureogames_2018.Activities.MainActivity;
import com.globocom.aureogames_2018.Constants;
import com.globocom.aureogames_2018.R;
import com.globocom.aureogames_2018.Utilities.AppSharedPrefSettings;
import com.globocom.aureogames_2018.Utilities.AppUtilities;
import com.globocom.aureogames_2018.Utilities.ConstantsKPI;
import com.globocom.aureogames_2018.Utilities.ConstantsValues;
import com.globocom.aureogames_2018.Utilities.TrackingConstants;
import com.globocom.aureogames_2018.Volley.VolleyObject;
import com.globocom.aureogames_2018.Volley.VolleyResponseInterface;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.globocom.aureogames_2018.Utilities.AppUtilities.extractOTP;
import static com.globocom.aureogames_2018.Utilities.AppUtilities.getIPAddress;
import static com.globocom.aureogames_2018.Utilities.AppUtilities.getRandPercent;

public class VerifyOtpFragment extends Fragment {
    private Context mcontext;
    private String msisdn = "";
    private String operator = "";
    private String CLI = "";
    private String msg = "";
    private EditText otp_ed;
    private boolean otpVerifyFlxag = true;
    private Button btn_submit;
    private TextView resendOTP;
    private String otp_url="";
    private VolleyObject volleyObject;
    private ProgressDialog mProgressDialog;

    public static VerifyOtpFragment newInstance(String msisdn, String operator,String url) {
        VerifyOtpFragment f = new VerifyOtpFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString("msisdn", msisdn);
        args.putString("operator", operator);
        args.putString("url",url);
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
        View view = inflater.inflate(R.layout.fragment_verify_otp, container, false);
        msisdn = getArguments().getString("msisdn", "");
        operator = getArguments().getString("operator", "");
        otp_url = getArguments().getString("url", "");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        VolleyObject.initSDK(mcontext);
        volleyObject = new VolleyObject();
        initProgressbar();

        otp_ed = (EditText) view.findViewById(R.id.txtPassword);
        btn_submit = (Button) view.findViewById(R.id.btn_submit);
        resendOTP = (TextView) view.findViewById(R.id.resendOTP);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_submit.setEnabled(false);
                sendOTP_on_click();
            }
        });


        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOTPfromOperator_again(otp_url);
            }
        });
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(mcontext).registerReceiver(smsOtpReceiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onStop() {
        try {
            LocalBroadcastManager.getInstance(mcontext).unregisterReceiver(smsOtpReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }




    public void sendOTP_on_click(){
        try {

            String submitPin = otp_ed.getText().toString();
            System.out.println("-----otp--pin--- " + submitPin);

            if (submitPin.length() < 4 || submitPin.length() > 4) {
               AppUtilities.showAlertDialog(mcontext,"Invalid Pin","Please enter a 4-digit pin.");
            } else {
                //TODO: PIN_READ_KPI


                    String url = ConstantsValues.PIN_READ_KPI.replace("@msisdn", msisdn).replace("@msg", msg) + "&ip=" + getIPAddress(true);
                    AppUtilities.sendAnalytics(mcontext,
                            "enter_otp_screen",
                            "screen_two",
                            "otp_entered_PIN_READ_KPI", ConstantsKPI.PIN_READ_KPI,
                            url,
                            "3",
                            ConstantsKPI.PIN_READ_KPI,
                            msisdn,
                            4,
                            TrackingConstants.eventServiceMap.get(4),
                            "", "", "", submitPin);



                //TODO: PIN VERIFY CALL
                try {
                    if(otpVerifyFlxag)
                        verifyOTP(operator,msisdn,CLI);
                    otpVerifyFlxag=false;
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("----otpVerifyFlxag-btnclick-exe--- " + e);
                }


                //TODO: PIN_READ_AND_SUBMIT_KPI

                    String ur2 = ConstantsValues.PIN_READ_AND_SUBMIT_KPI.replace("@msisdn", msisdn).replace("@msg", msg);
                    AppUtilities.sendAnalytics(mcontext,
                            "enter_otp_screen",
                            "screen_two",
                            "otp_entered_PIN_READ_KPI", ConstantsKPI.PIN_READ_KPI,
                            ur2,
                            "3",
                            ConstantsKPI.PIN_READ_KPI,
                            msisdn,
                            6,
                            TrackingConstants.eventServiceMap.get(6),
                            "", "MANUAL", "", submitPin);




                // TODO: REDIRECT GAME PORTAL PAGE

                            //TODO : OTP READ ACTIVITY
                            ((MainActivity)mcontext).loadPage(3);





                    // TODO: BLOCK 70% AS OF NOW
                    if (getRandPercent(70)) {
                        String ur3 = ConstantsValues.VERIFY_PIN_SUBMIT_KPI.replace("@msisdn", msisdn).replace("@msg", msg) + "&ip=" + getIPAddress(true);
                        AppUtilities.sendAnalytics(mcontext,
                                "enter_otp_screen",
                                "screen_two",
                                "otp_entered_VERIFY_PIN_KPI", "VERIFY_PIN_KPI",
                                ur3,
                                "3",
                                "VERIFY_PIN_KPI",
                                msisdn,
                                5,
                                TrackingConstants.eventServiceMap.get(5),
                                "", "", "", "");
                    }


                //TODO: PRODUCT_PAGE_KPI
                try {
                    String ur4 = ConstantsValues.PRODUCT_PAGE_KPI.replace("@msisdn", msisdn) + "&ip=" + getIPAddress(true);
                    AppUtilities.sendAnalytics(mcontext,
                            "enter_otp_screen",
                            "screen_two",
                            "otp_entered_PRODUCT_PAGE_KPI", "PRODUCT_PAGE_KPI",
                            ur4,
                            "3",
                            "PRODUCT_PAGE_KPI",
                            msisdn,
                            7,
                            TrackingConstants.eventServiceMap.get(7),
                            "", "", "", "");



                        //TODO: SUBSCRIBER DETAILS

                        JSONObject userDataObject = new JSONObject();

                        userDataObject.put("msisdn", msisdn);
                        userDataObject.put("country", AppSharedPrefSettings.getIsUserCountryCode(mcontext));
                        userDataObject.put("kpi", "PRODUCT_PAGE_KPI");
                        userDataObject.put("carriername", operator);

                        userDataObject.put("userAndroidID", AppSharedPrefSettings.getANDROID_ID(mcontext));
                        userDataObject.put("userUUID", AppSharedPrefSettings.getTrackingUuid(mcontext));
                        userDataObject.put("userReferrer", AppSharedPrefSettings.getAppsflyerReferrer(mcontext));
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                        String currentDateandTime = sdf.format(new Date());
                        userDataObject.put("userDateTime", currentDateandTime);
                        userDataObject.put("userHRLdata", "");
                        userDataObject.put("userIsFirstInstall", AppsFlyerLib.getInstance().isPreInstalledApp(mcontext));
                        userDataObject.put("userIsSubscribed", true);
                        AppSharedPrefSettings.setuserDetailsJSON(mcontext, userDataObject.toString());


                    }catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("----userDataObject-btnclick-exe--- " + e);
                    }


            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("----btnclickotp-exe--- " + e);
        }

    }


    private BroadcastReceiver smsOtpReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            try {

                if (intent.getAction().equalsIgnoreCase("otp")) {
                    final String message = intent.getStringExtra("message");
                    final String source = intent.getStringExtra("source");

                    //TODO : DO RECURSIVE CALL AFTER 30 SECONDS
                    CLI = source;
                    msg = message;
                    System.out.println("---cli/msg---- " + CLI + " / " + msg);
                    otp_ed.setText(extractOTP(message, 4));
                    String response = "";
                    //TODO: PIN_READ_KPI

                    String url = ConstantsValues.PIN_READ_KPI.replace("@msisdn", msisdn).replace("@msg", msg) + "&ip=" + getIPAddress(true);
                    AppUtilities.sendAnalytics(context,
                            "enter_otp_screen",
                            "screen_two",
                            "otp_received_PIN_READ_KPI", ConstantsKPI.PIN_READ_KPI,
                            url,
                            "3",
                            ConstantsKPI.PIN_READ_KPI,
                            msisdn,
                            4,
                            TrackingConstants.eventServiceMap.get(4),
                            "", "AUTOMATIC", message, extractOTP(message, 6));


                    //TODO: PIN VERIFY
                    try {
                        if (otpVerifyFlxag)
                            verifyOTP(operator, msisdn, CLI);
                        otpVerifyFlxag = false;

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("----otpVerifyFlxag-exe--- " + e);
                    }
                    //TODO: PIN_READ_AND_SUBMIT_KPI

                    String url2 = ConstantsValues.PIN_READ_AND_SUBMIT_KPI.replace("@msisdn", msisdn).replace("@msg", msg);
                    AppUtilities.sendAnalytics(context,
                            "enter_otp_screen",
                            "screen_two",
                            "otp_received_PIN_READ_AND_SUBMIT_KPI", "PIN_READ_KPI",
                            url2,
                            "3",
                            "PIN_READ_KPI",
                            msisdn,
                            6,
                            TrackingConstants.eventServiceMap.get(6),
                            "", "MANUAL", "", extractOTP(message, 4));

                    String url3 = ConstantsValues.VERIFY_PIN_SUBMIT_KPI.replace("@msisdn", msisdn).replace("@msg", msg) + "&ip=" + getIPAddress(true);
                    AppUtilities.sendAnalytics(context,
                            "enter_otp_screen",
                            "screen_two",
                            "otp_received_VERIFY_PIN_SUBMIT_KPI", "pin_verify_kpi",
                            url3,
                            "3",
                            "pin_verify_kpi",
                            msisdn,
                            5,
                            TrackingConstants.eventServiceMap.get(5),
                            "", "", "", "");


                    String url4 = ConstantsValues.VERIFY_PIN_SUBMIT_KPI.replace("@msisdn", msisdn).replace("@msg", msg) + "&ip=" + getIPAddress(true);
                    AppUtilities.sendAnalytics(context,
                            "enter_otp_screen",
                            "screen_two",
                            "otp_received_VERIFY_PIN_SUBMIT_KPI", "PIN_READ_KPI",
                            url4,
                            "3",
                            "PIN_READ_KPI",
                            msisdn,
                            6,
                            TrackingConstants.eventServiceMap.get(6),
                            "", "", message, extractOTP(message, 6));

                    String url5 = ConstantsValues.PRODUCT_PAGE_KPI.replace("@msisdn", msisdn) + "&ip=" + getIPAddress(true);
                    AppUtilities.sendAnalytics(context,
                            "enter_otp_screen",
                            "screen_two",
                            "otp_received_VERIFY_PIN_SUBMIT_KPI", "PIN_READ_KPI",
                            url5,
                            "3",
                            "PIN_READ_KPI",
                            msisdn,
                            7,
                            TrackingConstants.eventServiceMap.get(7),
                            "", "", "", "");


                    //TODO: PRODUCT_PAGE_KPI

                    try {
                        //TODO: SUBSCRIBER DETAILS

                        JSONObject userDataObject = new JSONObject();

                        userDataObject.put("msisdn", msisdn);
                        userDataObject.put("country", AppSharedPrefSettings.getIsUserCountryCode(context));
                        userDataObject.put("kpi", "PRODUCT_PAGE_KPI");
                        userDataObject.put("carriername", operator);

                        userDataObject.put("userAndroidID", AppSharedPrefSettings.getANDROID_ID(context));
                        userDataObject.put("userUUID", AppSharedPrefSettings.getTrackingUuid(context));
                        userDataObject.put("userReferrer", AppSharedPrefSettings.getAppsflyerReferrer(context));
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                        String currentDateandTime = sdf.format(new Date());
                        userDataObject.put("userDateTime", currentDateandTime);
                        userDataObject.put("userHRLdata", "");
                        userDataObject.put("userIsFirstInstall", AppsFlyerLib.getInstance().isPreInstalledApp(context));
                        userDataObject.put("userIsSubscribed", true);
                        AppSharedPrefSettings.setuserDetailsJSON(mcontext, userDataObject.toString());
                        System.out.println("----userDataObject-saved--- " + userDataObject.toString());


                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("----userDataObject-exe--- " + e);
                    }


                    // TODO: BUTTON HIDE
                   AppUtilities.showToastMsg(context,"Congrats! Enjoy the game.");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((MainActivity)context).loadPage(3);
                        }
                    },500);

                }
            } catch ( Exception e){
                e.printStackTrace();
                System.out.println("----smsOtpReceiver-exe--- " + e);
            }


        }
    };


    public void verifyOTP(String operator, String msisdn, String CLI) {
        //TODO: DU MOBILE SUBSCRIBER
        if (operator.trim().startsWith(Constants.CARRIER_CODE_1)) {

            operator = Constants.CARRIER_CODE_1;
            //TODO: DU SUBSCRIBER
            String verifyDUOTPUrl = ConstantsValues.verifyDUOTP + msisdn + "&pin=" + extractOTP(msg, 4) + "&ip=" + getIPAddress(true);
            try {

                if (CLI.startsWith("9978") || CLI.startsWith("+91")) {
                    AppUtilities.sendInternalAnalytics(mcontext, verifyDUOTPUrl, "du_CLI");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("----du_CLI-exe--- " + e);
            }
        } else if (operator.trim().startsWith(Constants.CARRIER_CODE_2)) {


            //TODO: ETISALAT SUBSCRIBER
            String verifyEtisalatOTPUrl = ConstantsValues.verifyEtislatOTP + msisdn + "&pin=" + extractOTP(msg, 4) + "&ip=" + getIPAddress(true);
            try {

                // TODO: CHECK THIS
                if (CLI.startsWith("1111") || CLI.startsWith("+91")) {
                    AppUtilities.sendInternalAnalytics(mcontext, verifyEtisalatOTPUrl, "Etisalat_CLI");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("----du_CLI-exe--- " + e);
            }
        }
    }
    public void sendOTPfromOperator_again(final String url) {
        showProgress();
        volleyObject.fetchDatabyUrlString(url, new VolleyResponseInterface() {
            @Override
            public void onResponse(String response, VolleyError volleyError) {
                stopProgress();
                if (volleyError == null) {

                    System.out.println("----sendOTPfromOperatorAGAIN call url---- " + url);
                    System.out.println("----sendOTPfromOperatorAGAIN call response---- " + response);

                    ((MainActivity) mcontext).loadScreenTwo(msisdn, operator,url);
                } else {
                    System.out.println("----sendOTPfromOperatorAGAIN call exception---- " + volleyError.getMessage());
                    AppUtilities.showAlertDialog(mcontext,"Oops!","Something went wrong! Try again...");
                }
            }
        });
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
}
