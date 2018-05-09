package com.globocom.aureogames_2018.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.globocom.aureogames_2018.Activities.MainActivity;
import com.globocom.aureogames_2018.R;

public class VerifyOtpFragment extends Fragment {
    private Context mcontext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mcontext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verify_otp, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView statusTV = (TextView) view.findViewById(R.id.statusTV);
        statusTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) mcontext).loadPage(3);
            }
        });
    }
}
