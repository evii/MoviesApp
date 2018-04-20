package com.example.android.moviesapp;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = "DetailActivity";

    FragmentPagerAdapter adapterViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ViewPager vpPager = findViewById(R.id.pager_header);
        adapterViewPager = new DetailMoviePagerAdapter(this, getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(vpPager);
    }

    public static class DetailMoviePagerAdapter extends FragmentPagerAdapter {
        private static int NUMBER_ITEMS = 3;
        private Context mContext;

        public DetailMoviePagerAdapter(Context context, FragmentManager fragmentManager) {
            super(fragmentManager);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new DetailsFragment();
            } else if (position == 1) {
                return new TrailersFragment();
            } else {
                return new ReviewsFragment();
            }
        }

        @Override
        public int getCount() {
            return NUMBER_ITEMS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return mContext.getString(R.string.details);
            } else if (position == 1) {
                return mContext.getString(R.string.trailers);
            } else {
                return mContext.getString(R.string.reviews);
            }
        }
    }
}




