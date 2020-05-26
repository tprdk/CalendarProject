package com.example.calendarproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Defaults defaults = SharedPref.getDefaults(context);

        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");

        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(title, content);

        notificationHelper.getManager().notify(1, nb.build());

        if(defaults.getHasAlertBox()){
            Intent mIntent = new Intent(context,AlertDialogActivity.class);
            mIntent.putExtra("title", title);
            mIntent.putExtra("content", content);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mIntent);
        }
    }
}
