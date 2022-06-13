package com.example.smate.Profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.smate.Classes.UserProfile;
import com.example.smate.Login.LoginActivity;
import com.example.smate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    View view;
    TextView tvName,tvUSN,tvYear,tvBranch;
    Button btnLogout;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    UserProfile profile = new UserProfile();
    DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("user");
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        Log.e("profile","profile started");
        tvName = view.findViewById(R.id.tvName);
        tvBranch = view.findViewById(R.id.tvBranch);
        tvUSN = view.findViewById(R.id.tvUSN);
        tvYear = view.findViewById(R.id.tvYear);
        btnLogout = view.findViewById(R.id.btnLogout);

        Thread profileDataThread = new Thread(new Runnable() {
            @Override
            public void run() {
                firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            profile = snapshot.child(user.getPhoneNumber()).getValue(UserProfile.class);
                            SetValues();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            private void SetValues() {
                try {
                    tvName.setText(profile.getName());
                    tvBranch.setText(profile.getBranch());
                    tvUSN.setText(profile.getUSN());
                    tvYear.setText(profile.getYear());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        profileDataThread.start();
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }

}