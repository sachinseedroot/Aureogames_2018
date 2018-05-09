package com.globocom.aureogames_2018.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.chartboost.sdk.Chartboost;
import com.globocom.aureogames_2018.Constants;
import com.globocom.aureogames_2018.Controller.MainApplication;
import com.globocom.aureogames_2018.Fragments.EnterNumberFragment;
import com.globocom.aureogames_2018.Fragments.GamePageFragment;
import com.globocom.aureogames_2018.Fragments.VerifyOtpFragment;
import com.globocom.aureogames_2018.R;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;
import java.util.Locale;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private FrameLayout frameLayoutContainer;
    private Context mcontext;
    public static final int PERMISSION_ALL = 99;
    private Button permissionTV;
    private String mobileNo;
    private String countryCode;
    private String carrierName;
    private Stack<Fragment> fragmentStack;
    private EnterNumberFragment enterNumberFragment;
    private VerifyOtpFragment verifyOtpFragment;
    private GamePageFragment gamePageFragment;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mcontext = this;


        //chartboost initialization
        try {
            Chartboost.startWithAppId(this, Constants.CHARTBOOST_appId, Constants.CHARTBOOST_appSignature);
            Chartboost.onCreate(this);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("-chartboost--- " + e);
        }

        init();
        getPermissionStatusAndWifiStatus();


    }

    public void init() {

        fragmentStack = new Stack<Fragment>();

        if (MainApplication.firebaseAnalytics == null) {
            MainApplication.firebaseAnalytics = FirebaseAnalytics.getInstance(mcontext);
        }
        frameLayoutContainer = (FrameLayout) findViewById(R.id.container);
        permissionTV = (Button) findViewById(R.id.permissionTV);
        permissionTV.setVisibility(View.GONE);
        permissionTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPermissionStatusAndWifiStatus();
            }
        });
    }

    public void getPermissionStatusAndWifiStatus() {
        String[] PERMISSIONS = {
                Manifest.permission.READ_SMS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.READ_PHONE_STATE
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
            //MOVE AHEAD
            permissionTV.setVisibility(View.GONE);
            verifyUser();
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    System.out.println("-----permissionDenied------- " + permission.toString());
                    return false;
                }
            }
        }
        return true;
    }

    public void loadScreenOne() {


        if (enterNumberFragment == null)
            enterNumberFragment = new EnterNumberFragment();

        if (enterNumberFragment.isAdded()) {
            return;
        }

        fragmentStack.clear();


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slideinright, R.anim.slideoutleft);
        ft.add(frameLayoutContainer.getId(), enterNumberFragment);
        if (fragmentStack.size() > 0) {
            fragmentStack.lastElement().onPause();
            ft.hide(fragmentStack.lastElement());
        }
        fragmentStack.push(enterNumberFragment);
        ft.commitAllowingStateLoss();


    }


    public void loadScreenTwo() {
        if (verifyOtpFragment == null)
            verifyOtpFragment = new VerifyOtpFragment();

        if (verifyOtpFragment.isAdded()) {
            return;
        }


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slideinright, R.anim.slideoutleft);
        ft.add(frameLayoutContainer.getId(), verifyOtpFragment);
        if (fragmentStack.size() > 0) {
            fragmentStack.lastElement().onPause();
            ft.hide(fragmentStack.lastElement());
        }
        fragmentStack.push(verifyOtpFragment);
        ft.commitAllowingStateLoss();
    }

    public void loadScreenThree() {
        if (gamePageFragment == null)
            gamePageFragment = new GamePageFragment();

        if (gamePageFragment.isAdded()) {
            return;
        }


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slideinright, R.anim.slideoutleft);
        ft.add(frameLayoutContainer.getId(), gamePageFragment);
        if (fragmentStack.size() > 0) {
            fragmentStack.lastElement().onPause();
            ft.hide(fragmentStack.lastElement());
        }
        fragmentStack.push(gamePageFragment);
        ft.commitAllowingStateLoss();
    }

    public void loadPage(int pagenumber) {
        if (pagenumber == 1) {

            loadScreenOne();

        } else if (pagenumber == 2) {

            loadScreenTwo();

        } else if (pagenumber == 3) {

            loadScreenThree();

        } else {

            loadScreenOne();
        }
    }


    public void verifyUser() {
        countryCode = getCountryBasedOnSimCardOrNetwork(mcontext);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<SubscriptionInfo> subscription = SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoList();
            for (int i = 0; i < subscription.size(); i++) {
                SubscriptionInfo info = subscription.get(i);
                System.out.println("-code-number--- " + info.getNumber());
                System.out.println("-code-network-name-- " + info.getCarrierName());
                if (!TextUtils.isEmpty(info.getCountryIso())) {
                    countryCode = info.getCountryIso();
                }
            }
        }

        System.out.println("-validation--countrycode--- " + countryCode);

        if (!Constants.COUNTRY_CODE.equalsIgnoreCase(countryCode)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(mcontext.getResources().getString(R.string.fullversionnoavailable))
                    .setCancelable(false)
                    .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finishAffinity();
                        }
                    });


            //Creating dialog box
            AlertDialog alert = builder.create();
            alert.setTitle("Information");
            alert.show();
        } else {
            loadPage(1);
        }
    }


    private static String getCountryBasedOnSimCardOrNetwork(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_ALL: {

                if (grantResults.length > 0) {
                    boolean permissionGranted = true;
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            permissionGranted = false;
                        }
                    }
                    if (permissionGranted) {
                        permissionTV.setVisibility(View.GONE);
                        verifyUser();
                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        //MOVE AHEAD
                    } else {
                        permissionTV.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    permissionTV.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Chartboost.onStart(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Chartboost.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Chartboost.onPause(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        Chartboost.onStop(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Chartboost.onDestroy(this);
    }

    @Override
    public void onBackPressed() {
        // If an interstitial is on screen, close it.
        if (Chartboost.onBackPressed()) {
            return;
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }


            if (fragmentStack.size() > 1) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment fragment = fragmentStack.pop();


                ft.setCustomAnimations(R.anim.slideinleft, R.anim.slideoutright);

                Fragment lastFragment = fragmentStack.lastElement();

                lastFragment.onPause();
                ft.remove(fragment);
                lastFragment.onResume();
                ft.show(lastFragment);
                ft.commit();
            } else {

                if (doubleBackToExitPressedOnce) {

                    super.onBackPressed();
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(mcontext, "Click again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        }
    }
}
