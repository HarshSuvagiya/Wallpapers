package com.scorpion.splashwalls.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.facebook.ads.Ad;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scorpion.splashwalls.R;
import com.scorpion.splashwalls.adapters.MyFavImagesAdapter;
import com.scorpion.splashwalls.helpers.AdHelper;
import com.scorpion.splashwalls.models.ImageDetailModel;
import com.scorpion.splashwalls.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyFavImagesActivity extends AppCompatActivity {

    RecyclerView myFavRecycler;
    ArrayList<ImageDetailModel> myList = new ArrayList<>();
    ArrayList<ImageDetailModel> lstArrayList = new ArrayList<>();
    SharedPreferences shref;
    SharedPreferences.Editor editor;
    String key = "MyFavImages";

    AdView adView;
    LinearLayout layBanner;
    FrameLayout ad_view_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fav_images);
        shref = getSharedPreferences("MyFavImages", Context.MODE_PRIVATE);

        myFavRecycler = findViewById(R.id.myFavRecycler);

        layBanner = findViewById(R.id.banner_container);
        ad_view_container = findViewById(R.id.ad_view_container);

        AdHelper.LoadInter(this);
//        AdHelper.AdLoadHelper(this, mAdView, layBanner);

        adView = findViewById(R.id.adView);
        layBanner = findViewById(R.id.banner_container);
        ad_view_container = findViewById(R.id.ad_view_container);

        AdHelper.AdLoadHelper(MyFavImagesActivity.this,adView, layBanner);

    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            Gson gson = new Gson();
            String response = shref.getString(key, "");
            lstArrayList = gson.fromJson(response,
                    new TypeToken<List<ImageDetailModel>>() {
                    }.getType());
            Collections.reverse(lstArrayList);
            MyFavImagesAdapter myFavImagesAdapter = new MyFavImagesAdapter(lstArrayList, getApplicationContext());
            myFavRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            myFavRecycler.setAdapter(myFavImagesAdapter);
        } catch (Exception e) {
        }


    }

    @Override
    public void onBackPressed() {
        AdHelper.ShowInter(MyFavImagesActivity.this);
        super.onBackPressed();
    }
}
