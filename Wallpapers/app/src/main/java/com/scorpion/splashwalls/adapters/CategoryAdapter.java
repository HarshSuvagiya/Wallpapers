package com.scorpion.splashwalls.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scorpion.splashwalls.R;
import com.scorpion.splashwalls.activities.CategoryActivity;
import com.scorpion.splashwalls.activities.ImageDetailActivity;
import com.scorpion.splashwalls.helpers.AdHelper;
import com.scorpion.splashwalls.models.CategoryModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    ArrayList<CategoryModel> categoryModelArrayList = new ArrayList<>();
    Context mContext;

    public CategoryAdapter(ArrayList<CategoryModel> categoryModelArrayList, Context mContext) {
        this.categoryModelArrayList = categoryModelArrayList;
        this.mContext = mContext;
        AdHelper.LoadInter(mContext);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater;
        View view = LayoutInflater.from(mContext).inflate(R.layout.category_adapter_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String link = categoryModelArrayList.get(position).getLink();
        link = link.replace("w=1080","w=400");
        String finalLink = link;
        String finalLink1 = link;
        Picasso.get().load(link).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.placeholder).into(holder.image, new Callback() {
            @Override
            public void onSuccess() {
                Picasso.get().load(finalLink).placeholder(R.drawable.placeholder).into(holder.image);
            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load(finalLink1).placeholder(R.drawable.placeholder).into(holder.image);
            }
        });
        holder.title.setText(categoryModelArrayList.get(position).getName());

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdHelper.ShowInter(mContext);
                ((CategoryActivity)mContext).getUpdatedCategoryImages(categoryModelArrayList.get(position).getName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
        }
    }
}
