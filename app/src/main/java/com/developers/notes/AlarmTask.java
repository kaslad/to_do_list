package com.developers.notes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Наталья on 23.10.2016.
 */

public class AlarmTask implements Runnable {
    // The date selected for the alarm
    private final Calendar date;
    // The android system alarm manager
    private final AlarmManager am;
    // Your context to retrieve the alarm manager from
    private final Context context;
    String upNoti;
    int unicId;

    public AlarmTask(Context context, Calendar date) {
        this.context = context;
        this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.date = date;

    }
    public void setText(String upNoti, int unicId) {
        this.upNoti = upNoti;
        this.unicId = unicId;
        Log.d("cot", "row inserted, ID = " + unicId + upNoti);
    }

    public void testing (String upNoti, int unicId){
        Intent intent = new Intent(context, CreateNoti.class);
        intent.putExtra("upNoti", upNoti);
        intent.putExtra("kolvo", unicId);
        PendingIntent pendingIntent = PendingIntent.getService(context, unicId, intent, 0);
    }

    @Override
    public void run() {


        // Request to start are service when the alarm date is upon us
        // We don't start an activity as we just want to pop up a notification into the system bar not a full activity
        Intent intent = new Intent(context, CreateNoti.class);
        intent.putExtra("upNoti", upNoti);
        intent.putExtra("kolvo", unicId);
        PendingIntent pendingIntent = PendingIntent.getService(context, unicId, intent, 0);

        // Sets an alarm - note this alarm will be lost if the phone is turned off and on again
        am.set(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(), pendingIntent);
    }
}
