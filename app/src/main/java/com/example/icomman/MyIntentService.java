package com.example.icomman;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService extends IntentService {

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("MyIntentService", "Service running");
        createNotification();
    }

    public void createNotification() {
        CharSequence channel_name = "channel_name";
        String channel_id = "channel_id";
        int importance = 0;

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            importance = NotificationManager.IMPORTANCE_HIGH;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(channel_id, channel_name, importance);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Notification notification = new Notification.Builder(this, channel_id)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Hello From BoundAIDLClient")
                    .setContentText("Reminder that AlarmManager is working")
                    .build();
            notificationManager.notify(11, notification);
        }


    }

}
