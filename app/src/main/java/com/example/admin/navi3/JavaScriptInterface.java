package com.example.admin.navi3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by admin on 2016-12-06.
 */
public class JavaScriptInterface {
    private Context mContext;

    JavaScriptInterface(Context context) {
        mContext = context;
    }

    @JavascriptInterface
    public void showToast(String toast){

        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();

    }

    @JavascriptInterface
    public void callLoginActivity(){
        Intent intent = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(intent);
    }
}
