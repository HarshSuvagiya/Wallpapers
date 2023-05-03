package com.scorpion.splashwalls.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.scorpion.splashwalls.R;
import com.scorpion.splashwalls.activities.SearchActivity;
import com.scorpion.splashwalls.activities.UserActivity;
import com.scorpion.splashwalls.adapters.SearchUsersAdapter;
import com.scorpion.splashwalls.adapters.UserPhotosAdapter;
import com.scorpion.splashwalls.api.WallpaperApi;
import com.scorpion.splashwalls.api.WallpaperService;
import com.scorpion.splashwalls.models.SearchUserModel;
import com.scorpion.splashwalls.models.UserCollectionsModel;
import com.scorpion.splashwalls.utils.PaginationScrollListener;
import com.scorpion.splashwalls.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchUsersFragment extends Fragment {

    private WallpaperService imageDetailService;
    ArrayList<UserCollectionsModel> myList = new ArrayList<>();
    RecyclerView userPhotosRecyclerView;
    UserPhotosAdapter userPhotosAdapter;
    private static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    LottieAnimationView lav_actionBar;
    LottieAnimationView noCollections;
    //    SwipeRefreshLayout swipeRefreshLayout;
    SearchUsersAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = UserActivity.totalPagePhotos;
    List<SearchUserModel.Results> list = new ArrayList<>();

    public static SearchUsersFragment newInstance() {
        return new SearchUsersFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageDetailService = WallpaperApi.getClient().create(WallpaperService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_users, container, false);

        userPhotosRecyclerView = view.findViewById(R.id.main_recycler);
        lav_actionBar = view.findViewById(R.id.lav_actionBar);
        noCollections = view.findViewById(R.id.noCollections);
        noCollections.setVisibility(View.VISIBLE);
        adapter = new SearchUsersAdapter(getActivity());
//        swipeRefreshLayout = view.findViewById(R.id.swipe);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
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

        return view;
    }

    public void loadFirstPage() {
//        Log.e("IDS", "IDS");
        currentPage = PAGE_START;
        isLastPage = false;
        imageDetailService = WallpaperApi.getClient().create(WallpaperService.class);
        list = new ArrayList<>();
        getSearchUsersApi().enqueue(new Callback<SearchUserModel>() {
            @Override
            public void onResponse(Call<SearchUserModel> call, Response<SearchUserModel> response) {
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
            public void onFailure(Call<SearchUserModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void loadNextPage() {
        getSearchUsersApi().enqueue(new Callback<SearchUserModel>() {
            @Override
            public void onResponse(Call<SearchUserModel> call, Response<SearchUserModel> response) {

                if (response.body() != null) {
                    adapter.removeLoadingFooter();
                    isLoading = false;
                    List<SearchUserModel.Results> results = Arrays.asList(response.body().getResults());
                    adapter.addAll(results);
                    noCollections.setVisibility(View.GONE);
//                    Log.e("TOTAL_PAGES", String.valueOf(TOTAL_PAGES));
                    if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                    else isLastPage = true;
                }
                lav_actionBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<SearchUserModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private Call<SearchUserModel> getSearchUsersApi() {
        return imageDetailService.getSearchUsers(
                SearchActivity.searchItem,
                15,
                Utils.myKey,
                currentPage
        );
    }
}
