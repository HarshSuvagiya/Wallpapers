package com.scorpion.splashwalls.services;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.IBinder;
import android.app.Notification.Builder;
import android.os.StrictMode;
import android.os.SystemClock;
//import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scorpion.splashwalls.R;
import com.scorpion.splashwalls.api.WallpaperApi;
import com.scorpion.splashwalls.api.WallpaperService;
import com.scorpion.splashwalls.models.AutoWallpaperHistoryModel;
import com.scorpion.splashwalls.models.ImageDetailModel;
import com.scorpion.splashwalls.receivers.CallBroadCastReceiver;
import com.scorpion.splashwalls.utils.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyService extends Service {
    private WallpaperService imageDetailService;
    ImageDetailModel imageDetailModel;
    int flag = 0;
    int MINUTES = 15; // The delay in minutes
    String auto_wallpaper_category;
    SharedPreferences shref;
    SharedPreferences.Editor editor;
    String auto_wallpaper_custom_category;
    AutoWallpaperHistoryModel autoWallpaperHistoryModel;
    ArrayList<AutoWallpaperHistoryModel> autoWallpaperHistoryModelArrayList = new ArrayList<>();
    String key = "AutoWallpaperHistory";
    SharedPreferences prefs;
    private int lastInteractionTime;
    private Boolean isScreenOff = false;
    boolean isIdleSet;
    boolean isWifiSet;
    boolean isBatterySet;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        onDestroy();
        setForeground();
    }

    private void setForeground() {
//        Log.e("SERVICE", "setForeground");
        flag = 0;
        imageDetailService = WallpaperApi.getClient().create(WallpaperService.class);
        shref = getSharedPreferences("MyFavImages", Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String response = shref.getString(key, "");

        prefs = PreferenceManager.getDefaultSharedPreferences(MyService.this);

        if (gson.fromJson(response, new TypeToken<List<AutoWallpaperHistoryModel>>() {
        }.getType()) != null)
            autoWallpaperHistoryModelArrayList = gson.fromJson(response, new TypeToken<List<AutoWallpaperHistoryModel>>() {
            }.getType());
        recursivelycallHandler();

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationManager notificationManager = (NotificationManager) getSystemService("notification");
            NotificationChannel notificationChannel = new NotificationChannel("noni", "system", 4);
            notificationManager.createNotificationChannel(notificationChannel);
            startForeground(8888, new Builder(this, notificationChannel.getId()).setContentTitle(getResources().getString(R.string.app_name)).setContentText("Autowallpaper is on...").setSmallIcon(R.drawable.ic_64).build());
            return;
        }
        Builder builder = new Builder(getApplicationContext());
        builder.setContentTitle(getResources().getString(R.string.app_name)).setSmallIcon(R.drawable.ic_64).setContentText("Autowallpaper is on...");
        startForeground(8888, builder.build());

    }

    public int onStartCommand(Intent intent, int i, int i2) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(NotificationCompat.CATEGORY_ALARM);
        long elapsedRealtime = SystemClock.elapsedRealtime() + 10000;
        Intent intent2 = new Intent(this, CallBroadCastReceiver.class);
        intent2.setAction("com.wond.call.coming");
        alarmManager.set(2, elapsedRealtime, PendingIntent.getBroadcast(this, 0, intent2, 0));
        return super.onStartCommand(intent, i, i2);
    }

    public void recursivelycallHandler() {
//        Log.e("SERVICE", "recursivelycallHandler");
        setWallpaperWithTimer();
//        isBatterySet = prefs.getBoolean("auto_wallpaper_charging", false);
//        isWifiSet = prefs.getBoolean("auto_wallpaper_on_wifi", false);
//        isIdleSet = prefs.getBoolean("auto_wallpaper_idle", false);
//
//        if (isIdleSet) {
//            startUserInactivityDetectThread(); // start the thread to detect inactivity
//            new ScreenReceiver();
//        } else if (isBatterySet && isConnected(getApplicationContext())) {
//            Toast.makeText(this, "BATTERY", Toast.LENGTH_SHORT).show();
//            setWallpaperWithTimer();
//        } else if (isWifiSet && checkWifiOnAndConnected()) {
//            setWallpaperWithTimer();
//        } else if (isWifiSet && checkWifiOnAndConnected()) {
//            if (isBatterySet && isConnected(getApplicationContext()))
//                setWallpaperWithTimer();
//        } else if (isIdleSet && isBatterySet && isConnected(getApplicationContext())) {
//            startUserInactivityDetectThread(); // start the thread to detect inactivity
//            new ScreenReceiver();
//        } else if (isIdleSet && isWifiSet && checkWifiOnAndConnected()) {
//            startUserInactivityDetectThread(); // start the thread to detect inactivity
//            new ScreenReceiver();
//        } else if (isBatterySet && isConnected(getApplicationContext()) && isWifiSet && checkWifiOnAndConnected() && isIdleSet) {
//            startUserInactivityDetectThread(); // start the thread to detect inactivity
//            new ScreenReceiver();
//        } else if (!isIdleSet && !isWifiSet && !isBatterySet){
//            setWallpaperWithTimer();
//        }
    }

    public void setWallpaperWithTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                prefs = PreferenceManager.getDefaultSharedPreferences(MyService.this);
                if (prefs.getBoolean("auto_wallpaper", false)) {
//                    Log.e("SERVICE", "TIMER");
                    setWallpaper();
                } else {
                    timer.cancel();
                    timer.purge();
                }
            }
        }, 0, 1000 * 60 * MINUTES);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        initAlarm();
        super.onTaskRemoved(rootIntent);
