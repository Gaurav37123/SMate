package com.example.smate;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smate.Classes.FeedItem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.Viewholder>{

    ArrayList<FeedItem> itemList;

    public FeedAdapter(Context context, ArrayList<FeedItem> list)
    {
        itemList = list;
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        TextView contentDesc;
        SliderView imageSlider;
        Button btnDownload,btnDispDesc;
        MaterialRatingBar ratingBar;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            contentDesc = itemView.findViewById(R.id.ContentDesc);
            imageSlider = itemView.findViewById(R.id.imageSlider);
            btnDownload = itemView.findViewById(R.id.btnDownload);
            ratingBar = itemView.findViewById(R.id.RatingBar);
            btnDispDesc = itemView.findViewById(R.id.btnDispDesc);
        }
    }
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item,parent,false);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        final boolean[] desc = {false};
        holder.ratingBar.setRating(itemList.get(position).getRating());
        holder.contentDesc.setText(itemList.get(position).getContentDesc());
        MySliderAdapter adapter = new MySliderAdapter(holder.imageSlider.getContext(), itemList.get(position).getUrl());
        holder.imageSlider.setSliderAdapter(adapter);
        holder.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        holder.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        holder.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        holder.imageSlider.setIndicatorSelectedColor(Color.WHITE);
        holder.imageSlider.setIndicatorUnselectedColor(Color.GRAY);
        holder.imageSlider.setScrollTimeInSec(4); //set scroll delay in seconds :
        holder.imageSlider.startAutoCycle();
        holder.btnDispDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!desc[0])
                    holder.contentDesc.setVisibility(View.VISIBLE);
                else
                    holder.contentDesc.setVisibility(View.GONE);
                desc[0] = !desc[0];
            }
        });
        if(itemList.get(position).getDownload().compareToIgnoreCase("null")==0)
        {
            holder.btnDownload.setVisibility(View.GONE);
        }
        else{
            holder.btnDownload.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
