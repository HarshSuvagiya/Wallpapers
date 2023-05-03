package com.scorpion.splashwalls.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ayoubfletcher.consentsdk.ConsentSDK;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.scorpion.splashwalls.R;
import com.scorpion.splashwalls.activities.ImageDetailActivity;
import com.scorpion.splashwalls.activities.UserActivity;
import com.scorpion.splashwalls.helpers.AdHelper;
import com.scorpion.splashwalls.models.HomeModel;
import com.scorpion.splashwalls.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class HomePaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final int AD_TYPE = 2;
    private AdView mAdView;
    private List<HomeModel> movieResults;
    private Context context;

    private boolean isLoadingAdded = false;

    public HomePaginationAdapter(Context context) {
        this.context = context;
        movieResults = new ArrayList<>();
        AdHelper.LoadInter(context);
    }

    public List<HomeModel> getMovies() {
        return movieResults;
    }

    public void setMovies(List<HomeModel> movieResults) {
        this.movieResults = movieResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;

//            case AD_TYPE:
//                View v3 = inflater.inflate(R.layout.banner_ads_layout, parent, false);
//                MobileAds.initialize(context, new OnInitializationCompleteListener() {
//                    @Override
//                    public void onInitializationComplete(InitializationStatus initializationStatus) {
//                    }
//                });
//
//                mAdView = v3.findViewById(R.id.adView);
//                AdRequest adRequest = new AdRequest.Builder().build();
//                mAdView.loadAd(adRequest);
//                viewHolder = new LoadingVH(v3);
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.home_adapter_layout, parent, false);
        viewHolder = new MovieVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final HomeModel result = movieResults.get(position); // Movie

        switch (getItemViewType(position)) {
            case ITEM:
                final MovieVH movieVH = (MovieVH) holder;
                String link = result.getUrls().getSmall();
                link = link.replace("w=400", "w=600");
                Picasso.get().load(link).placeholder(R.drawable.placeholder).into(movieVH.image);
                Picasso.get().load(result.getUser().getProfileImage().getLarge()).into(movieVH.profile);
                movieVH.username.setText(result.getUser().getName());
//                Log.e("imageID",result.getId());
                movieVH.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AdHelper.ShowInter((Activity)context);
                        context.startActivity(new Intent(context, ImageDetailActivity.class).putExtra("image_id", result.getId()).setFlags(FLAG_ACTIVITY_NEW_TASK));
                    }
                });

                movieVH.ll1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AdHelper.ShowInter(context);
                        context.startActivity(new Intent(context, UserActivity.class).putExtra("username", result.getUser().getUsername()).setFlags(FLAG_ACTIVITY_NEW_TASK));
                    }
                });

                MobileAds.initialize(context, new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {
                    }
                });

//                if (position != 0)
//                    if (position % Utils.adPos == 0) {
//                        AdView adView;
//
//                        adView = new AdView(context);
//                        adView.setAdUnitId(context.getString(R.string.banner_id));
//                        ((MovieVH) holder).ad_view_container.addView(adView);
//                        AdSize adSize = getAdSize();
//                        adView.setAdSize(adSize);
//                        adView.loadAd(ConsentSDK.getAdRequest(context));
//                    } else {
//                        ((MovieVH) holder).ad_view_container.setVisibility(View.GONE);
//                    }


                break;

            case LOADING:
//                Do nothing
                break;
        }

    }

    private AdSize getAdSize() {
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
    }

    @Override
    public int getItemCount() {
        return movieResults == null ? 0 : movieResults.size();
    }

    @Override
    public int getItemViewType(int position) {
//        if (position != 0)
//            if (position % 5 == 0)
//                return AD_TYPE;
        return (position == movieResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void add(HomeModel r) {
        movieResults.add(r);
        notifyItemInserted(movieResults.size() - 1);
    }

    public void addAll(List<HomeModel> moveResults) {
        for (HomeModel result : moveResults) {
            add(result);
        }
    }

    public void remove(HomeModel r) {
        int position = movieResults.indexOf(r);
        if (position > -1) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new HomeModel());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movieResults.size() - 1;
        HomeModel result = getItem(position);

        if (result != null) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public HomeModel getItem(int position) {
        return movieResults.get(position);
    }

    protected class MovieVH extends RecyclerView.ViewHolder {
        private ImageView image;
        private CircleImageView profile;
        private TextView username;
        LinearLayout ll1;
        FrameLayout ad_view_container;
        public MovieVH(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            profile = itemView.findViewById(R.id.profile);
            username = itemView.findViewById(R.id.username);
            ll1 = itemView.findViewById(R.id.ll1);
            ad_view_container = itemView.findViewById(R.id.ad_view_container);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}
