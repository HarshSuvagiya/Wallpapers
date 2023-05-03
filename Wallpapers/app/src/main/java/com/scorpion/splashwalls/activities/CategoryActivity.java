package com.scorpion.splashwalls.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.ads.AdView;
import com.littlemango.stacklayoutmanager.StackLayoutManager;
import com.scorpion.splashwalls.R;
import com.scorpion.splashwalls.adapters.CategoryAdapter;
import com.scorpion.splashwalls.adapters.SearchPhotosAdapter;
import com.scorpion.splashwalls.adapters.UserPhotosAdapter;
import com.scorpion.splashwalls.api.WallpaperApi;
import com.scorpion.splashwalls.api.WallpaperService;
import com.scorpion.splashwalls.helpers.AdHelper;
import com.scorpion.splashwalls.models.CategoryModel;
import com.scorpion.splashwalls.models.SearchPhotosModel;
import com.scorpion.splashwalls.models.UserPhotosModel;
import com.scorpion.splashwalls.utils.PaginationScrollListener;
import com.scorpion.splashwalls.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity {

    ArrayList<CategoryModel> categoryModelArrayList = new ArrayList<>();
    CategoryModel categoryModel;
    private WallpaperService imageDetailService;
    ArrayList<UserPhotosModel> myList = new ArrayList<>();
    RecyclerView userPhotosRecyclerView;
    UserPhotosAdapter userPhotosAdapter;
    private static final int PAGE_START = 5;
    private int currentPage = PAGE_START;
    LottieAnimationView lav_actionBar;
    LottieAnimationView noCollections;
    //    SwipeRefreshLayout swipeRefreshLayout;
    String finalQuery;
    SearchPhotosAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = UserActivity.totalPagePhotos;
    List<SearchPhotosModel.Results> list = new ArrayList<>();
    CardView filterSearchBtn;
    CardView categoryBtn;
    RadioButton all, landscape, portrait, squarish;
    String filterFlag = "all";
    Dialog dialog1;
    AdView adView;
    LinearLayout layBanner;
    FrameLayout ad_view_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        AdHelper.LoadInter(CategoryActivity.this);

        adView = findViewById(R.id.adView);
        layBanner = findViewById(R.id.banner_container);
        ad_view_container = findViewById(R.id.ad_view_container);

        AdHelper.AdLoadHelper(CategoryActivity.this,adView, layBanner);

        imageDetailService = WallpaperApi.getClient().create(WallpaperService.class);
        getCategoryList();
        userPhotosRecyclerView = findViewById(R.id.main_recycler);
        lav_actionBar = findViewById(R.id.lav_actionBar);
        noCollections = findViewById(R.id.noCollections);
        filterSearchBtn = findViewById(R.id.filterSearchBtn);
        categoryBtn = findViewById(R.id.categoryBtn);
        adapter = new SearchPhotosAdapter(getApplicationContext());
//        swipeRefreshLayout = view.findViewById(R.id.swipe);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        userPhotosRecyclerView.setLayoutManager(linearLayoutManager);
        userPhotosRecyclerView.setItemAnimator(new DefaultItemAnimator());
        userPhotosRecyclerView.setAdapter(adapter);

        dialog1 = new Dialog(CategoryActivity.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog1.setContentView(R.layout.category_dialog);
        StackLayoutManager.ScrollOrientation orientation = StackLayoutManager.ScrollOrientation.RIGHT_TO_LEFT;
        StackLayoutManager manager = new StackLayoutManager(orientation);

        RecyclerView categoryDialogRecyclerView;
        categoryDialogRecyclerView = dialog1.findViewById(R.id.categoryDialogRecyclerView);
        CategoryAdapter categoryAdapter = new CategoryAdapter(categoryModelArrayList, CategoryActivity.this);
        categoryDialogRecyclerView.setLayoutManager(manager);
        categoryDialogRecyclerView.setAdapter(categoryAdapter);
        dialog1.show();

        categoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1 = new Dialog(CategoryActivity.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog1.setContentView(R.layout.category_dialog);

                StackLayoutManager.ScrollOrientation orientation = StackLayoutManager.ScrollOrientation.RIGHT_TO_LEFT;
                StackLayoutManager manager = new StackLayoutManager(orientation);

                RecyclerView categoryDialogRecyclerView;
                categoryDialogRecyclerView = dialog1.findViewById(R.id.categoryDialogRecyclerView);
                CategoryAdapter categoryAdapter = new CategoryAdapter(categoryModelArrayList, CategoryActivity.this);
                categoryDialogRecyclerView.setLayoutManager(manager);
                categoryDialogRecyclerView.setAdapter(categoryAdapter);

                dialog1.show();
            }
        });

        filterSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(CategoryActivity.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setContentView(R.layout.search_filter_dialog);

                all = dialog.findViewById(R.id.all);
                landscape = dialog.findViewById(R.id.landscape);
                portrait = dialog.findViewById(R.id.portrait);
                squarish = dialog.findViewById(R.id.squarish);

                if (filterFlag.equals("all"))
                    all.setChecked(true);
                else if (filterFlag.equals("landscape"))
                    landscape.setChecked(true);
                else if (filterFlag.equals("portrait"))
                    portrait.setChecked(true);
                else
                    squarish.setChecked(true);


                all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            filterFlag = "all";
                            loadFirstPage();
                            dialog.dismiss();
                        }
                    }
                });

                landscape.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            filterFlag = "landscape";
                            loadFirstPage();
                            dialog.dismiss();
                        }
                    }
                });

                portrait.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            filterFlag = "portrait";
                            loadFirstPage();
                            dialog.dismiss();
                        }
                    }
                });

                squarish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            filterFlag = "squarish";
                            loadFirstPage();
                            dialog.dismiss();
                        }
                    }
                });


                dialog.show();
            }
        });

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

