package com.eggdevs.thequakeseeker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.eggdevs.thequakeseeker.fragments.HowFragment;
import com.eggdevs.thequakeseeker.fragments.RecentFragment;
import com.eggdevs.thequakeseeker.fragments.WhatFragment;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {

    private EarthquakeSectionsAdapter pagerAdapter;
    private ViewPager tabsPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ad.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        int numOfTabs = 3;
        pagerAdapter = new EarthquakeSectionsAdapter(getSupportFragmentManager(), numOfTabs);

        tabsPager = findViewById(R.id.tabsPager);
        tabsPager.setOffscreenPageLimit(2);
        tabsPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(tabsPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_refresh:
                // Refresh recent fragment
                pagerAdapter.getItem(0);
                tabsPager.setAdapter(pagerAdapter);
                return true;

            case R.id.action_follow:

                // Intent to web view activity to show quora page.
                Intent followIntent = new Intent(MainActivity.this, WebPageActivity.class);
                followIntent.putExtra("url", "https://www.quora.com/profile/Ayush-Gupta-914");
                followIntent.putExtra("ad_id", getString(R.string.interstitial_ad_1));
                startActivity(followIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Fragment tabsPager adapter to set up sliding tab view.
    private class EarthquakeSectionsAdapter extends FragmentPagerAdapter {

        public EarthquakeSectionsAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new RecentFragment();
                case 1:
                    return new HowFragment();
                case 2:
                    return new WhatFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {

                case 0:
                    return getString(R.string.recent_tab);
                case 1:
                    return getString(R.string.how_tab);
                case 2:
                    return getString(R.string.what_tab);
            }
            return null;
        }
    }
}