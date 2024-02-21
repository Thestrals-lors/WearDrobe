package com.bl4nk.weardrobe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bl4nk.weardrobe.adapter.UserPagerAdapter;
import com.bl4nk.weardrobe.fragment.Closet;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    public TabLayout tabLayout;
    private TextView pageName;
    UserPagerAdapter userPagerAdapter;
    private FrameLayout frameLayout;
    String style = null;
    Fragment closetCurrentFragment = null;
    UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        pageName = findViewById(R.id.pageName);
        frameLayout = findViewById(R.id.closetFrameLayout);

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.home));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.recom));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.closet));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        userPagerAdapter = new UserPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(userPagerAdapter);

        int selectedColor = ContextCompat.getColor(getApplicationContext(), R.color.black);

        tabLayout.getTabAt(0).setIcon(R.drawable.home_active);
        tabLayout.getTabAt(0).getIcon().setColorFilter(selectedColor, PorterDuff.Mode.SRC_IN);
        tabLayout.setSelectedTabIndicatorColor(selectedColor);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if(position == 0){
                    pageName.setText("Home");
                } else if(position == 1){
                    pageName.setText("Recommendation");
                } else if(position == 2){
                    pageName.setText("Closet");
                }

                new Handler().postDelayed(() -> {
                    tabLayout.selectTab(tabLayout.getTabAt(position));
                }, 100);
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()){
                    case 0:
                        userViewModel.setSelectedStyle(null);
                        frameLayout.setVisibility(View.GONE);
                        viewPager.setVisibility(View.VISIBLE);
                        tabLayout.getTabAt(0).setIcon(R.drawable.home_active);
                        tabLayout.getTabAt(0).getIcon().setColorFilter(selectedColor, PorterDuff.Mode.SRC_IN);
                        break;
                    case 1:
                        frameLayout.setVisibility(View.GONE);
                        viewPager.setVisibility(View.VISIBLE);
                        tabLayout.getTabAt(1).setIcon(R.drawable.recom_active);
                        tabLayout.getTabAt(1).getIcon().setColorFilter(selectedColor, PorterDuff.Mode.SRC_IN);
                        break;
                    case 2:
                        frameLayout.setVisibility(View.GONE);
                        viewPager.setVisibility(View.VISIBLE);
                        tabLayout.getTabAt(2).setIcon(R.drawable.closet_active);
                        tabLayout.getTabAt(2).getIcon().setColorFilter(selectedColor, PorterDuff.Mode.SRC_IN);
                        break;
                }

                userPagerAdapter.notifyDataSetChanged();
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        tabLayout.getTabAt(0).setIcon(R.drawable.home);
                        break;
                    case 1:
                        tabLayout.getTabAt(1).setIcon(R.drawable.recom);
                        break;
                    case 2:
                        tabLayout.getTabAt(2).setIcon(R.drawable.closet);
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                    case 1:
                    case 2:
                        frameLayout.setVisibility(View.GONE);
                        viewPager.setVisibility(View.VISIBLE);
                        break;
                }

            }
        });

    }

    public void receiveFragment(Fragment fragment){
        replaceFragment(fragment);
    }

    private void replaceFragment(Fragment fragment) {

        frameLayout.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.GONE);

        closetCurrentFragment = fragment;

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.closetFrameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void detachFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentById(R.id.closetFrameLayout); // Find the fragment to detach
        if (fragment != null) {
            fragmentTransaction.remove(fragment); // Remove the fragment
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onBackPressed() {

        if(closetCurrentFragment instanceof Closet){
            detachFragment();
            frameLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.VISIBLE);
        }
        else {
            super.onBackPressed();
        }
    }
    public UserViewModel getSharedViewModel() {
        return userViewModel;
    }

    public void notifyPagerAdapterDataSetChanged() {
        userPagerAdapter.notifyDataSetChanged();
    }
}