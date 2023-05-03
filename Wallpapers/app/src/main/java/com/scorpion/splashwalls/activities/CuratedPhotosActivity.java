package com.scorpion.splashwalls.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
//import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdView;
import com.scorpion.splashwalls.R;
import com.scorpion.splashwalls.adapters.CuratedPhotosAdapter;
import com.scorpion.splashwalls.api.WallpaperApi;
import com.scorpion.splashwalls.api.WallpaperService;
import com.scorpion.splashwalls.helpers.AdHelper;
import com.scorpion.splashwalls.models.CuratedPhotosModel;
import com.scorpion.splashwalls.utils.PaginationScrollListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CuratedPhotosActivity extends AppCompatActivity {

    private WallpaperService imageDetailService;
    private static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    CuratedPhotosModel videoWallpaperModel;
    List<CuratedPhotosModel.Photos> videosArrayList = new ArrayList<>();
    RecyclerView userPhotosRecyclerView;
    LottieAnimationView lav_actionBar;
    LottieAnimationView noCollections;
    CuratedPhotosAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES;
    AdView adView;
    LinearLayout layBanner;
    FrameLayout ad_view_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curated_photos);
        AdHelper.LoadInter(this);
        adView = findViewById(R.id.adView);
        layBanner = findViewById(R.id.banner_container);
        ad_view_container = findViewById(R.id.ad_view_container);

        AdHelper.AdLoadHelper(CuratedPhotosActivity.this,adView, layBanner);

        imageDetailService = WallpaperApi.getClientVideoWallpaper().create(WallpaperService.class);

        userPhotosRecyclerView = findViewById(R.id.curatedPhotosRecycler);
        lav_actionBar = findViewById(R.id.lav_actionBar);
//        noCollections = findViewById(R.id.noCollections);
//        noCollections.setVisibility(View.VISIBLE);
        adapter = new CuratedPhotosAdapter(getApplicationContext());
//        swipeRefreshLayout = view.findViewById(R.id.swipe);
        linearLayoutManager = new LinearLayoutManager(this);
//        StackLayoutManager manager = new StackLayoutManager();

        userPhotosRecyclerView.setLayoutManager(linearLayoutManager);
        userPhotosRecyclerView.setItemAnimator(new DefaultItemAnimator());
        userPhotosRecyclerView.setAdapter(adapter);

        userPhotosRecyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (TOTAL_PAGES != 1)
                            loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        loadFirstPage();
    }

    private void loadFirstPage() {
//        Log.d(TAG, "loadFirstPage: ");
        videosArrayList.clear();

        getVideoWallpaperApi().enqueue(new Callback<CuratedPhotosModel>() {
            @Override
            public void onResponse(Call<CuratedPhotosModel> call, Response<CuratedPhotosModel> response) {
                adapter.clear();
                videoWallpaperModel = response.body();
                videosArrayList = Arrays.asList(videoWallpaperModel.getPhotos());
//                for (int i = 0 ; i < videosArrayList.size();i++)
//                    Log.e("VIDEO",videosArrayList.get(i).getImage());
                lav_actionBar.setVisibility(View.GONE);
//                TmpVideoWallpaperAdapter tmpVideoWallpaperAdapter = new TmpVideoWallpaperAdapter(videosArrayList,getApplicationContext());
//                userPhotosRecyclerView.setAdapter(tmpVideoWallpaperAdapter);

                adapter.addAll(videosArrayList);
                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
//                TOTAL_PAGES = videoWallpaperModel.getTotal_results()/videoWallpaperModel.getPer_page();
            }

            @Override
            public void onFailure(Call<CuratedPhotosModel> call, Throwable t) {

            }
        });
    }

    private void loadNextPage() {
//        Log.d("TAG", "loadNextPage: " + currentPage);

        getVideoWallpaperApi().enqueue(new Callback<CuratedPhotosModel>() {
            @Override
            public void onResponse(Call<CuratedPhotosModel> call, Response<CuratedPhotosModel> response) {
                adapter.removeLoadingFooter();
                isLoading = false;
                videoWallpaperModel = response.body();
                videosArrayList = Arrays.asList(videoWallpaperModel.getPhotos());
//                lav_actionBar.setVisibility(View.GONE);
                adapter.addAll(videosArrayList);

                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<CuratedPhotosModel> call, Throwable t) {

            }
        });
    }


    private Call<CuratedPhotosModel> getVideoWallpaperApi() {
        return imageDetailService.getCuratedPhotos(
                "563492ad6f91700001000001772c4741dda54b4b8d5b30ce6af5a07e",
                50,
                currentPage
        );
    }

    @Override
    public void onBackPressed() {
        AdHelper.ShowInter(CuratedPhotosActivity.this);
        super.onBackPressed();
    }
}
