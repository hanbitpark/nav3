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

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        mBuilder
                .setSmallIcon(R.drawable.ic_alarm_on_black_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_logo))
                .setAutoCancel(true)
                .setContentTitle(title)
                .setPriority(Notification.PRIORITY_MAX)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message);

        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());

    }
}
