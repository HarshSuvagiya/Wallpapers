package com.scorpion.splashwalls.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdView;
import com.scorpion.splashwalls.R;
import com.scorpion.splashwalls.activities.ImageDetailActivity;
import com.scorpion.splashwalls.helpers.AdHelper;
import com.scorpion.splashwalls.models.ImageRelatedModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ImageRelatedAdapter extends RecyclerView.Adapter<ImageRelatedAdapter.MyViewHolder> {

    List<ImageRelatedModel.Result> myList = new ArrayList<>();
    Context mContext;

    public ImageRelatedAdapter(List<ImageRelatedModel.Result> myList, Context mContext) {
        this.myList = myList;
        this.mContext = mContext;
        AdHelper.LoadInter(mContext);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.related_adapter_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        String link = myList.get(position).getUrls().getSmall();
        link = link.replace("w=400","w=500");
        final String finalLink = link;
        Picasso.get().load(link).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.placeholder).into(holder.image, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load(finalLink).placeholder(R.drawable.placeholder).into(holder.image);
            }
        });

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdHelper.ShowInter(mContext);
                mContext.startActivity(new Intent(mContext, ImageDetailActivity.class).putExtra("image_id",myList.get(position).getId()).setFlags(FLAG_ACTIVITY_NEW_TASK));
            }
        });
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
//        LinearLayout ll1;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
//            ll1 = itemView.findViewById(R.id.ll1);
//            ll1.setVisibility(View.GONE);

        }
    }
}
