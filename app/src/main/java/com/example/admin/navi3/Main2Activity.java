package com.example.admin.navi3;

import android.app.Notification;
import android.app.NotificationManager;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


    }

    public void button(View v){
        String title = "nTitle";
        String message = "nMessage";

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
