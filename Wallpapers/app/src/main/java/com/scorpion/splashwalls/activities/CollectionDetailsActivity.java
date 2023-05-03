package com.scorpion.splashwalls.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
//import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.ads.AdView;
import com.google.android.material.appbar.AppBarLayout;
import com.scorpion.splashwalls.R;
import com.scorpion.splashwalls.adapters.CollectionsRelatedAdapter;
import com.scorpion.splashwalls.adapters.UserCollectionsAdapter;
import com.scorpion.splashwalls.adapters.UserPhotosAdapter;
import com.scorpion.splashwalls.api.WallpaperApi;
import com.scorpion.splashwalls.api.WallpaperService;
import com.scorpion.splashwalls.fragments.UserPhotosFragment;
import com.scorpion.splashwalls.helpers.AdHelper;
import com.scorpion.splashwalls.models.UserCollectionsModel;
import com.scorpion.splashwalls.models.UserPhotosModel;
import com.scorpion.splashwalls.utils.PaginationScrollListener;
import com.scorpion.splashwalls.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionDetailsActivity extends AppCompatActivity {

    ImageView profile;
    TextView name, description, byWhom;
    String collection_id;
    private WallpaperService imageDetailService;
    AppBarLayout app_bar;
    UserCollectionsModel userCollectionsModel;
    ArrayList<UserPhotosModel> myList = new ArrayList<>();
    RecyclerView userPhotosRecyclerView;
    UserCollectionsAdapter userCollectionsAdapter;
    CollectionsRelatedAdapter collectionsRelatedAdapter;
    ArrayList<UserCollectionsModel> collectionsAdapterArrayList = new ArrayList<>();

    public static UserPhotosFragment newInstance() {
        return new UserPhotosFragment();
    }

    UserPhotosAdapter userPhotosAdapter;
    private static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    LottieAnimationView lav_actionBar;
    LottieAnimationView noCollections;
    //    SwipeRefreshLayout swipeRefreshLayout;
    UserPhotosAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES;
    ArrayList<UserPhotosModel> list = new ArrayList<>();
    CardView relatedBtn;
    AdView adView;
    LinearLayout layBanner;
    FrameLayout ad_view_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_details);
        loadShowCase();
        AdHelper.LoadInter(CollectionDetailsActivity.this);
        adView = findViewById(R.id.adView);
        layBanner = findViewById(R.id.banner_container);
        ad_view_container = findViewById(R.id.ad_view_container);

        AdHelper.AdLoadHelper(CollectionDetailsActivity.this,adView, layBanner);

        collection_id = getIntent().getStringExtra("collection_id");
        imageDetailService = WallpaperApi.getClient().create(WallpaperService.class);
        profile = findViewById(R.id.profile);
        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        byWhom = findViewById(R.id.byWhom);
        app_bar = findViewById(R.id.app_bar);
        relatedBtn = findViewById(R.id.relatedBtn);
        userPhotosRecyclerView = findViewById(R.id.collectionsRecycler);
        lav_actionBar = findViewById(R.id.lav_actionBar);
        noCollections = findViewById(R.id.noCollections);
        adapter = new UserPhotosAdapter(CollectionDetailsActivity.this);
