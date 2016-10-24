package com.developers.notes;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import static java.lang.System.currentTimeMillis;

/**
 * Created by Наталья on 23.10.2016.
 */

public class CreateNoti extends Service {
    private int i = 0;
    NotificationManager nm;
    String upNoti, notename;
    int kolvo;

    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        upNoti = intent.getStringExtra("upNoti");
        notename = intent.getStringExtra("notename");
        kolvo = intent.getIntExtra("kolvo" , 0);

        sendNotif();
        return super.onStartCommand(intent, flags, startId);
    }
    void sendNotif() {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("filename", upNoti);
        intent.putExtra("notename", notename);
        intent.putExtra("kolvo", kolvo);///////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notif = new NotificationCompat.Builder(this)
                .setContentTitle(upNoti)
                .setContentText("")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setCategory(Notification.CATEGORY_STATUS)
                .setShowWhen(true)
                .setWhen(currentTimeMillis())
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, notif);



        notif.flags |= Notification.FLAG_AUTO_CANCEL;

        nm.notify(1, notif);

        stopSelf();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}

