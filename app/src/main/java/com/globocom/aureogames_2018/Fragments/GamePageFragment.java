package com.globocom.aureogames_2018.Fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.globocom.aureogames_2018.Activities.MainActivity;
import com.globocom.aureogames_2018.Constants;
import com.globocom.aureogames_2018.R;
import com.globocom.aureogames_2018.Utilities.ConstantsValues;

import java.util.HashMap;


public class GamePageFragment extends Fragment {
    private Context mcontext;
    private WebView webView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String url= ConstantsValues.GAMEZINE_PAGE;
    private HashMap<String, String> noCacheHeaders;

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


        webView = (WebView) view.findViewById(R.id.webView);
        webView.loadUrl("http://www.google.com");
        WebSettings webSettings = webView.getSettings();
        webSettings.setAppCacheEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                if (newProgress == 0)
                    mSwipeRefreshLayout.setRefreshing(true);
                else if (newProgress == 100)
                    mSwipeRefreshLayout.setRefreshing(false);

            }
        });

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                GamePageFragment.this.url = url;
                view.loadUrl(url, noCacheHeaders);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if(view.getTitle() != null && view.getTitle().length() > 0){
                    System.out.println("-----page--title------- "+view.getTitle());
                }
            }
        });



        noCacheHeaders = new HashMap<>(2);
        noCacheHeaders.put("Pragma", "no-cache");
        noCacheHeaders.put("Cache-Control", "no-cache");


        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.black));
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (url != null) {
                    webView.loadUrl(url, noCacheHeaders);
                }else{
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });

    }
}
