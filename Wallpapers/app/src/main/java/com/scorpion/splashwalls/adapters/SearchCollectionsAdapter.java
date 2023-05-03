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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.scorpion.splashwalls.R;
import com.scorpion.splashwalls.activities.CollectionDetailsActivity;
import com.scorpion.splashwalls.activities.ImageDetailActivity;
import com.scorpion.splashwalls.models.SearchCollectionsModel;
import com.scorpion.splashwalls.helpers.AdHelper;
import com.scorpion.splashwalls.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class SearchCollectionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<SearchCollectionsModel.Results> movieResults;
    private Context context;

    private boolean isLoadingAdded = false;

    public SearchCollectionsAdapter(Context context) {
        this.context = context;
        movieResults = new ArrayList<>();
        AdHelper.LoadInter(context);
    }

    public List<SearchCollectionsModel.Results> getMovies() {
        return movieResults;
    }

    public void setMovies(List<SearchCollectionsModel.Results> movieResults) {
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
                viewHolder = new SearchCollectionsAdapter.LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.collections_layout, parent, false);
        viewHolder = new SearchCollectionsAdapter.MovieVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final SearchCollectionsModel.Results result = movieResults.get(position); // Movie
//        Log.e("movieResultsSize", String.valueOf(result.getUrls().getFull()));
        switch (getItemViewType(position)) {
            case ITEM:
                final SearchCollectionsAdapter.MovieVH movieVH = (SearchCollectionsAdapter.MovieVH) holder;
//                String link = result.getPreview_photos()[0].getUrls().getSmall();
//                link = link.replace("w=400","w=600");
                try {
                    if (result.getPreview_photos().length == 0) {
                        movieVH.card1.setVisibility(View.VISIBLE);
                        movieVH.card2.setVisibility(View.GONE);
                        movieVH.card3.setVisibility(View.GONE);
                        Picasso.get().load(R.drawable.ic_file_upload_black_24dp).into(movieVH.image11);
                    } else if (result.getPreview_photos().length == 1) {
                        movieVH.card1.setVisibility(View.VISIBLE);
                        movieVH.card2.setVisibility(View.GONE);
                        movieVH.card3.setVisibility(View.GONE);
                        Picasso.get().load(result.getPreview_photos()[0].getUrls().getRegular()).into(movieVH.image11);
                    } else if (result.getPreview_photos().length == 2) {
                        movieVH.card1.setVisibility(View.GONE);
                        movieVH.card2.setVisibility(View.VISIBLE);
                        movieVH.card3.setVisibility(View.GONE);
                        Picasso.get().load(result.getPreview_photos()[0].getUrls().getRegular()).into(movieVH.image21);
                        Picasso.get().load(result.getPreview_photos()[1].getUrls().getRegular()).into(movieVH.image22);
                    } else {
                        movieVH.card1.setVisibility(View.GONE);
                        movieVH.card2.setVisibility(View.GONE);
                        movieVH.card3.setVisibility(View.VISIBLE);
                        Picasso.get().load(result.getPreview_photos()[0].getUrls().getSmall()).into(movieVH.image31);
                        Picasso.get().load(result.getPreview_photos()[1].getUrls().getSmall()).into(movieVH.image32);
                        Picasso.get().load(result.getPreview_photos()[2].getUrls().getSmall()).into(movieVH.image33);
                    }

//                    Log.e("COLLECTIONSTITLE",result.getTitle());
//                    Log.e("COLLECTIONSTITLE",result.getTotal_photos());
                    movieVH.title.setText(result.getTitle());
                    movieVH.detail.setText(result.getUser().getName() + " | " + result.getTotal_photos() + " photos");

                    movieVH.linearCollectionLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AdHelper.ShowInter(context);
                            context.startActivity(new Intent(context, CollectionDetailsActivity.class).putExtra("collection_id", result.getId()).setFlags(FLAG_ACTIVITY_NEW_TASK));
                        }
                    });

//                    if (position != 0)
//                        if (position % Utils.adPos == 0) {
////                            Log.e("POS", String.valueOf(position));
//
//                            AdHelper.AdLoadHelper(context,movieVH.adView, movieVH.layBanner);
//
//                        } else {
//                            movieVH.adView.setVisibility(View.GONE);
//                        }

//                    if (position != 0)
//                        if (position % Utils.adPos == 0) {
//                            ((FrameLayout)((SearchCollectionsAdapter.MovieVH) holder).ad_view_container).setVisibility(View.VISIBLE);
//                            AdView mAdView = new AdView(context);
//                            mAdView.setAdSize(AdSize.BANNER);
//                            mAdView.setAdUnitId(context.getString(R.string.banner_id));
//                            ((FrameLayout)((SearchCollectionsAdapter.MovieVH) holder).ad_view_container).addView(mAdView);
//                            AdRequest adRequest = new AdRequest.Builder().build();
//                            mAdView.loadAd(adRequest);
//                        } else {
//                            movieVH.ad_view_container.setVisibility(View.GONE);
//                        }


                } catch (Exception e) {
                }
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
        return (position == movieResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void add(SearchCollectionsModel.Results r) {
        movieResults.add(r);
        notifyItemInserted(movieResults.size() - 1);
    }

    public void addAll(List<SearchCollectionsModel.Results> moveResults) {
        for (SearchCollectionsModel.Results result : moveResults) {
            add(result);
//            Log.e("IDS", result.getId());
        }
    }

    public void remove(SearchCollectionsModel.Results r) {
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
        add(new SearchCollectionsModel.Results());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movieResults.size() - 1;
        SearchCollectionsModel.Results result = getItem(position);

        if (result != null) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public SearchCollectionsModel.Results getItem(int position) {
        return movieResults.get(position);
    }

    protected class MovieVH extends RecyclerView.ViewHolder {
        private ImageView image31, image32, image33, image21, image22, image11;
        LinearLayout card1, card2, card3;
        LinearLayout linearCollectionLayout;
        TextView title, detail;
        AdView adView;
        LinearLayout layBanner;
        FrameLayout ad_view_container;
//        private CircleImageView profile;
//        private TextView username;
//        LinearLayout ll1;

        public MovieVH(View itemView) {
            super(itemView);
            image31 = itemView.findViewById(R.id.image31);
            image32 = itemView.findViewById(R.id.image32);
            image33 = itemView.findViewById(R.id.image33);
            image21 = itemView.findViewById(R.id.image21);
            image22 = itemView.findViewById(R.id.image22);
            image11 = itemView.findViewById(R.id.image11);
            card1 = itemView.findViewById(R.id.card1);
            card2 = itemView.findViewById(R.id.card2);
            card3 = itemView.findViewById(R.id.card3);
            linearCollectionLayout = itemView.findViewById(R.id.linearCollectionLayout);
            title = itemView.findViewById(R.id.collectionName);
            detail = itemView.findViewById(R.id.detail);
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
