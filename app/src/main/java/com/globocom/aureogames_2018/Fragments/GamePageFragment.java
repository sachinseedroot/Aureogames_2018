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
import com.globocom.aureogames_2018.Constants;
import com.globocom.aureogames_2018.R;


public class GamePageFragment extends Fragment {
    private Context mcontext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mcontext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_page,container,false);
        return view ;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textView  =(TextView) view.findViewById(R.id.statusTV);
        textView.setText(Constants.ApplicationID);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
