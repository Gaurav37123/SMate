package com.example.smate.Home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smate.FeedActivity;
import com.example.smate.R;

public class HomeFragment extends Fragment {

    CardView Notes,Sports,Food,Essentials,Tourist,Bulletin;
    View view;
    public HomeFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Notes = view.findViewById(R.id.Notes);
//        Notes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getContext(), FeedActivity.class);
//                startActivity(intent);
//            }
//        });
//    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("Home","home started");
        Notes = view.findViewById(R.id.Notes);
        Sports = view.findViewById(R.id.Sports);
        Food = view.findViewById(R.id.Food);
        Essentials = view.findViewById(R.id.Essentials);
        Tourist = view.findViewById(R.id.Tourist);
        Bulletin = view.findViewById(R.id.Bulletin);

        Intent intent = new Intent(getContext(), FeedActivity.class);

        Notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("category","Notes");
                startActivity(intent);
            }
        });
        Sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("category","Sports");
                startActivity(intent);
            }
        });
        Food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("category","Food");
                startActivity(intent);
            }
        });
        Essentials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("category","Essentials");
                startActivity(intent);
            }
        });
        Tourist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("category","Tourist");
                startActivity(intent);
            }
        });
        Bulletin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("category","Bulletin");
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }
}