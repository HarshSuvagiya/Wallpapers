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

import com.scorpion.splashwalls.R;
import com.scorpion.splashwalls.activities.CollectionDetailsActivity;
import com.scorpion.splashwalls.helpers.AdHelper;
import com.scorpion.splashwalls.models.UserCollectionsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class CollectionsRelatedAdapter extends RecyclerView.Adapter<CollectionsRelatedAdapter.MyViewHolder> {

    List<UserCollectionsModel> myList = new ArrayList<>();
    Context mContext;

    public CollectionsRelatedAdapter(List<UserCollectionsModel> myList, Context mContext) {
        this.myList = myList;
        this.mContext = mContext;
        AdHelper.LoadInter(mContext);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.collections_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder movieVH, final int position) {
        UserCollectionsModel result = myList.get(position);
//        Log.e("DESC",result.getDescription());
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

            movieVH.title.setText(result.getTitle());
            movieVH.detail.setText(result.getUser().getName() + " | " +result.getTotal_photos() + " photos");

            movieVH.linearCollectionLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AdHelper.ShowInter( mContext);
                    mContext.startActivity(new Intent(mContext, CollectionDetailsActivity.class).putExtra("collection_id", result.getId()).setFlags(FLAG_ACTIVITY_NEW_TASK));
                }
            });

        }
        catch (Exception e){}
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView image31, image32, image33, image21, image22, image11;
        LinearLayout card1, card2, card3;
        LinearLayout linearCollectionLayout;
        TextView title, detail;

        public MyViewHolder(@NonNull View itemView) {
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

        }
    }
}
