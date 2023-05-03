package com.scorpion.splashwalls.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.scorpion.splashwalls.R;
import com.scorpion.splashwalls.utils.Utils;

import okhttp3.internal.Util;

public class AdHelper {

    public static InterstitialAd mInterstitialAd;
    public static InterstitialAd mInterstitialAd1;

    public static void LoadInter(Context ctx) {
        if (Utils.loadAd) {
            if (Utils.AdmobFacebook == 2) {

                mInterstitialAd1 = new InterstitialAd(ctx);
                mInterstitialAd1.setAdUnitId(ctx.getResources().getString(R.string.inter_id));
                mInterstitialAd1.loadAd(new AdRequest.Builder().build());

                mInterstitialAd1.setAdListener(new AdListener());

//                FBInterstitial.getInstance().loadFBInterstitial((Activity) ctx);
            } else {
                mInterstitialAd = new InterstitialAd(ctx);
                mInterstitialAd.setAdUnitId(ctx.getResources().getString(R.string.inter_id));
                mInterstitialAd.loadAd(new AdRequest.Builder().build());

                mInterstitialAd.setAdListener(new AdListener());
            }

        }
    }

//    public static void ShowInterDirect() {
//        if (mInterstitialAd.isLoaded()) {
//            mInterstitialAd.show();
//        } else {
//            Log.d("TAG", "The interstitial wasn't loaded yet.");
//        }
//    }


//    public static void ShowInterWithoutFB() {
//
//        Utils.tmpInter = Utils.tmpInter + 1;
//        if (Utils.tmpInter % Utils.TimesInterAd == 0) {
//
//            if (Utils.loadAd) {
//                if (mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();
//                } else {
//                    Log.d("TAG", "The interstitial wasn't loaded yet.");
//                }
//            }
//        }
//    }


    public static void ShowInter(Context ctx) {

        Utils.tmpInter = Utils.tmpInter + 1;
        if (Utils.tmpInter % Utils.TimesInterAd == 0) {
            if (Utils.loadAd) {
                if (Utils.AdmobFacebook == 2) {
                    FBInterstitial.getInstance().displayFBInterstitial(new FBInterstitial.FbCallback() {
                        public void callbackCall() {

                        }
                    });
                } else {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }
                }
            }
        }

    }

    public static void AdLoadHelper(Context context, AdView adView, LinearLayout layBanner) {
        if (Utils.loadAd) {
            if (Utils.AdmobFacebook == 2) {

//                AdSettings.addTestDevice("\"1b2ee112-d0b0-4949-a1ed-44a30179e646\"");
                com.facebook.ads.AdView adView1 = new com.facebook.ads.AdView(context, context.getResources().getString(R.string.fb_banner_id), AdSize.BANNER_HEIGHT_50);
                if(layBanner!=null) {
                    layBanner.removeAllViews();
                }
                layBanner.addView(adView1);
                // Request an ad
                adView1.loadAd();
                layBanner.setVisibility(View.VISIBLE);
                adView.setVisibility(View.GONE);
                adView1.setAdListener(new com.facebook.ads.AdListener() {
                    @Override
                    public void onError(Ad ad, AdError adError) {
//                        Toast.makeText(context, "Error: " + adError.getErrorMessage(),
//                                Toast.LENGTH_LONG).show();
                        adView.setVisibility(View.VISIBLE);
                        MobileAds.initialize(context, new OnInitializationCompleteListener() {
                            @Override
                            public void onInitializationComplete(InitializationStatus initializationStatus) {
                            }
                        });
                        AdRequest adRequest = new AdRequest.Builder().build();
                        adView.loadAd(adRequest);

                        adView.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                            }

                            @Override
                            public void onAdFailedToLoad(int i) {
                                super.onAdFailedToLoad(i);
                                MobileAds.initialize(context, new OnInitializationCompleteListener() {
                                    @Override
                                    public void onInitializationComplete(InitializationStatus initializationStatus) {
                                    }
                                });
                                AdRequest adRequest = new AdRequest.Builder().build();
                                adView.loadAd(adRequest);
                                adView.setAdListener(new AdListener() {
                                    @Override
                                    public void onAdClosed() {
                                        super.onAdClosed();
                                    }

                                    @Override
                                    public void onAdFailedToLoad(int i) {
                                        super.onAdFailedToLoad(i);
                                    }

                                    @Override
                                    public void onAdLeftApplication() {
                                        super.onAdLeftApplication();
                                    }

                                    @Override
                                    public void onAdOpened() {
                                        super.onAdOpened();
                                    }

                                    @Override
                                    public void onAdLoaded() {
                                        super.onAdLoaded();
                                        adView.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onAdClicked() {
                                        super.onAdClicked();
                                    }

                                    @Override
                                    public void onAdImpression() {
                                        super.onAdImpression();
                                    }
                                });
                            }

                            @Override
                            public void onAdLeftApplication() {
                                super.onAdLeftApplication();
                            }

                            @Override
                            public void onAdOpened() {
                                super.onAdOpened();
                            }

                            @Override
                            public void onAdLoaded() {
                                super.onAdLoaded();
                                adView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAdClicked() {
                                super.onAdClicked();
                            }

                            @Override
                            public void onAdImpression() {
                                super.onAdImpression();
                            }
                        });

                    }

                    @Override
                    public void onAdLoaded(Ad ad) {

                    }

                    @Override
                    public void onAdClicked(Ad ad) {

                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {

                    }
                });


            } else {
                MobileAds.initialize(context, new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {
                    }
                });
                adView.setVisibility(View.VISIBLE);
                layBanner.setVisibility(View.GONE);
                AdRequest adRequest = new AdRequest.Builder().build();
                adView.loadAd(adRequest);
                adView.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                    }

                    @Override
                    public void onAdLeftApplication() {
                        super.onAdLeftApplication();
                    }

                    @Override
                    public void onAdOpened() {
                        super.onAdOpened();
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        adView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                    }
                });
            }

        }
    }
}
