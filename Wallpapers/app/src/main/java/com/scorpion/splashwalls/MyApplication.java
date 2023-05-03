package com.scorpion.splashwalls;

import android.app.Application;

import androidx.annotation.NonNull;

import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.onesignal.OneSignal;
import com.scorpion.splashwalls.helpers.FBInterstitial;
import com.scorpion.splashwalls.utils.Utils;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(getApplicationContext());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("SplashWalls").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Utils.myKey = dataSnapshot.child("API").getValue().toString();
                Utils.adPos = Integer.parseInt(dataSnapshot.child("AdPerItem").getValue().toString());
                Utils.loadAd = Boolean.parseBoolean(dataSnapshot.child("LoadAd").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        AudienceNetworkAds.initialize(this);
        AdSettings.addTestDevice("e838929d-e2db-46a6-afff-95107d323f87");
        FBInterstitial.getInstance().loadFBInterstitial(this);
    }
}
