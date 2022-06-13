package com.example.smate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cuberto.flashytabbarandroid.TabFlashyAnimator;
import com.example.smate.Chat.ChatFragment;
import com.example.smate.Home.HomeFragment;
import com.example.smate.Login.LoginActivity;
import com.example.smate.Profile.ProfileFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class HostActivity extends AppCompatActivity {
    TabFlashyAnimator tabFlashyAnimator;
    TabLayout tabLayout;
    ViewPager viewPager;

    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private String Titles[] = new String[]{"Home","Chat","Profile"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        viewPager = findViewById(R.id.myViewPager);
        tabLayout = findViewById(R.id.tabLayout);
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
    }

    @Override
    protected void onStop() {
        super.onStop();
        tabFlashyAnimator.onStop();
    }
}