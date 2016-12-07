package com.example.admin.navi3;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by admin on 2016-11-29.
 */

public class CloseBackPressed_Handler {

    private long BackKeyPressedTime = 0;
    private Activity activity;
    private Toast toast;

    public CloseBackPressed_Handler(Activity activity) {
        this.activity = activity;
    }

    public void onBackPressed(){
        if(System.currentTimeMillis() > BackKeyPressedTime + 2000){
            BackKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(activity, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();

        } else if(System.currentTimeMillis() <= BackKeyPressedTime + 2000){
            activity.finish();
        }
    }
}

