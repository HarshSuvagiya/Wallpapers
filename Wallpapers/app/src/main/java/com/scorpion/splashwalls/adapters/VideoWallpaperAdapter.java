package com.scorpion.splashwalls.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scorpion.splashwalls.R;
import com.scorpion.splashwalls.helpers.AdHelper;
import com.scorpion.splashwalls.models.VideoWallpaperModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class VideoWallpaperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<VideoWallpaperModel.Videos> movieResults;
    private Context context;

    private boolean isLoadingAdded = false;

    public VideoWallpaperAdapter(Context context) {
        this.context = context;
        movieResults = new ArrayList<>();
        AdHelper.LoadInter(context);
    }

    public List<VideoWallpaperModel.Videos> getMovies() {
        return movieResults;
    }

    public void setMovies(List<VideoWallpaperModel.Videos> movieResults) {
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
                viewHolder = new VideoWallpaperAdapter.LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.video_wallpaper_adapter_layout, parent, false);
        viewHolder = new VideoWallpaperAdapter.MovieVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final VideoWallpaperModel.Videos result = movieResults.get(position); // Movie
//        Log.e("movieResultsSize", String.valueOf(result.getUrls().getFull()));
        switch (getItemViewType(position)) {
            case ITEM:
                final VideoWallpaperAdapter.MovieVH movieVH = (VideoWallpaperAdapter.MovieVH) holder;
                String link = result.getImage();
//                link = link.replace("w=400","w=600");
                Picasso.get().load(link).placeholder(R.drawable.placeholder).networkPolicy(NetworkPolicy.OFFLINE).into(movieVH.image, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(link).into(movieVH.image);
                    }
                });


                movieVH.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        movieVH.videoView.setVideoURI(Uri.parse(result.getVideo_files()[0].getLink()));
                        movieVH.videoView.start();
                        movieVH.image.setVisibility(View.GONE);
                    }
                });

                break;

            case LOADING:
                break;
        }

    }

    @Override
    public int getItemCount() {
        return movieResults == null ? 0 : movieResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == movieResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void add(VideoWallpaperModel.Videos r) {
        movieResults.add(r);
        notifyItemInserted(movieResults.size() - 1);
    }

    public void addAll(List<VideoWallpaperModel.Videos> moveResults) {
        for (VideoWallpaperModel.Videos result : moveResults) {
            add(result);
//            Log.e("IDS",result.getId());
        }
    }

    public void remove(VideoWallpaperModel.Videos r) {
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
        add(new VideoWallpaperModel.Videos());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movieResults.size() - 1;
        VideoWallpaperModel.Videos result = getItem(position);

        if (result != null) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public VideoWallpaperModel.Videos getItem(int position) {
        return movieResults.get(position);
    }

    protected class MovieVH extends RecyclerView.ViewHolder {
        private ImageView image;
        VideoView videoView;
//        private CircleImageView profile;
//        private TextView username;
//        LinearLayout ll1;

        public MovieVH(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            videoView = itemView.findViewById(R.id.videoView);
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
