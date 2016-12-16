package com.example.admin.navi3;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyFirebaseInstaceIDService extends FirebaseInstanceIdService {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public String url = "http://rukusmin.ppuang.co.kr/fcm/registor.php";

    //토큰이 변경될때 값 다시 받기
    private static final String TAG = MyFirebaseInstaceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "refreshedToken : "+ refreshedToken);


//        new HttpURL().execute(refreshedToken);
//        new OKHTTP3().RegistrationToServer(refreshedToken);
//        HttpURL url = new HttpURL();
//        url.execute(refreshedToken);

//        //OKHTTP3통신
//        sendRegistrationToServer(refreshedToken);

        //push 메세지 -> 브로드 캐스트로 전달
//        Intent registrationComplete = new Intent("registrationComplete");
//        registrationComplete.putExtra("token", refreshedToken);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(String refreshedToken){
        Log.e(TAG, "sendRegistrationToServer : "+ refreshedToken);

        OkHttpClient client = new OkHttpClient();

        JSONObject json = new JSONObject();

        try {

            json.put("Token", refreshedToken);

            Log.d("json", json.toString());

            RequestBody body = RequestBody.create(JSON, json.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();

            Log.d("reponse", response.toString());
//            response.body().string();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("E1", e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("E2", e.toString());
        }
    }
}
