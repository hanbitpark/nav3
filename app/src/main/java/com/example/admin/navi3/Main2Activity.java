package com.example.admin.navi3;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

public class Main2Activity extends AppCompatActivity {

    private static final String TAG = Main2Activity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public void button(View v){
        String title = "nTitle";
        String message = "nMessage";
//        String imgUrl = "https://i.ytimg.com/vi/B850LHUgKV4/hqdefault.jpg";
        String link = "http://ppuang.co.kr/bbs.php?type=star";
//        Bitmap bitmap = imgUrl(String imgUrl);

        //메시지 클릭후 이동할 주소
        Intent intent = new Intent(this, MainActivity.class);
        //링크 번들로 값 넣었음
        Bundle bundle = new Bundle();
        bundle.putString("url", link);
        intent.putExtras(bundle);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

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


        String bigtext2 = "bigtextTitle";
        String bigtext3 = "bigtextbigtextbigtextbigtextbigtextbigtextbigtextbigtextbigtextbigtext";

//        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle(mBuilder)
//                .setBigContentTitle(bigtext2)
//                .bigText(bigtext3);


        String bigtitle = "bigtitle";
        String bigtext = "bigtext";

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle(mBuilder)
//                .bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.choi2)) //상단의 비트맵을 넣어준다.
                .bigLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.choi2))
                .bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.choi2))
                .setBigContentTitle( bigtitle ) //열렸을때의 타이틀
                .setSummaryText( bigtext /*getResources().getString( R.string.gcm_defaultSenderId)*/ ); //열렸을때의 Description

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());

    }

    public class imgUrl extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] params) {
            return null;
        }
    }
}
