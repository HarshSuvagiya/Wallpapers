package com.scorpion.splashwalls.fragments;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.CheckBoxPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scorpion.splashwalls.R;
import com.scorpion.splashwalls.helpers.AdHelper;
import com.scorpion.splashwalls.services.MyService;

public class AutoWallpaperFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private CheckBoxPreference mEnableAutoWallpaperPreference;
    private EditTextPreference mCustomCategoryPreference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.autowallpaperpreferences);
//        getContext().getTheme().applyStyle(R.style.PreferenceScreen, true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mCustomCategoryPreference = findPreference("auto_wallpaper_custom_category");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean customCategorySelected = sharedPreferences
                .getString("auto_wallpaper_category", getActivity().getResources().getString(R.string.auto_wallpaper_category_default))
                .equals("Custom");
        showCustomCategoryPreference(customCategorySelected);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        if (sharedPreferences.getBoolean("auto_wallpaper", false)) {
//            getActivity().stopService(new Intent(getActivity(), MyService.class));
//            PendingIntent.getService(getActivity(), 0, new Intent(getActivity(), MyService.class), PendingIntent.FLAG_IMMUTABLE);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                getActivity().startForegroundService(new Intent(getActivity(), MyService.class));
//            } else {
//                getActivity().startService(new Intent(getActivity(), MyService.class));
//            }
            getActivity().startService(new Intent(getActivity(), MyService.class));
//            Context context;

            if (pref.getInt("key_name", 0) == 0) {
                ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Turning on...");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 3000);
                editor.putInt("key_name", 1);
                editor.commit();
            }
        } else {
            editor.putInt("key_name", 0);
            editor.commit();
            getActivity().stopService(new Intent(getActivity(), MyService.class));
        }


        boolean customCategorySelected = sharedPreferences
                .getString("auto_wallpaper_category", getActivity().getResources().getString(R.string.auto_wallpaper_category_default))
                .equals("Custom");
        showCustomCategoryPreference(customCategorySelected);
    }

    private void showCustomCategoryPreference(boolean show) {
        PreferenceCategory preferenceCategory = findPreference("auto_wallpaper_source");
        if (show) {
            preferenceCategory.addPreference(mCustomCategoryPreference);
        } else {
            preferenceCategory.removePreference(mCustomCategoryPreference);
        }
    }
}
