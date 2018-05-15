package com.globocom.aureogames_2018.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AppsFlyerLib;
import com.appsflyer.AppsFlyerProperties;
import com.globocom.aureogames_2018.Activities.MainActivity;
import com.globocom.aureogames_2018.Adapaters.CountryListViewAdapter;
import com.globocom.aureogames_2018.Constants;
import com.globocom.aureogames_2018.Model.CountryCodeModel;
import com.globocom.aureogames_2018.R;
import com.globocom.aureogames_2018.Utilities.AppConstants;
import com.globocom.aureogames_2018.Utilities.AppSharedPrefSettings;
import com.globocom.aureogames_2018.Utilities.AppUtilities;
import com.globocom.aureogames_2018.Utilities.ConstantsKPI;
import com.globocom.aureogames_2018.Utilities.ConstantsValues;
import com.globocom.aureogames_2018.Utilities.TrackingConstants;
import com.globocom.aureogames_2018.Utilities.ValidationUtils;
import com.globocom.aureogames_2018.Volley.VolleyObject;
import com.globocom.aureogames_2018.Volley.VolleyResponseInterface;

import java.util.ArrayList;
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
    private MyDialogFragment dialog;


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
        IntentFilter intentFilter = new IntentFilter(AppConstants.COUNTRY_SELECT);
        mcontext.registerReceiver(countryReceiver,intentFilter);
        VolleyObject.initSDK(mcontext);
        volleyObject = new VolleyObject();
        initProgressbar();
        phonenoTV = (EditText) view.findViewById(R.id.phonenoTV);
        phonenoTV.setHint("enter mobile number");
        phonenoTV.setTypeface(AppUtilities.applyTypeFace(mcontext,AppConstants.EgonSans_Light));
        countryCodeTV = (TextView) view.findViewById(R.id.countryCodeTV);
        submitBTN = (Button) view.findViewById(R.id.submitBTN);
        TextView statusTV = (TextView) view.findViewById(R.id.statusTV);
        statusTV.setTypeface(AppUtilities.applyTypeFace(mcontext,AppConstants.EgonSans_Bold));
        TextView otpdetailsTV = (TextView) view.findViewById(R.id.otpdetailsTV);
        otpdetailsTV.setTypeface(AppUtilities.applyTypeFace(mcontext,AppConstants.EgonSans_Light));

        submitBTN.setTypeface(AppUtilities.applyTypeFace(mcontext,AppConstants.EgonSans_Light));

        if(!TextUtils.isEmpty(msisdnfromsim)){
            phonenoTV.setText(msisdnfromsim);
        }
        if(!TextUtils.isEmpty(countryCode)){
            CountryCodeModel countryCodeModel = AppUtilities.getCountryCodeModelByName(countryCode);
            if(countryCodeModel!=null && !TextUtils.isEmpty(countryCodeModel.countryDialCode)) {
                countryCodeTV.setText(countryCodeModel.countryDialCode);
                countryCodeTV.setCompoundDrawablesWithIntrinsicBounds(AppUtilities.getFlagMasterResID(countryCodeModel.countryCode.toLowerCase().trim()), 0, 0, 0);
                phonenoTV.setText(msisdnfromsim.replace(countryCodeModel.countryDialCode,""));
            }
        }
        countryCodeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 dialog =new  MyDialogFragment();
                dialog.show(getFragmentManager(), "dialog");
            }
        });

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View views = getActivity().getCurrentFocus();
                if (views != null) {
                    InputMethodManager imm = (InputMethodManager) mcontext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(views.getWindowToken(), 0);
                }
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
            msisdn = ValidationUtils.validedMobileNumber(countryCodeTV.getText().toString().trim()+phonenoTV.getText().toString().trim());
            mobVersion = android.os.Build.VERSION.SDK_INT;

            try {
                sendAnalytics_MOBILE_SUBMIT_KPI();
                if (ValidationUtils.checkValidMobileNumber(msisdn)) {
                    System.out.println("--------------OTP SENT HERE-------------");
                    sendOTP();                                    // remove comment after for testing
//                    resultDATA("du ,SUBSCRIBERSTATUS_CONNECTED"); //for testing

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
        showProgress("Loading, please wait...");
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
      //  mProgressDialog.setMessage("Loading, please wait...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    public void showProgress(String msg) {
        try {
            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.setMessage(msg);
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
      //  String urls = "http://globobill.com/globobill/sendSmsToUser/919922622444/5020";
        showProgress("Sending OTP code, please wait...");
        volleyObject.fetchDatabyUrlString(url, new VolleyResponseInterface() {
            @Override
            public void onResponse(String response, VolleyError volleyError) {
                stopProgress();
                if (volleyError == null && !TextUtils.isEmpty(response) && !response.equalsIgnoreCase("false")) {

                    System.out.println("----sendOTPfromOperator cal url---- " + url);
                    System.out.println("----sendOTPfromOperator cal response---- " + response);
                    AppSharedPrefSettings.setIsUserCountryCode(mcontext,countryCode);
                    ((MainActivity) mcontext).loadScreenTwo(msisdn, operator,url);

                    AppUtilities.sendAnalytics(mcontext,
                            "enter_number_screen",
                            "screen_one",
                            "otp_sent_success", ConstantsKPI.OTP_SENT_SUCCESS,
                            ""
                            , "18",
                            ConstantsKPI.OTP_SENT_SUCCESS,
                            msisdn,
                            18,
                            TrackingConstants.eventServiceMap.get(18),
                            "", "", "", "");

                } else {
                    System.out.println("----sendOTPfromOperator cal exception---- " + volleyError.getMessage());
                    AppUtilities.showAlertDialog(mcontext,"Oops!","Something went wrong! Try again...");
                    AppUtilities.sendAnalytics(mcontext,
                            "enter_number_screen",
                            "screen_one",
                            "otp_sent_failed", ConstantsKPI.OTP_SENT_FAILED,
                            ""
                            , "17",
                            ConstantsKPI.OTP_SENT_FAILED,
                           msisdn,
                            17,
                            TrackingConstants.eventServiceMap.get(17),
                            "", "", "", "");
                }
            }
        });
    }

    public static class MyDialogFragment extends DialogFragment
    {
        //private View pic;

        public MyDialogFragment()
        {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_my_dialog, new LinearLayout(getActivity()), false);

            // Retrieve layout elements
            TextView title = (TextView) view.findViewById(R.id.selectCountryTV);
            title.setTypeface(AppUtilities.applyTypeFace(getActivity(), AppConstants.EgonSans_Bold));


            ListView listView = (ListView) view.findViewById(R.id.countryListview);
            CountryListViewAdapter countryListViewAdapter = new CountryListViewAdapter(getActivity(),AppUtilities.getCountryCodeModelArrayByName());
            listView.setAdapter(countryListViewAdapter);
            // Set values
       //     title.setText("Not perfect yet");

            // Build dialog
            Dialog builder = new Dialog(getActivity());
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
            builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            builder.setContentView(view);
            return builder;
        }
    }

    public BroadcastReceiver countryReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(dialog!=null){
                dialog.dismiss();
            }
            if(context!=null && intent !=null && intent.hasExtra("cc")){
                String cc = intent.getStringExtra("cc");
                if(!TextUtils.isEmpty(cc)){
                    CountryCodeModel countryCodeModel = AppUtilities.getCountryCodeModelByName(cc);
                    if(countryCodeModel!=null && !TextUtils.isEmpty(countryCodeModel.countryDialCode)) {
                        countryCodeTV.setText(countryCodeModel.countryDialCode);
                        countryCodeTV.setCompoundDrawablesWithIntrinsicBounds(AppUtilities.getFlagMasterResID(countryCodeModel.countryCode.toLowerCase().trim()), 0, 0, 0);
                    }
                }
            }
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        try{
            mcontext.unregisterReceiver(countryReceiver);
        }catch (Exception e){e.printStackTrace();}
    }
}
