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

        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wakelock.acquire(500);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "push message : " + remoteMessage.getData().toString());
        Log.d(TAG, "push message : " + remoteMessage.getData());


        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");

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
                .setAutoCancel(true);

        String bigtitle = "bigtitle";
        String bigtext = "bigtext";

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle(mBuilder)
                .bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.choi2)) //상단의 비트맵을 넣어준다.
                .setBigContentTitle( bigtitle ) //열렸을때의 타이틀
                .setSummaryText( bigtext /*getResources().getString( R.string.gcm_defaultSenderId)*/ ); //열렸을때의 Description

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());

    }

}
