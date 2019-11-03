package com.example.icomman;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {

    public static final int REQ_CODE = 123;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, MyIntentService.class);
        intent1.putExtra("foo", "bar");
        context.startService(intent1);
    }
}
