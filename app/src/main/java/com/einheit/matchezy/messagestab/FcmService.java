package com.einheit.matchezy.messagestab;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.einheit.matchezy.login.Login;
import com.einheit.matchezy.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

/*
 Created by hari on 16/6/18.
*/


public class FcmService extends FirebaseMessagingService {

    NotificationManager notificationManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            throughChannels(remoteMessage);
        }

        int notificationId = new Random().nextInt(1001);

        Intent intent = new Intent(this, Login.class);

        intent.setAction(Long.toString(System.currentTimeMillis()));

        if(remoteMessage.getData().get("intent").equals("chatPage")) {
            intent.putExtra("notify", "chat");
        } else if (remoteMessage.getData().get("intent").equals("chat")) {
            intent.putExtra("notify", "like");
        }

        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "matchezy_1001")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("message"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getData().get("message")))
                .setLights(Color.RED, 3000, 3000)
                .setAutoCancel(true)
                .setGroup(remoteMessage.getData().get("title"))
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        notificationManager.notify(notificationId, notificationBuilder.build());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void throughChannels(RemoteMessage remoteMessage){
        CharSequence adminChannelName = "matchezy";
        String adminChannelDescription = "matchezy channel";

        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel("matchezy_1001", adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        adminChannel.setSound(defaultSoundUri, attributes);
        adminChannel.setGroup(remoteMessage.getData().get("title"));
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }
}
