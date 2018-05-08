package com.globocom.aureogames_2018.Activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.globocom.aureogames_2018.Fragments.EnterNumberFragment;
import com.globocom.aureogames_2018.R;

public class MainActivity extends AppCompatActivity {

    private FrameLayout frameLayoutContainer;
    private Context mcontext;
    public static final int PERMISSION_ALL=99;
    private Button permissionTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("-------------init-----main-activity");
        setContentView(R.layout.activity_main);
        mcontext=this;

        init();
        getPermissionStatusAndWifiStatus();
    }

    public void init() {
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
                Manifest.permission.ACCESS_NETWORK_STATE
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            System.out.println("-----permission NOT Granted-------");
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
            //MOVE AHEAD
            loadScreenOne();
            System.out.println("-----permission Granted-------");
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
        permissionTV.setVisibility(View.GONE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(frameLayoutContainer.getId(), new EnterNumberFragment());
        ft.commitAllowingStateLoss();
    }














    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
                        loadScreenOne();
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
}
