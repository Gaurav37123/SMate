package com.example.smate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;

import com.cuberto.flashytabbarandroid.TabFlashyAnimator;
import com.example.smate.Chat.ChatFragment;
import com.example.smate.Home.HomeFragment;
import com.example.smate.Login.LoginActivity;
import com.example.smate.Login.SignUpActivity;
import com.example.smate.Profile.ProfileFragment;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class HostActivity extends AppCompatActivity {
    TabFlashyAnimator tabFlashyAnimator;
    TabLayout tabLayout;
    ViewPager viewPager;
//    CommonTabLayout commonTabLayout;


    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private String Titles[] = new String[]{"Home","Chat","Profile"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        viewPager = findViewById(R.id.myViewPager);
        tabLayout = findViewById(R.id.tabLayout);
//        commonTabLayout = findViewById(R.id.tabLayout);

        fragments.add(new HomeFragment());
        fragments.add(new ChatFragment());
        fragments.add(new ProfileFragment());

        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        tabFlashyAnimator = new TabFlashyAnimator(tabLayout);
        tabFlashyAnimator.addTabItem(Titles[0],R.drawable.ic_baseline_home_24);
        tabFlashyAnimator.addTabItem(Titles[1],R.drawable.ic_baseline_chat_bubble_24);
        tabFlashyAnimator.addTabItem(Titles[2],R.drawable.ic_baseline_person_24);
        tabFlashyAnimator.highLightTab(0);

        viewPager.addOnPageChangeListener(tabFlashyAnimator);

//        ArrayList<CustomTabEntity> tabs = new ArrayList<>();
//        tabs.add(new TabEn)
//        commonTabLayout.setTabData();
//        commonTabLayout.setViewPager(viewPager);
    }
    @Override
    protected void onStart() {
        super.onStart();
        tabFlashyAnimator.onStart((TabLayout) findViewById(R.id.tabLayout));
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user== null)
        {
            Intent intent = new Intent(HostActivity.this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        }
        FirebaseDatabase.getInstance().getReference().child("user").child(user.getPhoneNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                {
                    Intent intent = new Intent(HostActivity.this, SignUpActivity.class);
                    intent.putExtra("phoneNumber",user.getPhoneNumber().substring(3));
                    startActivity(intent);
                    HostActivity.this.finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        tabFlashyAnimator.onStop();
    }
}