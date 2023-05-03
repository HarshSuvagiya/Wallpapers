package com.scorpion.splashwalls.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.preference.PreferenceManager;

import com.scorpion.splashwalls.services.MyService;

public class CallBroadCastReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
//        Log.e("handler","121212");
//        C1822L.m696i("CallBroadCastReceiver", "CallBroadCastReceiver onReceive()");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        Log.e("GETBOOLEAN", String.valueOf(prefs.getBoolean("auto_wallpaper", false)));

        if (prefs.getBoolean("auto_wallpaper", false)){
            Intent intent2 = new Intent(context, MyService.class);
            if (Build.VERSION.SDK_INT >= 26) {
                context.startForegroundService(intent2);
            } else {
                context.startService(intent2);
            }
        }
    }
}
