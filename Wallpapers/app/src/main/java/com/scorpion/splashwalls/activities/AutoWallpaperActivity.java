package com.scorpion.splashwalls.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.preference.PreferenceManager;

import com.facebook.ads.Ad;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.scorpion.splashwalls.R;
import com.scorpion.splashwalls.adapters.SearchPhotosAdapter;
import com.scorpion.splashwalls.fragments.AutoWallpaperFragment;
import com.scorpion.splashwalls.helpers.AdHelper;
import com.scorpion.splashwalls.utils.Utils;

public class AutoWallpaperActivity extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;
    private AdView mAdView;

    AdView adView;
    LinearLayout layBanner;
    FrameLayout ad_view_container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_wallpaper);

        getSupportFragmentManager().beginTransaction().replace(R.id.container123, new AutoWallpaperFragment()).commit();


        AdHelper.LoadInter(AutoWallpaperActivity.this);


        adView = findViewById(R.id.adView);
        layBanner = findViewById(R.id.banner_container);
        ad_view_container = findViewById(R.id.ad_view_container);

        AdHelper.AdLoadHelper(AutoWallpaperActivity.this,adView, layBanner);



//                ad_view_container.setVisibility(View.VISIBLE);
//                AdView mAdView = new AdView(getApplicationContext());
//                mAdView.setAdSize(AdSize.BANNER);
//                mAdView.setAdUnitId(getString(R.string.banner_id));
//                ad_view_container.addView(mAdView);
//                AdRequest adRequest = new AdRequest.Builder().build();
//                mAdView.loadAd(adRequest);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        startService(new Intent(getApplicationContext(), MyService.class));
//        Log.e("auto_wallpaper","");

    }

    @Override
    protected void onResume() {

        super.onResume();

    }

    @Override
    public void onBackPressed() {

        AdHelper.ShowInter(AutoWallpaperActivity.this);
        super.onBackPressed();

    }
}
