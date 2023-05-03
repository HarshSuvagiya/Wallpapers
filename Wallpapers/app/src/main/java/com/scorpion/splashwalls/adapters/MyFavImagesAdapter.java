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

import com.scorpion.splashwalls.R;
import com.scorpion.splashwalls.activities.ImageDetailActivity;
import com.scorpion.splashwalls.helpers.AdHelper;
import com.scorpion.splashwalls.models.ImageDetailModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MyFavImagesAdapter extends RecyclerView.Adapter<MyFavImagesAdapter.MyViewHolder> {

    ArrayList<ImageDetailModel> myList = new ArrayList<>();
    Context mContext;

    public MyFavImagesAdapter(ArrayList<ImageDetailModel> myList, Context mContext) {
        this.myList = myList;
        this.mContext = mContext;
        AdHelper.LoadInter(mContext);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.related_adapter_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(myList.get(position).getUrls().getSmall()).placeholder(R.drawable.placeholder).into(holder.image);
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
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }
}
