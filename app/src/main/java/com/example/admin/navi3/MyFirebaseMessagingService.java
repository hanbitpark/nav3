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
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessingService";
//    Context mContext = getApplicationContext();


    @Override
    public void sendBroadcast(Intent intent) {
        super.sendBroadcast(intent);
        Log.d(TAG, "sendBroadcast실행");
    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
    }

    public MyFirebaseMessagingService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate실행");
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

//        try {
//            JSONObject obj = new JSONObject(messageBody);
//            imgUrl = obj.getString("imgUrl");
//        }catch(Exception e){
//
//        }
//
//        if(imgUrl != "") {
//            try {
//                URL url = new URL(imgUrl);
//                URLConnection conn = url.openConnection();
//                conn.connect();
//
//                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
//                Bitmap img = BitmapFactory.decodeStream(bis);
//
//                NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle(notificationBuilder);
//                style.bigPicture(img).setBigContentTitle(title);
//            } catch (Exception e) {
//
//            }
//        }


        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");

        Log.d("title", title);
        Log.d("mesage", message);

        System.out.println("received message : " + message);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.choi22);

        Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.choi11)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.choi22) )
                .setTicker("미리보기")
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
//                .setWhen(System.currentTimeMillis())
//                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(pendingIntent);

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
