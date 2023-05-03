package com.scorpion.splashwalls.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.airbnb.lottie.LottieAnimationView;
import com.scorpion.splashwalls.R;
import com.scorpion.splashwalls.activities.SearchActivity;
import com.scorpion.splashwalls.activities.UserActivity;
import com.scorpion.splashwalls.adapters.SearchPhotosAdapter;
import com.scorpion.splashwalls.adapters.UserPhotosAdapter;
import com.scorpion.splashwalls.api.WallpaperApi;
import com.scorpion.splashwalls.api.WallpaperService;
import com.scorpion.splashwalls.models.SearchPhotosModel;
import com.scorpion.splashwalls.models.UserPhotosModel;
import com.scorpion.splashwalls.utils.PaginationScrollListener;
import com.scorpion.splashwalls.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPhotosFragment extends Fragment {

    private WallpaperService imageDetailService;
    ArrayList<UserPhotosModel> myList = new ArrayList<>();
    RecyclerView userPhotosRecyclerView;
    UserPhotosAdapter userPhotosAdapter;
    private static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    LottieAnimationView lav_actionBar;
    LottieAnimationView noCollections;
    //    SwipeRefreshLayout swipeRefreshLayout;
    SearchPhotosAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = UserActivity.totalPagePhotos;
    List<SearchPhotosModel.Results> list = new ArrayList<>();
    CardView filterSearchBtn;
    RadioButton all, landscape, portrait, squarish;
    String filterFlag = "all";

    public static SearchPhotosFragment newInstance() {
        return new SearchPhotosFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageDetailService = WallpaperApi.getClient().create(WallpaperService.class);
//        loadFirstPage();
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_photos, container, false);

        userPhotosRecyclerView = view.findViewById(R.id.main_recycler);
        lav_actionBar = view.findViewById(R.id.lav_actionBar);
        noCollections = view.findViewById(R.id.noCollections);
        filterSearchBtn = view.findViewById(R.id.filterSearchBtn);
        adapter = new SearchPhotosAdapter(getActivity());
//        swipeRefreshLayout = view.findViewById(R.id.swipe);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        userPhotosRecyclerView.setLayoutManager(linearLayoutManager);
        userPhotosRecyclerView.setItemAnimator(new DefaultItemAnimator());
        userPhotosRecyclerView.setAdapter(adapter);

        filterSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
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

        return view;
    }

    private Call<SearchPhotosModel> getSearchPhotosApi() {
        if (filterFlag.equals("all")) {
            return imageDetailService.getSearchPhotos(
                    SearchActivity.searchItem,
                    15,
                    Utils.myKey,
                    currentPage
            );
        } else {
            return imageDetailService.getSearchPhotosOrientated(
                    SearchActivity.searchItem,
                    15,
                    Utils.myKey,
                    currentPage,
                    filterFlag
            );
        }
    }
}
