package com.example.admin.navi3;

import android.content.Context;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by admin on 2016-12-08.
 */

public class myWebChromeClient extends WebChromeClient {

    private ContentLoadingProgressBar progressBar;

    Context mContext;


    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);

        progressBar.setProgress(newProgress);
        if (newProgress <= 99) {
            progressBar.setVisibility(View.VISIBLE);
        } else if(newProgress == 100){
            progressBar.setVisibility(View.GONE);
        }
    }
}
