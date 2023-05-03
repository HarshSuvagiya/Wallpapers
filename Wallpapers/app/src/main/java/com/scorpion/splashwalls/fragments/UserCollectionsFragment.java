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
import com.scorpion.splashwalls.activities.UserActivity;
import com.scorpion.splashwalls.adapters.UserCollectionsAdapter;
import com.scorpion.splashwalls.api.WallpaperApi;
import com.scorpion.splashwalls.api.WallpaperService;
import com.scorpion.splashwalls.models.UserCollectionsModel;
import com.scorpion.splashwalls.utils.PaginationScrollListener;
import com.scorpion.splashwalls.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserCollectionsFragment extends Fragment {

    private WallpaperService imageDetailService;
    RecyclerView userPhotosRecyclerView;
    UserCollectionsAdapter userPhotosAdapter;
    private static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    LottieAnimationView lav_actionBar;
    LottieAnimationView noCollections;
//    SwipeRefreshLayout swipeRefreshLayout;
    UserCollectionsAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 1;
    ArrayList<UserCollectionsModel> list = new ArrayList<>();

    public static UserCollectionsFragment newInstance() {
        return new UserCollectionsFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageDetailService = WallpaperApi.getClient().create(WallpaperService.class);

        loadFirstPage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_collections, container, false);

        userPhotosRecyclerView = view.findViewById(R.id.main_recycler);
        lav_actionBar = view.findViewById(R.id.lav_actionBar);
        noCollections = view.findViewById(R.id.noCollections);
        noCollections.setVisibility(View.VISIBLE);
        adapter = new UserCollectionsAdapter(getActivity());
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

    private void loadFirstPage() {
//        Log.d(TAG, "loadFirstPage: ");
        list.clear();
        getUserCollections().enqueue(new Callback<ArrayList<UserCollectionsModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UserCollectionsModel>> call, Response<ArrayList<UserCollectionsModel>> response) {
                if (response.body()!=null)
                if(response.body().size()!=0){
                    adapter.clear();
                    list = response.body();
                    noCollections.setVisibility(View.GONE);
                    adapter.addAll(list);
                    if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                    else isLastPage = true;
                }
                lav_actionBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ArrayList<UserCollectionsModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void loadNextPage() {
//        Log.d(TAG, "loadNextPage: " + currentPage);

        getUserCollections().enqueue(new Callback<ArrayList<UserCollectionsModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UserCollectionsModel>> call, Response<ArrayList<UserCollectionsModel>> response) {

                if (response.body()!=null)
                if (response.body().size()!=0){
                    adapter.removeLoadingFooter();
                    isLoading = false;
                    List<UserCollectionsModel> results = response.body();
                    adapter.addAll(results);
                    noCollections.setVisibility(View.GONE);
                    TOTAL_PAGES = UserActivity.totalPageCollections;
//                    Log.e("PAGESCollections", String.valueOf(TOTAL_PAGES));
                    if (currentPage != TOTAL_PAGES)
                        adapter.addLoadingFooter();
                    else isLastPage = true;
                }
                lav_actionBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ArrayList<UserCollectionsModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private Call<ArrayList<UserCollectionsModel>> getUserCollections() {
        return imageDetailService.getUserCollections(
                UserActivity.username1,
                30,
                Utils.myKey,
                currentPage
        );
    }

}
