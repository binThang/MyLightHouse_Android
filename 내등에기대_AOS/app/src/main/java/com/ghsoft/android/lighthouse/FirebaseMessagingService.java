package com.ghsoft.android.lighthouse;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import java.net.URL;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService
{
    private static final String TAG = "FirebaseMsgService";
    Bitmap bigPicture;
    String img = "", type = "", link = "", idxx = "";
    String nickname;

    SharedPreferences pref;
    String push;

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        nickname = "내 등에 기대";

        pref = getSharedPreferences("push", MODE_PRIVATE);
        push = pref.getString("onoff", "0");

        img += remoteMessage.getData().get("image");
        type += remoteMessage.getData().get("type");
        link += remoteMessage.getData().get("link");
        idxx += remoteMessage.getData().get("idx");

        Log.d("푸시알람", link);
        Log.d("푸시알람1", idxx);

        //img는 http로 받아옴
        //안거르면 둘 다 실행됨(오류)
        if (type.equals("image"))
        {
            //img 추가-메세지와 이미지를 인수로 지정
            sendNotificationImg(remoteMessage.getData().get("text"), remoteMessage.getData().get("image"));
        }
        else
        {
            sendNotification(remoteMessage.getData().get("text"));
        }
    }

    //            messageBody를 핸드폰 상단 화면에 띄움
    //메세지만
    private void sendNotification(String messageBody)
    {
        Intent intent = new Intent(this, MainActivity.class).putExtra("pushlink", link).putExtra("pushidx", idxx);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        if (push.equals("0"))
        {
            Log.d("진동1", push);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder;
            if (Build.VERSION.SDK_INT >= 26)
            {
                NotificationChannel channel = new NotificationChannel("lighthouse", "lighthouse", NotificationManager.IMPORTANCE_DEFAULT );
                manager.createNotificationChannel(channel);
                builder = new NotificationCompat.Builder(this, channel.getId());
            }
            else
            {
                builder = new NotificationCompat.Builder(this);
            }

            builder
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setContentTitle(nickname)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .setBigContentTitle(nickname)
                            .bigText(messageBody))
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setVibrate(null)
                    .setSound(null);
            builder.setVibrate(null);
            builder.setSound(null);
            builder.setContentIntent(pendingIntent);   //PendingIntent 설정
            builder.setAutoCancel(true);
            Notification notification = builder.build();    //Notification 객체 생성
            manager.notify(0, notification);    //NotificationManager가 알림(Notification)을 표시, id는 알림구분용
        }
        else
        {
            Log.d("진동2", push);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder;
            if (Build.VERSION.SDK_INT >= 26)
            {
                NotificationChannel channel = new NotificationChannel("lighthouse", "lighthouse", NotificationManager.IMPORTANCE_DEFAULT );
                manager.createNotificationChannel(channel);
                builder = new NotificationCompat.Builder(this, channel.getId());
            }
            else
            {
                builder = new NotificationCompat.Builder(this);
            }

            builder
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setContentTitle(nickname)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .setBigContentTitle(nickname)
                            .bigText(messageBody))
                    .setContentText(messageBody)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{0, 500})
                    .setContentIntent(pendingIntent);

            manager.notify(
                    0, builder.build());

        }
    }

    //이미지도
    private void sendNotificationImg(String messageBody, String myimgurl)
    {
        Intent intent = new Intent(this, MainActivity.class).putExtra("pushlink", link).putExtra("pushidx", idxx);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        try
        {
            URL url = new URL(myimgurl);
            bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.d("진동1", push);

        if (push.equals("0"))
        {
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder builder;
            if (Build.VERSION.SDK_INT >= 26)
            {
                NotificationChannel channel = new NotificationChannel("lighthouse", "lighthouse", NotificationManager.IMPORTANCE_DEFAULT );
                manager.createNotificationChannel(channel);
                builder = new NotificationCompat.Builder(this, channel.getId());
            }
            else
            {
                builder = new NotificationCompat.Builder(this);
            }

            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setContentTitle(nickname)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(bigPicture)
                            .setBigContentTitle(nickname)
                            .setSummaryText(messageBody))
                    .setAutoCancel(true)
                    .setVibrate(null)
                    .setSound(null);
            builder.setVibrate(null);
            builder.setSound(null);
            builder.setContentIntent(pendingIntent);   //PendingIntent 설정
            builder.setAutoCancel(true);
            Notification notification = builder.build();    //Notification 객체 생성
            manager.notify(0, notification);    //NotificationManager가 알림(Notification)을 표시, id는 알림구분용
        }
        else
        {
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder;
            if (Build.VERSION.SDK_INT >= 26)
            {
                NotificationChannel channel = new NotificationChannel("lighthouse", "lighthouse", NotificationManager.IMPORTANCE_DEFAULT );
                manager.createNotificationChannel(channel);
                builder = new NotificationCompat.Builder(this, channel.getId());
            }
            else
            {
                builder = new NotificationCompat.Builder(this);
            }

            builder
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setContentTitle(nickname)
                    .setContentText("알림을 확인해 주세요.")
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(bigPicture)
                            .setBigContentTitle(nickname)
                            .setSummaryText(messageBody))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0 /* ID of notification */, builder.build());
        }
    }
}
