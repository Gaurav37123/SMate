package com.example.smate;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smate.Chat.ChatAdapter;
import com.example.smate.Chat.ChatFragment;
import com.example.smate.Chat.ChatMessage;
import com.example.smate.Classes.FeedItem;
import com.example.smate.Classes.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.Viewholder>{

    ArrayList<FeedItem> itemList;
    Context activity;
    String category;

    public FeedAdapter(Context context, ArrayList<FeedItem> list,String category)
    {
        activity = (Activity)context;
        itemList = list;
        this.category = category;
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        TextView contentDesc;
        SliderView imageSlider;
        ImageButton btnReview,btnDispDesc;
        TextView ratingBar;
        RelativeLayout ItemReview;
        RecyclerView ReviewList;
        EditText etReview;
        ImageView btnSendReview;
//        FragmentContainerView chatContainer;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            contentDesc = itemView.findViewById(R.id.ContentDesc);
            imageSlider = itemView.findViewById(R.id.imageSlider);
            btnReview = itemView.findViewById(R.id.btnReview);
            ratingBar = itemView.findViewById(R.id.RatingBar);
            btnDispDesc = itemView.findViewById(R.id.btnDispDesc);
            ItemReview = itemView.findViewById(R.id.ItemReview);
            ReviewList = itemView.findViewById(R.id.ReviewList);
            etReview = itemView.findViewById(R.id.etReview);
            btnSendReview = itemView.findViewById(R.id.btnSendReview);

            ItemReview.setVisibility(View.GONE);
//            chatContainer = itemView.findViewById(R.id.ChatContainer);
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
        int i=position;
        int Rating=itemList.get(position).getRating();
        ArrayList<ChatMessage> MessageList = new ArrayList<>();
        final UserProfile[] profile = {new UserProfile()};
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("home").child(category).child(itemList.get(i).getNode()).child("review");
        ChatAdapter reviewAdapter = new ChatAdapter(holder.ReviewList.getContext(), MessageList);
        holder.ReviewList.setLayoutManager(new LinearLayoutManager(holder.ReviewList.getContext()));
        holder.ReviewList.setAdapter(reviewAdapter);

        holder.contentDesc.setText(itemList.get(position).getContentDesc());
        holder.ratingBar.setText(itemList.get(position).getRating() + "");

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
                    holder.contentDesc.setMaxHeight(100);
                else
                    holder.contentDesc.setMaxHeight(1000);
                desc[0] = !desc[0];
            }
        });

        FirebaseDatabase.getInstance().getReference().child("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    profile[0] = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).getValue(UserProfile.class);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.ratingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("home").child(category).child(itemList.get(i).getNode()).child("rating").child(profile[0].getPhoneNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!snapshot.exists())
                        {
                            FirebaseDatabase.getInstance().getReference().child("home").child(category).child(itemList.get(i).getNode()).child("rating").child("value").setValue(Rating+1);
                            holder.ratingBar.setText(itemList.get(i).getRating()+"");
                            FirebaseDatabase.getInstance().getReference().child("home").child(category).child(itemList.get(i).getNode()).child("rating").child(profile[0].getPhoneNumber()).setValue(1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MessageList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    ChatMessage temp = dataSnapshot.getValue(ChatMessage.class);
                    MessageList.add(temp);
                }
                reviewAdapter.notifyDataSetChanged();

                holder.ReviewList.scrollToPosition(MessageList.size()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        RecyclerView.OnItemTouchListener scrollTouchListener = new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                int action = e.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        };
        holder.ReviewList.addOnItemTouchListener(scrollTouchListener);


        holder.btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.ItemReview.setVisibility(View.VISIBLE);
                holder.btnSendReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String message = holder.etReview.getText().toString().trim();
                        if(!message.isEmpty())
                        {
                            ChatMessage SendMessage = new ChatMessage();
                            Date date = new Date();
                            SendMessage.setMessage(message);
                            SendMessage.setSenderPhone(profile[0].getPhoneNumber());
                            SendMessage.setSenderName(profile[0].getName());
                            SimpleDateFormat TimeFormat = new SimpleDateFormat("hh.mm a");
                            SendMessage.setTime(TimeFormat.format(date));
                            SimpleDateFormat DateFormat = new SimpleDateFormat("dd.mm.yy");
                            SendMessage.setDate(DateFormat.format(date));
                            databaseReference.push().setValue(SendMessage);
                            holder.etReview.setText("");
                            reviewAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