//        loadFirstPage();
    }

    public void getUpdatedCategoryImages(String query) {
        finalQuery = query;
        loadFirstPage();
        dialog1.dismiss();
    }

    private void getCategoryList() {

        categoryModelArrayList.add(new CategoryModel("https://images.unsplash.com/photo-1518005020951-eccb494ad742?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjc5NjV9", "Architecture"));
        categoryModelArrayList.add(new CategoryModel("https://images.unsplash.com/photo-1545490069-c398ee1f0fd6?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjc5NjV9", "Textures & Patterns"));
        categoryModelArrayList.add(new CategoryModel("https://images.unsplash.com/photo-1540206395-68808572332f?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjc5NjV9", "Nature"));
        categoryModelArrayList.add(new CategoryModel("https://images.unsplash.com/photo-1567508429478-9a35455e0e2e?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjc5NjV9", "Current Events"));
        categoryModelArrayList.add(new CategoryModel("https://images.unsplash.com/photo-1552031536-c64b001fdc05?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjc5NjV9", "Business & Work"));
        categoryModelArrayList.add(new CategoryModel("https://images.unsplash.com/photo-1542204165-65bf26472b9b?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjc5NjV9", "Film"));
        categoryModelArrayList.add(new CategoryModel("https://images.unsplash.com/photo-1529654674248-a940ce22fe51?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjc5NjV9", "Animals"));
        categoryModelArrayList.add(new CategoryModel("https://images.unsplash.com/photo-1500835556837-99ac94a94552?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjc5NjV9", "Travel"));
        categoryModelArrayList.add(new CategoryModel("https://images.unsplash.com/photo-1512436991641-6745cdb1723f?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjc5NjV9", "Fashion"));
        categoryModelArrayList.add(new CategoryModel("https://images.unsplash.com/photo-1569864358642-9d1684040f43?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjc5NjV9", "Food & Drink"));
        categoryModelArrayList.add(new CategoryModel("https://images.unsplash.com/photo-1490730141103-6cac27aaab94?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjc5NjV9", "Spirituality"));
        categoryModelArrayList.add(new CategoryModel("https://images.unsplash.com/photo-1572851898951-4075ed48c0ce?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjc5NjV9", "Experimental"));
        categoryModelArrayList.add(new CategoryModel("https://images.unsplash.com/photo-1494790108377-be9c29b29330?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjc5NjV9", "People"));
        categoryModelArrayList.add(new CategoryModel("https://images.unsplash.com/photo-1506126613408-eca07ce68773?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjc5NjV9", "Health"));
        categoryModelArrayList.add(new CategoryModel("https://images.unsplash.com/photo-1569172122301-bc5008bc09c5?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjc5NjV9", "Arts & Culture"));

    }

    public void loadFirstPage() {
//        Log.d(TAG, "loadFirstPage: ");
        currentPage = PAGE_START;
        isLastPage = false;
        imageDetailService = WallpaperApi.getClient().create(WallpaperService.class);
        list = new ArrayList<>();
        getSearchPhotosApi().enqueue(new Callback<SearchPhotosModel>() {
            @Override
            public void onResponse(Call<SearchPhotosModel> call, Response<SearchPhotosModel> response) {
                if (response.body() != null) {
                    adapter.clear();
                    list = Arrays.asList(response.body().getResults());
                    noCollections.setVisibility(View.GONE);
                    Collections.shuffle(list);
                    adapter.addAll(list);
                    TOTAL_PAGES = Integer.parseInt(response.body().getTotal_pages());
                    if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                    else isLastPage = true;
                }
                lav_actionBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<SearchPhotosModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void loadNextPage() {
//        Log.d(TAG, "loadNextPage: " + currentPage);

        getSearchPhotosApi().enqueue(new Callback<SearchPhotosModel>() {
            @Override
            public void onResponse(Call<SearchPhotosModel> call, Response<SearchPhotosModel> response) {

                if (response.body() != null) {
                    adapter.removeLoadingFooter();
                    isLoading = false;
                    List<SearchPhotosModel.Results> results = Arrays.asList(response.body().getResults());
                    Collections.shuffle(results);
                    adapter.addAll(results);
                    noCollections.setVisibility(View.GONE);
                    if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                    else isLastPage = true;
                }
                lav_actionBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<SearchPhotosModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private Call<SearchPhotosModel> getSearchPhotosApi() {
        if (filterFlag.equals("all")) {
            return imageDetailService.getSearchPhotos(
                    finalQuery,
                    15,
                    Utils.myKey,
                    currentPage
            );
        } else {
            return imageDetailService.getSearchPhotosOrientated(
                    finalQuery,
                    15,
                    Utils.myKey,
                    currentPage,
                    filterFlag
            );
        }
    }

    @Override
    public void onBackPressed() {
        AdHelper.ShowInter(CategoryActivity.this);
        super.onBackPressed();
    }
}

