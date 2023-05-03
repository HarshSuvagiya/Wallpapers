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
import com.scorpion.splashwalls.adapters.UserCollectionsAdapter;
import com.scorpion.splashwalls.api.WallpaperApi;
import com.scorpion.splashwalls.api.WallpaperService;
import com.scorpion.splashwalls.helpers.AdHelper;
import com.scorpion.splashwalls.models.UserCollectionsModel;
import com.scorpion.splashwalls.utils.PaginationScrollListener;
import com.scorpion.splashwalls.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionsActivity extends AppCompatActivity {

    RecyclerView collectionsRecycler;
    private WallpaperService imageDetailService;
    UserCollectionsAdapter userPhotosAdapter;
    private static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    LottieAnimationView lav_actionBar;
    //    SwipeRefreshLayout swipeRefreshLayout;
    UserCollectionsAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 45;
    ArrayList<UserCollectionsModel> list = new ArrayList<>();
    AdView adView;
    LinearLayout layBanner;
    FrameLayout ad_view_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);

        AdHelper.LoadInter(this);
        adView = findViewById(R.id.adView);
        layBanner = findViewById(R.id.banner_container);
        ad_view_container = findViewById(R.id.ad_view_container);

        AdHelper.AdLoadHelper(CollectionsActivity.this,adView, layBanner);

        collectionsRecycler = findViewById(R.id.collectionsRecycler);
        imageDetailService = WallpaperApi.getClient().create(WallpaperService.class);
        lav_actionBar = findViewById(R.id.lav_actionBar);

        adapter = new UserCollectionsAdapter(this);
//        swipeRefreshLayout = view.findViewById(R.id.swipe);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        collectionsRecycler.setLayoutManager(linearLayoutManager);
        collectionsRecycler.setItemAnimator(new DefaultItemAnimator());
        collectionsRecycler.setAdapter(adapter);

        collectionsRecycler.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
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
        list.clear();
        getCollections().enqueue(new Callback<ArrayList<UserCollectionsModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UserCollectionsModel>> call, Response<ArrayList<UserCollectionsModel>> response) {

                adapter.clear();
                list = response.body();
//                progressBar.setVisibility(View.GONE);
                lav_actionBar.setVisibility(View.GONE);
//                Log.e("size", String.valueOf(list.size()));
//                Log.e("size", String.valueOf(list.get(0).getId()));
                adapter.addAll(list);

                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
//                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ArrayList<UserCollectionsModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void loadNextPage() {
//        Log.d("TAG", "loadNextPage: " + currentPage);

        getCollections().enqueue(new Callback<ArrayList<UserCollectionsModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UserCollectionsModel>> call, Response<ArrayList<UserCollectionsModel>> response) {
                adapter.removeLoadingFooter();
                isLoading = false;

                List<UserCollectionsModel> results = response.body();
                adapter.addAll(results);
//
                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<ArrayList<UserCollectionsModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private Call<ArrayList<UserCollectionsModel>> getCollections() {
        return imageDetailService.getCollections(
                30,
                Utils.myKey,
                currentPage
        );
    }

    @Override
    public void onBackPressed() {
        AdHelper.ShowInter(CollectionsActivity.this);
        super.onBackPressed();
    }
}
