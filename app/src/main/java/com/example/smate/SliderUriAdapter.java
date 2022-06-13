package com.example.smate;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;


        import android.content.Context;
        import android.graphics.Color;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.bumptech.glide.Glide;
        import com.smarteist.autoimageslider.SliderViewAdapter;

        import java.util.ArrayList;
        import java.util.List;

public class SliderUriAdapter extends SliderViewAdapter<SliderUriAdapter.SliderAdapterVH> {

    private Context context;
    private List<Uri> mSliderItems = new ArrayList<>();

    public SliderUriAdapter(Context context, ArrayList<Uri> items) {
        this.context = context;
        mSliderItems = items;
    }

//        public void renewItems(List<SliderItem> sliderItems) {
//            this.mSliderItems = sliderItems;
//            notifyDataSetChanged();
//        }
//
//        public void deleteItem(int position) {
//            this.mSliderItems.remove(position);
//            notifyDataSetChanged();
//        }
//
//        public void addItem(SliderItem sliderItem) {
//            this.mSliderItems.add(sliderItem);
//            notifyDataSetChanged();
//        }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        Uri sliderItem = mSliderItems.get(position);

//            viewHolder.textViewDescription.setText(sliderItem.getDescription());
//            viewHolder.textViewDescription.setTextSize(16);
//            viewHolder.textViewDescription.setTextColor(Color.WHITE);
        Glide.with(viewHolder.itemView)
                .load(sliderItem)
                .fitCenter()
                .into(viewHolder.imageViewBackground);

//            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(context, "This is item in position " + position, Toast.LENGTH_SHORT).show();
//                }
//            });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        TextView imageGifContainer;
        //TextView textViewDescription;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
//                textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }

}

