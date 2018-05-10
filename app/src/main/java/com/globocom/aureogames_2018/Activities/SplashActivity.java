package com.globocom.aureogames_2018.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.globocom.aureogames_2018.R;
import com.globocom.aureogames_2018.Utilities.AppUtilities;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        if(AppUtilities.isNetworkAvailable(this)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.enter_from_right, R.anim.hold);

                    finish();
                }
            }, SPLASH_TIME_OUT);


            AppUtilities.sendAnalytics(this, "splashscreen", "launch", "view", "app_launched","","",
                    "","",0,0,"","","","");

        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please check your internet connection and try again!")
                    .setCancelable(false)
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finishAffinity();
                        }
                    });


            //Creating dialog box
            AlertDialog alert = builder.create();
            alert.setTitle("No internet!");
            alert.show();
        }
    }
}
