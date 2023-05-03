package com.scorpion.splashwalls.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scorpion.splashwalls.R;
import com.scorpion.splashwalls.adapters.AutoWallpaperHistoryAdapter;
import com.scorpion.splashwalls.helpers.AdHelper;
import com.scorpion.splashwalls.models.AutoWallpaperHistoryModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AutoWallpaperHistoryActivity extends AppCompatActivity {


    RecyclerView autoWallpaperHistoryRecycler;
    AutoWallpaperHistoryModel autoWallpaperHistoryModel;
    ArrayList<AutoWallpaperHistoryModel> autoWallpaperHistoryModelArrayList = new ArrayList<>();
    String key = "AutoWallpaperHistory";
    SharedPreferences shref;
    SharedPreferences.Editor editor;
    AutoWallpaperHistoryAdapter autoWallpaperHistoryAdapter;
    CardView clearBtn;
    AdView adView;
    LinearLayout layBanner;
    FrameLayout ad_view_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_wallpaper_history);
        AdHelper.LoadInter(AutoWallpaperHistoryActivity.this);
        adView = findViewById(R.id.adView);
        layBanner = findViewById(R.id.banner_container);
        ad_view_container = findViewById(R.id.ad_view_container);

        AdHelper.AdLoadHelper(AutoWallpaperHistoryActivity.this,adView, layBanner);

        clearBtn = findViewById(R.id.clearBtn);
        autoWallpaperHistoryRecycler = findViewById(R.id.autoWallpaperHistoryRecycler);
        shref = getSharedPreferences("MyFavImages", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String response = shref.getString(key, "");


        if (gson.fromJson(response, new TypeToken<List<AutoWallpaperHistoryModel>>() {
        }.getType()) != null)
            autoWallpaperHistoryModelArrayList = gson.fromJson(response, new TypeToken<List<AutoWallpaperHistoryModel>>() {
            }.getType());

        Collections.reverse(autoWallpaperHistoryModelArrayList);
        autoWallpaperHistoryAdapter = new AutoWallpaperHistoryAdapter(autoWallpaperHistoryModelArrayList, getApplicationContext());
        autoWallpaperHistoryRecycler.setLayoutManager(new LinearLayoutManager(this));
        autoWallpaperHistoryRecycler.setAdapter(autoWallpaperHistoryAdapter);

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(AutoWallpaperHistoryActivity.this);
                dialog.setContentView(R.layout.clear_history_layout);
                TextView yes, no;
                yes = dialog.findViewById(R.id.yes);
                no = dialog.findViewById(R.id.no);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        autoWallpaperHistoryModelArrayList = new ArrayList<>();
                        Gson gson = new Gson();
                        String json = gson.toJson(autoWallpaperHistoryModelArrayList);
                        editor = shref.edit();
                        editor.putString(key, json);
                        editor.commit();
                        autoWallpaperHistoryAdapter = new AutoWallpaperHistoryAdapter(autoWallpaperHistoryModelArrayList, getApplicationContext());
                        autoWallpaperHistoryRecycler.setLayoutManager(new LinearLayoutManager(AutoWallpaperHistoryActivity.this));
                        autoWallpaperHistoryRecycler.setAdapter(autoWallpaperHistoryAdapter);
                        autoWallpaperHistoryAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AdHelper.ShowInter(AutoWallpaperHistoryActivity.this);
        super.onBackPressed();
    }
}
