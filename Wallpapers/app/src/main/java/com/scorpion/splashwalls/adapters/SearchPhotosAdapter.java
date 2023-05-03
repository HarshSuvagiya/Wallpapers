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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ayoubfletcher.consentsdk.ConsentSDK;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.scorpion.splashwalls.R;
import com.scorpion.splashwalls.activities.ImageDetailActivity;
import com.scorpion.splashwalls.models.SearchPhotosModel;
import com.scorpion.splashwalls.helpers.AdHelper;
import com.scorpion.splashwalls.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class SearchPhotosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final int AD_TYPE = 2;
    private AdView mAdView;
    private List<SearchPhotosModel.Results> movieResults;
    private Context context;
    SearchPhotosAdapter searchPhotosAdapter;
    private boolean isLoadingAdded = false;

    public SearchPhotosAdapter(Context context) {
        this.context = context;
        movieResults = new ArrayList<>();
        AdHelper.LoadInter(context);

    }

    public List<SearchPhotosModel.Results> getMovies() {
        return movieResults;
    }

    public void setMovies(List<SearchPhotosModel.Results> movieResults) {
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
                viewHolder = new SearchPhotosAdapter.LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.related_adapter_layout, parent, false);
        viewHolder = new SearchPhotosAdapter.MovieVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final SearchPhotosModel.Results result = movieResults.get(position); // Movie
//        Log.e("movieResultsSize", String.valueOf(result.getUrls().getFull()));
        switch (getItemViewType(position)) {
            case ITEM:
                final SearchPhotosAdapter.MovieVH movieVH = (SearchPhotosAdapter.MovieVH) holder;
                String link = result.getUrls().getSmall();
                link = link.replace("w=400", "w=500");
                Picasso.get().load(link).placeholder(R.drawable.placeholder).into(movieVH.image);
//                Log.e("imageID",result.getId());
                movieVH.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AdHelper.ShowInter(context);
                        context.startActivity(new Intent(context, ImageDetailActivity.class).putExtra("image_id", result.getId()).setFlags(FLAG_ACTIVITY_NEW_TASK));
                    }
                });


//                if (position != 0)
//                    if (position % Utils.adPos == 0) {
////                        Log.e("POS", String.valueOf(position));
//
//                        AdHelper.AdLoadHelper(context,movieVH.adView,movieVH.layBanner );
//
//                    } else {
                        movieVH.adView.setVisibility(View.GONE);
//                    }

//                if (position != 0)
//                    if (position % Utils.adPos == 0) {
//                        ((FrameLayout)((MovieVH) holder).ad_view_container).setVisibility(View.VISIBLE);
//                        AdView mAdView = new AdView(context);
//                        mAdView.setAdSize(AdSize.BANNER);
//                        mAdView.setAdUnitId(context.getString(R.string.banner_id));
//                        ((FrameLayout)((MovieVH) holder).ad_view_container).addView(mAdView);
//                        AdRequest adRequest = new AdRequest.Builder().build();
//                        mAdView.loadAd(adRequest);
//                    } else {
//                        movieVH.ad_view_container.setVisibility(View.GONE);
//                    }


                break;

            case LOADING:
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

    public void add(SearchPhotosModel.Results r) {
        movieResults.add(r);
        notifyItemInserted(movieResults.size() - 1);
    }

    public void addAll(List<SearchPhotosModel.Results> moveResults) {
        for (SearchPhotosModel.Results result : moveResults) {
            add(result);
//            Log.e("IDS",result.getId());
        }
    }

    public void remove(SearchPhotosModel.Results r) {
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
        add(new SearchPhotosModel.Results());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movieResults.size() - 1;
        SearchPhotosModel.Results result = getItem(position);

        if (result != null) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public SearchPhotosModel.Results getItem(int position) {
        return movieResults.get(position);
    }

    protected class MovieVH extends RecyclerView.ViewHolder {
        private ImageView image;
        AdView adView;
        LinearLayout layBanner;
        FrameLayout ad_view_container;
//        private CircleImageView profile;
//        private TextView username;
//        LinearLayout ll1;

        public MovieVH(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            adView = itemView.findViewById(R.id.adView);
            layBanner = itemView.findViewById(R.id.banner_container);
            ad_view_container = itemView.findViewById(R.id.ad_view_container);
//            profile = itemView.findViewById(R.id.profile);
//            username = itemView.findViewById(R.id.username);
//            ll1 = itemView.findViewById(R.id.ll1);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}
