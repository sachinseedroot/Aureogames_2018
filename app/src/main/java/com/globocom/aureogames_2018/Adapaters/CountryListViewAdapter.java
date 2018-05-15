package com.globocom.aureogames_2018.Adapaters;


import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.globocom.aureogames_2018.Model.CountryCodeModel;
import com.globocom.aureogames_2018.R;
import com.globocom.aureogames_2018.Utilities.AppConstants;
import com.globocom.aureogames_2018.Utilities.AppUtilities;
import com.globocom.aureogames_2018.Utilities.CircleTransform;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CountryListViewAdapter extends BaseAdapter {

    private ArrayList<CountryCodeModel> countryCodeModels;
    private LayoutInflater inflater;
    private Context context;

    public CountryListViewAdapter(Context context, ArrayList<CountryCodeModel> images) {
        this.context = context;
        this.countryCodeModels = images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return countryCodeModels.size();
    }

    @Override
    public Object  getItem(int position) {
        return countryCodeModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.listview_item, parent, false);
        TextView textView =(TextView) convertView.findViewById(R.id.cnameTV);
        ImageView myImage = (ImageView) convertView
                .findViewById(R.id.flagIV);
        Picasso.get().load(AppUtilities.getFlagMasterResID(countryCodeModels.get(position).countryCode)).into(myImage);
        textView.setTypeface(AppUtilities.applyTypeFace(context, AppConstants.EgonSans_Light));
        textView.setText(countryCodeModels.get(position).countryName);

        RelativeLayout relativeLayout = (RelativeLayout) convertView.findViewById(R.id.selectCountryRel);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppConstants.COUNTRY_SELECT);
                intent.putExtra("cc",countryCodeModels.get(position).countryCode);
                context.sendBroadcast(intent);
            }
        });
        return convertView;
    }
}