//        swipeRefreshLayout = view.findViewById(R.id.swipe);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
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

        relatedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(CollectionDetailsActivity.this, R.style.RelatedDialog);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setContentView(R.layout.related_dialog);
                dialog.show();
                dialog.getWindow().getAttributes().windowAnimations = R.style.RelatedDialogAnimation;

                RecyclerView relatedRecycler;
                relatedRecycler = dialog.findViewById(R.id.relatedRecycler);

                LottieAnimationView relatedLoading;
                relatedLoading = dialog.findViewById(R.id.relatedLoading);

                getCollectionsDetailsRelatedApi().enqueue(new Callback<ArrayList<UserCollectionsModel>>() {
                    @Override
                    public void onResponse(Call<ArrayList<UserCollectionsModel>> call, Response<ArrayList<UserCollectionsModel>> response) {
                        collectionsAdapterArrayList = response.body();
                        if (response.body() != null) {
                            collectionsRelatedAdapter = new CollectionsRelatedAdapter(collectionsAdapterArrayList, CollectionDetailsActivity.this);
                            relatedRecycler.setLayoutManager(new LinearLayoutManager(CollectionDetailsActivity.this));
                            relatedRecycler.setAdapter(collectionsRelatedAdapter);
                            relatedLoading.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserCollectionsModel>> call, Throwable t) {
//                        Log.e("fail", String.valueOf(t));
                    }
                });

            }
        });


        getCollectionFromId().enqueue(new Callback<UserCollectionsModel>() {
            @Override
            public void onResponse(Call<UserCollectionsModel> call, Response<UserCollectionsModel> response) {
                userCollectionsModel = response.body();
                name.setText(response.body().getTitle());
                Picasso.get().load(response.body().getUser().getProfileImage().getLarge()).into(profile);
                description.setText(response.body().getDescription());
                byWhom.setText("By " + response.body().getUser().getName());
                TOTAL_PAGES = userCollectionsModel.getTotal_photos() / 30 + 1;
                if (description.getText().toString().length() == 0)
                    description.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<UserCollectionsModel> call, Throwable t) {

            }
        });

        app_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserActivity.class).putExtra("username", userCollectionsModel.getUser().getUsername()));
            }
        });

        loadFirstPage();

    }

    private void loadFirstPage() {
//        Log.d(TAG, "loadFirstPage: ");
        list.clear();
        getCollectionDetailsFromId().enqueue(new Callback<ArrayList<UserPhotosModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UserPhotosModel>> call, Response<ArrayList<UserPhotosModel>> response) {

                if (response.body() != null)
                    if (response.body().size() != 0) {
                        adapter.clear();
                        list = response.body();
//                progressBar.setVisibility(View.GONE);

                        noCollections.setVisibility(View.GONE);
//                Log.e("sizefromfirstpage", String.valueOf(ge));
//                Log.e("idfromfirstpage", String.valueOf(list.get(0).getId()));
                        adapter.addAll(list);
//                swipeRefreshLayout.setRefreshing(false);
//                adapter.removeLoadingFooter();
                    }
                lav_actionBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ArrayList<UserPhotosModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void loadNextPage() {
//        Log.d(TAG, "loadNextPage: " + currentPage);

        getCollectionDetailsFromId().enqueue(new Callback<ArrayList<UserPhotosModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UserPhotosModel>> call, Response<ArrayList<UserPhotosModel>> response) {

                if (response.body() != null)
                    if (response.body().size() != 0) {
                        adapter.removeLoadingFooter();
                        isLoading = false;
//                Log.e("sizefromnextpage", String.valueOf(response.body().size()));
//                Log.e("idfromnextpage",response.body().get(0).getId());
                        List<UserPhotosModel> results = response.body();
                        adapter.addAll(results);
                        noCollections.setVisibility(View.GONE);
//
//                        TOTAL_PAGES = UserActivity.totalPagePhotos;
//                    Log.e("PAGES", String.valueOf(TOTAL_PAGES));
                        if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                        else isLastPage = true;
                    }
                lav_actionBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ArrayList<UserPhotosModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private Call<UserCollectionsModel> getCollectionFromId() {
        return imageDetailService.getUserCollections(
                collection_id,
                30,
                Utils.myKey
        );
    }

    private Call<ArrayList<UserPhotosModel>> getCollectionDetailsFromId() {
        return imageDetailService.getUserCollectionsDetails(
                collection_id,
                30,
                Utils.myKey,
                currentPage

        );
    }

    private Call<ArrayList<UserCollectionsModel>> getCollectionsDetailsRelatedApi() {
        return imageDetailService.getCollectionsDetailsRelated(
                collection_id,
                Utils.myKey
        );
    }

    @Override
    public void onBackPressed() {
        AdHelper.ShowInter(CollectionDetailsActivity.this);
        super.onBackPressed();
    }

    private void loadShowCase() {
        RelativeLayout.LayoutParams lps;
        ShowcaseView showCase;
        lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
        lps.setMargins(margin, margin, margin, 80 + ((Number) (getResources().getDisplayMetrics().density * 5)).intValue());

        showCase = new ShowcaseView.Builder(this)
                .blockAllTouches()
                .singleShot(4)
                .setStyle(R.style.CustomShowcaseTheme)
                .useDecorViewAsParent()
                .setTarget(new ViewTarget(R.id.name, this))
                .setContentTitle("User Profile")
                .setContentText("Click here to explore more about user.")
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
                        showCase.setTarget(new ViewTarget(R.id.relatedBtn, CollectionDetailsActivity.this));
                        showCase.setContentTitle("Related Collections");
                        showCase.setContentText("You can always find related collections...");
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
