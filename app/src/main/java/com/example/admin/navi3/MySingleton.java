package com.example.admin.navi3;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by admin on 2016-11-27.
 */

public class MySingleton {
    private static MySingleton mInstance;
    private RequestQueue requestQueue;
    private static Context mContext;


    public MySingleton(Context context) {
        this.mContext = context;
        this.requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized MySingleton getmInstance(Context context){
        if(mInstance==null){
            mInstance = new MySingleton(context);
        }

        return mInstance;
    }

    public <T>void addToRequestque(Request<T> request){
        requestQueue.add(request);
    }
}
