package com.wcxdhr.simpleplayer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wcxdhr.simpleplayer.fragment.VideoListFragment;

import java.util.ArrayList;

public class PageAdapter extends FragmentPagerAdapter {

    ArrayList<String> mTitles;

    public PageAdapter(FragmentManager fm, ArrayList<String> mTitles) {
        super(fm);
        this.mTitles = mTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return VideoListFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
