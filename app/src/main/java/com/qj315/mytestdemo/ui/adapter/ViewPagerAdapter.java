package com.qj315.mytestdemo.ui.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.qj315.mytestdemo.bean.FragmentInfo;

import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<FragmentInfo> mFragments;

    public ViewPagerAdapter(FragmentManager fm, List<FragmentInfo> fragments) {
        super(fm);

        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {


        try {
            return (Fragment) mFragments.get(position).getFragment().newInstance();

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return  null;

    }

    @Override
    public int getCount() {
        return mFragments.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mFragments.get(position).getTitle();
    }
}
