package com.example.admin.navi3;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessingService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate실행");



    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        //화면 깨우기
        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wakelock.acquire(500);

        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");
        String imgUrl = remoteMessage.getData().get("imgUrl");
        String linkUrl = remoteMessage.getData().get("linkUrl");

        Log.d(TAG, "title : " + title);
        Log.d(TAG, "message : " + message);
        Log.d(TAG, "imgUrl : " + imgUrl);
        Log.d(TAG, "linkUrl : "  + linkUrl);

        Bitmap bitmap = imgUrl(imgUrl);


        //메세지 클릭후 원하는 페이지로 이동
        Intent intent = new Intent(this, MainActivity.class);
        //링크 주소넣기
        Bundle bundle = new Bundle();
        bundle.putString("url", linkUrl);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_alarm_on_black_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_logo))
                .setAutoCancel(true)
                .setContentTitle(title)
                .setPriority(Notification.PRIORITY_MAX)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                // 0-> waitTime, 1000->vibrateTime
                .setVibrate(new long[]{0,1000})
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        String bigTexttitle = "bigtitle";
        String bigText2 = "bigtextbigtextbigtextbigtextbigtextbigtextbigtextbigtextbigtextbigtext";

//        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle(mBuilder)
//                .setBigContentTitle(bigTexttitle)
//                .bigText(bigText2);


        String bigtitle = "bigtitle";
        String bigtext = "bigtext";

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle(mBuilder)
//                .bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.choi2)) //상단의 비트맵을 넣어준다.
                .bigPicture(bitmap)
                .setBigContentTitle( bigtitle ) //열렸을때의 타이틀
                .setSummaryText( bigtext /*getResources().getString( R.string.gcm_defaultSenderId)*/ ); //열렸을때의 Description

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());

    }


    private Bitmap imgUrl(String imgUrl){
        Bitmap bitmap;

        try{
            URL url = new URL(imgUrl);
            URLConnection conn = url.openConnection();
            conn.connect();

            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            bitmap = BitmapFactory.decodeStream(bis);

            return bitmap;

        }catch (Exception e){
            Log.d(TAG, "imgUrl : " + e);
            return bitmap = null;
        }

    }

}
