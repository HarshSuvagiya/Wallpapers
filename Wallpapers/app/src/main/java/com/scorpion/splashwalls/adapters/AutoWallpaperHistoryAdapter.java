package com.scorpion.splashwalls.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.scorpion.splashwalls.R;
import com.scorpion.splashwalls.activities.ImageDetailActivity;
import com.scorpion.splashwalls.helpers.AdHelper;
import com.scorpion.splashwalls.models.AutoWallpaperHistoryModel;
import com.scorpion.splashwalls.utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class AutoWallpaperHistoryAdapter extends RecyclerView.Adapter<AutoWallpaperHistoryAdapter.MyViewHolder> {

    ArrayList<AutoWallpaperHistoryModel> myList = new ArrayList<>();
    Context mContext;

    public AutoWallpaperHistoryAdapter(ArrayList<AutoWallpaperHistoryModel> myList, Context mContext) {
        this.myList = myList;
        this.mContext = mContext;
        AdHelper.LoadInter(mContext);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.auto_wallaper_history_adapter_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(myList.get(position).getImageDetailModel().getUrls().getRegular()).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.placeholder).into(holder.image, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load(myList.get(position).getImageDetailModel().getUrls().getRegular()).placeholder(R.drawable.placeholder).into(holder.image);
            }
        });
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdHelper.ShowInter(mContext);
                mContext.startActivity(new Intent(mContext, ImageDetailActivity.class).putExtra("image_id",myList.get(position).getImageDetailModel().getId()).setFlags(FLAG_ACTIVITY_NEW_TASK));
            }
        });

        long diff = System.currentTimeMillis() - myList.get(position).getTimeStamp();

        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        holder.name.setText("By " + myList.get(position).getImageDetailModel().getUser().getName());
        if (diffDays != 0)
            holder.time.setText(String.valueOf(diffDays + " days ago"));
        else if (diffHours != 0)
            holder.time.setText(String.valueOf(diffHours + " hours ago"));
        else if (diffMinutes != 0)
            holder.time.setText(String.valueOf(diffMinutes + " minutes ago"));
        else
            holder.time.setText(String.valueOf(diffSeconds + " seconds ago"));


//        if (position != 0)
//            if (position % Utils.adPos == 0) {
//
//                AdView mAdView = new AdView(mContext);
//                mAdView.setAdSize(AdSize.BANNER);
//                mAdView.setAdUnitId(mContext.getString(R.string.banner_id));
//                ((LinearLayout)holder.layBanner).addView(mAdView);
//                AdRequest adRequest = new AdRequest.Builder().build();
//                mAdView.loadAd(adRequest);
//
////                AdRequest adRequest = new AdRequest.Builder().build();
////                holder.adView.setAdSize();
////                holder.adView.loadAd(adRequest);
////                holder.adView.setVisibility(View.VISIBLE);
////                AdHelper.AdLoadHelper(mContext,holder.adView,holder.layBanner );
//            } else {
//                holder.adView.setVisibility(View.GONE);
//            }


    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name,time;
        AdView adView;
        LinearLayout layBanner;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            adView = itemView.findViewById(R.id.adView);
            layBanner = itemView.findViewById(R.id.banner_container);
        }
    }
}
