package com.scorpion.splashwalls.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scorpion.splashwalls.R;
import com.scorpion.splashwalls.models.VideoWallpaperModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TmpVideoWallpaperAdapter extends RecyclerView.Adapter<TmpVideoWallpaperAdapter.MyViewHolder> {

    List<VideoWallpaperModel.Videos> myList = new ArrayList<>();
    Context mContext;

    public TmpVideoWallpaperAdapter(List<VideoWallpaperModel.Videos> myList, Context mContext) {
        this.myList = myList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.video_wallpaper_adapter_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(myList.get(position).getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }
}
