package com.globocom.aureogames_2018.Fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.globocom.aureogames_2018.Activities.MainActivity;
import com.globocom.aureogames_2018.R;
import com.globocom.aureogames_2018.Utilities.AppSharedPrefSettings;

public class EnterNumberFragment extends Fragment {
    private Context mcontext;
    private EditText phonenoTV;
    private TextView countryCodeTV;
    private Button submitBTN;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mcontext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enter_number, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        loadDATA();

    }

    public void init(View view) {
        phonenoTV = (EditText) view.findViewById(R.id.phonenoTV);
        countryCodeTV = (TextView) view.findViewById(R.id.countryCodeTV);
        submitBTN = (Button) view.findViewById(R.id.submitBTN);

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) mcontext).loadPage(2);
            }
        });
    }
    public void loadDATA(){

    }
}
