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
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

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

        Log.d(TAG, "push message : " + remoteMessage.getData().toString());
        Log.d(TAG, "push message : " + remoteMessage.getData());


        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");

//        Log.d("title", title);
//        Log.d("mesage", message);

        Context mContext = getApplicationContext();
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.activity_intro);


//        remoteViews.setTextViewText();
        System.out.println("received message : " + message);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.choi22);

        Notification.Builder notificationBuilder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.choi11)
                    //setvisibility 잠금화면에서 보여주기 public 전체콘텐츠보여주기, secret 어떤부분도 안보여줌 private, 기본정보만 보여줌
                    .setVisibility(android.support.v7.app.NotificationCompat.VISIBILITY_PUBLIC)
    //                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.choi22) )
                    .setTicker("미리보기")
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setVibrate(new long[] {500,100,500,100})
                    .setSound(defaultSoundUri)
//                    .setCustomContentView(remoteViews)
    //                .setWhen(System.currentTimeMillis())
    //                .setPriority(Notification.PRIORITY_MAX)
                    .setContentIntent(pendingIntent);
        }

        notificationBuilder.setStyle(new Notification.BigPictureStyle()
                .bigPicture(bitmap)
                .setSummaryText(message)
                .setBigContentTitle(title));

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wakelock.acquire(5000);

        notificationManager.notify(999 /* ID of notification */, notificationBuilder.build());


    }

}
