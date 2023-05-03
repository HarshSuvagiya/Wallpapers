package com.scorpion.splashwalls.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;
import com.scorpion.splashwalls.R;
import com.scorpion.splashwalls.adapters.TabsPagerAdapter;
import com.scorpion.splashwalls.api.WallpaperApi;
import com.scorpion.splashwalls.api.WallpaperService;
import com.scorpion.splashwalls.helpers.AdHelper;
import com.scorpion.splashwalls.models.UserModel;
import com.scorpion.splashwalls.utils.Utils;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {

    public static String username1;
    TextView username,name,bio,followers,followings;
    private WallpaperService imageDetailService;
    UserModel userModel;
    ImageView profile;
    TabLayout mTabLayout;
    ViewPager mViewPager;
    LinearLayout ll1;
    public static int totalPagePhotos;
    public static int totalPageLikes;
    public static int totalPageCollections;
    AdView adView;
    LinearLayout layBanner;
    FrameLayout ad_view_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        AdHelper.LoadInter(this);

        adView = findViewById(R.id.adView);
        layBanner = findViewById(R.id.banner_container);
        ad_view_container = findViewById(R.id.ad_view_container);

        AdHelper.AdLoadHelper(UserActivity.this,adView, layBanner);


        username1 = getIntent().getStringExtra("username");
//        Log.e("username",username1);
        loadShowCase();
        imageDetailService = WallpaperApi.getClient().create(WallpaperService.class);

        ll1 = findViewById(R.id.ll1);
        mTabLayout = findViewById(R.id.user_tabs);
        mViewPager = findViewById(R.id.user_viewpager);
        profile = findViewById(R.id.profile);
        followers = findViewById(R.id.followers);
        followings = findViewById(R.id.followings);
        username = findViewById(R.id.username);
        name = findViewById(R.id.name);
        bio = findViewById(R.id.bio);

        TabsPagerAdapter tabsPagerAdapter = new TabsPagerAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(tabsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mViewPager);

        getUserDetailsApi().enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                userModel = response.body();

                if (response.body()!=null){
                    totalPagePhotos = userModel.getTotal_photos()/30 + 1;
                    totalPageLikes = userModel.getTotal_likes()/30 + 1;
                    totalPageCollections = userModel.getTotal_collections()/30 + 1;
//                    Log.e("totalPagePhotos", String.valueOf(totalPagePhotos));
//                    Log.e("totalPageLikes", String.valueOf(totalPageLikes));
//                    Log.e("totalPageCollections", String.valueOf(totalPageCollections));

                    Picasso.get().load(userModel.getProfile_image().getLarge()).into(profile, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            ll1.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });

                    username.setText("@"+userModel.getUsername());
                    name.setText(userModel.getName());
                    bio.setText(userModel.getBio());
                    followers.setText("Followers : "+userModel.getFollowers_count());
                    followings.setText("Followings : "+userModel.getFollowing_count());

                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });
    }

    private Call<UserModel> getUserDetailsApi() {
        return imageDetailService.getUserDetails(
                username1,
                Utils.myKey
        );
    }

    @Override
    public void onBackPressed() {
        AdHelper.ShowInter(UserActivity.this);
        super.onBackPressed();
    }

    private void loadShowCase() {
        RelativeLayout.LayoutParams lps;
        ShowcaseView showCase;
        lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
        lps.setMargins(margin, margin, margin, 80 + ((Number) (getResources().getDisplayMetrics().density * 5)).intValue());

        showCase = new ShowcaseView.Builder(this)
                .blockAllTouches()
                .singleShot(6)
                .setStyle(R.style.CustomShowcaseTheme)
                .useDecorViewAsParent()
                .setTarget(new ViewTarget(R.id.username, this))
                .setContentTitle("User Profile")
                .setContentText("User profile in Unsplash with their status,followings and followers count.")
                .build();
        showCase.setButtonText("Next");
        showCase.setButtonPosition(lps);
        showCase.overrideButtonClick(new View.OnClickListener() {
            int count1 = 0;

            @Override
            public void onClick(View v) {
                count1++;
                switch (count1) {
                    case 1:
                        showCase.setTarget(new ViewTarget(R.id.user_tabs, UserActivity.this));
                        showCase.setContentTitle("Contribution...");
                        showCase.setContentText("User contribution are shown over here with photos,likes and collections made in unsplash.");
                        showCase.setButtonPosition(lps);
                        showCase.setButtonText("Done");
                        break;
                    case 2:
                        showCase.hide();
                        break;
                }
            }

        });
    }

}
