package com.example.icomman;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    IComman iComman;
    Handler handler = new Handler();

    private ServiceConnection serviceCon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            iComman = IComman.Stub.asInterface(iBinder);
            Log.i("AIDL", iComman.toString());
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i("AIDL ERROR", "Service not connected");
            iComman = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnBind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.example.myservices.AIDL");
                bindService(convertImplicitToExplicit(intent), serviceCon, Context.BIND_AUTO_CREATE);
            }
        });

        findViewById(R.id.btnCalculate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String res = "Calculation is - " + iComman.calculate(10, 20);
                    Toast.makeText(MainActivity.this, res, Toast.LENGTH_SHORT).show();
                    repeatNotification(res);
                    List<Person> personList = iComman.getPersonsList();
                    Log.i("FROM AIDL SERVER", personList.toString());
                    for (int i = 0; i <= personList.size() - 1 ; i++) {
                        Person person = personList.get(i);
                        Log.i("@FROM AIDL SERVER", "Name: " + person.getName() + ", Age: " + person.getAge());
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btnContacts).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ContactsActivity.class);
                startActivity(intent);
            }
        });

        setAlarm();
        checkPermission();

    }

    public void checkPermission() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 99:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void setAlarm() {

        Intent intent = new Intent(getApplicationContext(), MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), MyReceiver.REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstMillis = System.currentTimeMillis();
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, AlarmManager.INTERVAL_HALF_HOUR, pendingIntent);

    }

    public Intent convertImplicitToExplicit(Intent implicitIntent) {

        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentServices(implicitIntent, 0);
        if (resolveInfoList == null || resolveInfoList.size() != 1) {
            return null;
        }
        ResolveInfo resolveInfo = resolveInfoList.get(0);
        ComponentName componentName = new ComponentName(resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name);
        Intent explicitIntent = new Intent(implicitIntent);
        explicitIntent.setComponent(componentName);
        return explicitIntent;
    }

    public void createNotification(String str) {

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
                    .setContentText(str)
                    .setContentTitle("From AIDLServer")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .build();
            notificationManager.notify(0, notification);
        }

    }

    public void repeatNotification(final String str) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    createNotification(str);
                } catch (Exception e) {

                } finally {
                    handler.postDelayed(this, 10000);
                }
            }
        });
    }

}
