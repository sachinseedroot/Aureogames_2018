package com.globocom.aureogames_2018.Fragments;


import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AppsFlyerLib;
import com.globocom.aureogames_2018.Activities.MainActivity;
import com.globocom.aureogames_2018.R;
import com.globocom.aureogames_2018.Utilities.AppSharedPrefSettings;
import com.globocom.aureogames_2018.Utilities.AppUtilities;
import com.globocom.aureogames_2018.Utilities.ConstantsKPI;
import com.globocom.aureogames_2018.Utilities.ConstantsValues;
import com.globocom.aureogames_2018.Utilities.TrackingConstants;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.globocom.aureogames_2018.Utilities.AppUtilities.extractOTP;
import static com.globocom.aureogames_2018.Utilities.AppUtilities.getIPAddress;

public class VerifyOtpFragment extends Fragment {
    private Context mcontext;
    private String msisdn = "";
    private String operator = "";
    private String CLI = "";
    private String msg = "";
    private EditText otp_ed;
    private boolean otpVerifyFlxag = true;

    public static VerifyOtpFragment newInstance(String msisdn, String operator) {
        VerifyOtpFragment f = new VerifyOtpFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString("msisdn", msisdn);
        args.putString("operator", operator);
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
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        otp_ed = (EditText) view.findViewById(R.id.txtPassword);

        TextView statusTV = (TextView) view.findViewById(R.id.statusTV);
        statusTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) mcontext).loadPage(3);
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


    private BroadcastReceiver smsOtpReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
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
                    AppUtilities.sendAnalytics(mcontext,
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
                    AppUtilities.sendAnalytics(mcontext,
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
                    AppUtilities.sendAnalytics(mcontext,
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
                    AppUtilities.sendAnalytics(mcontext,
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
                    AppUtilities.sendAnalytics(mcontext,
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
                        userDataObject.put("country", AppSharedPrefSettings.getIsUserCountryCode(mcontext));
                        userDataObject.put("kpi", "PRODUCT_PAGE_KPI");
                        userDataObject.put("carriername", "");

                        userDataObject.put("userAndroidID", AppSharedPrefSettings.getANDROID_ID(mcontext));
                        userDataObject.put("userUUID", AppSharedPrefSettings.getTrackingUuid(mcontext));
                        userDataObject.put("userReferrer", AppSharedPrefSettings.getAppsflyerReferrer(mcontext));
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                        String currentDateandTime = sdf.format(new Date());
                        userDataObject.put("userDateTime", currentDateandTime);
                        userDataObject.put("userHRLdata", "");
                        userDataObject.put("userIsFirstInstall", AppsFlyerLib.getInstance().isPreInstalledApp(context));
                        userDataObject.put("userIsSubscribed", true);
                        AppSharedPrefSettings.setuserDetailsJSON(mcontext, userDataObject.toString());


                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("----userDataObject-exe--- " + e);
                    }


                    // TODO: BUTTON HIDE
                   AppUtilities.showToastMsg(mcontext,"Congrats! Enjoy the game.");

                }
            } catch ( Exception e){
                e.printStackTrace();
                System.out.println("----smsOtpReceiver-exe--- " + e);
            }


        }
    };


    public void verifyOTP(String operator, String msisdn, String CLI) {
        //TODO: DU MOBILE SUBSCRIBER
        if (operator.trim().startsWith("du")) {

            operator = "du";
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
        } else if (operator.trim().startsWith("Etisalat")) {

            operator = "Etisalat";
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

}