//        Log.e("handler", "task");
    }

    private void initAlarm() {
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, CallBroadCastReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE);

        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() +
                        2000, alarmIntent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private Call<ImageDetailModel> getRandom() {

        if (auto_wallpaper_category.equals("All")) {
            return imageDetailService.getRandomAll(
                    Utils.myKey,
                    "portrait"
            );
        } else if (auto_wallpaper_category.equals("Featured")) {
            return imageDetailService.getRandomFeatured(
                    Utils.myKey,
                    "portrait",
                    true
            );
        } else {
//            Log.e("SERVICE", auto_wallpaper_custom_category);
            return imageDetailService.getRandom(
                    Utils.myKey,
                    "portrait",
                    auto_wallpaper_custom_category
            );
        }
    }

    public void setWallpaper() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyService.this);
        String auto_wallpaper_interval = prefs.getString("auto_wallpaper_interval", "1440");
        MINUTES = Integer.valueOf(auto_wallpaper_interval);
//        MINUTES = 1;

        auto_wallpaper_category = prefs.getString("auto_wallpaper_category", "All");
        auto_wallpaper_custom_category = prefs.getString("auto_wallpaper_custom_category", "Wallpaper");
        auto_wallpaper_custom_category = auto_wallpaper_custom_category.replaceAll("\\s+", "");

        getRandom().enqueue(new Callback<ImageDetailModel>() {
            @Override
            public void onResponse(Call<ImageDetailModel> call, Response<ImageDetailModel> response) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                imageDetailModel = response.body();
//                Log.e("SERVICE", imageDetailModel.getUrls().getRegular());

                autoWallpaperHistoryModel = new AutoWallpaperHistoryModel(imageDetailModel, System.currentTimeMillis());
                autoWallpaperHistoryModelArrayList.add(autoWallpaperHistoryModel);

//                for (int i = 0; i < autoWallpaperHistoryModelArrayList.size(); i++)
//                    Log.e("autoWallpaperHistory", autoWallpaperHistoryModelArrayList.get(i).getImageDetailModel().getId());

                Gson gson = new Gson();
                String json = gson.toJson(autoWallpaperHistoryModelArrayList);
                editor = shref.edit();
                editor.putString(key, json);
                editor.commit();

                try {
                    URL url = new URL(imageDetailModel.getUrls().getRegular());
                    Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    WallpaperManager myWallpaperManager = WallpaperManager.getInstance(MyService.this);
                    try {
//                        Log.e("SERVICE", "BITMAP SUCCESS");
                        myWallpaperManager.setBitmap(image);
                    } catch (IOException e) {
//                        Log.e("SERVICE", String.valueOf(e));
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }
            }

            @Override
            public void onFailure(Call<ImageDetailModel> call, Throwable t) {
//                Log.e("SERVICE", "CALL FAILED");
            }
        });
    }


    public void startUserInactivityDetectThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000 * 60 * 1); // checks every 15sec for inactivity
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (isScreenOff || getLastInteractionTime() > 12000) {
                        if (prefs.getBoolean("auto_wallpaper_idle", false)) {
//                            Log.e("SERVICE", "IDLE");
                            setWallpaper();
                        }
                    }
                }
            }
        }).start();
    }

    public long getLastInteractionTime() {
        return lastInteractionTime;
    }

    private class ScreenReceiver extends BroadcastReceiver {

        protected ScreenReceiver() {
            // register receiver that handles screen on and screen off logic
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            registerReceiver(this, filter);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                isScreenOff = true;
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                isScreenOff = false;
            }
        }
    }

    public static boolean isConnected(Context context) {
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        return plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB || plugged == BatteryManager.BATTERY_PLUGGED_WIRELESS;
    }

    private boolean checkWifiOnAndConnected() {
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiMgr.isWifiEnabled()) { // Wi-Fi adapter is ON

            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

            if (wifiInfo.getNetworkId() == -1) {
                return false; // Not connected to an access point
            }
            return true; // Connected to an access point
        } else {
            return false; // Wi-Fi adapter is OFF
        }
    }
}
