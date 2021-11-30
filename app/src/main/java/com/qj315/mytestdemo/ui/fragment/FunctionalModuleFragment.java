package com.qj315.mytestdemo.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.mytestdemo.R;
import com.qj315.mytestdemo.bean.FragmentInfo;
import com.qj315.mytestdemo.ui.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 **********     Github https://github.com/lmy8848
 **********     @author 麒玖网络QJ315 (NJQ-PRO)
 **********     @address     NJQ-PC
 **********     @time 2021/10/29 20:42
 */
public class FunctionalModuleFragment extends Fragment {
    View view;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.message_fragment_music_list,container,false);
        initView();
        initFragments();
        initTablayout();
        return view;
    }

        private List<FragmentInfo>  initFragments(){
            List<FragmentInfo> mFragments = new ArrayList<>(4);
            mFragments.add(new FragmentInfo("官方博客",WebQJ315Fragment.class));
            mFragments.add(new FragmentInfo("留言列表",MessageFragment.class));
            mFragments.add(new FragmentInfo("音乐列表",MusicListFragment.class));
            mFragments.add(new FragmentInfo("在线音乐",MobMusicFragment.class));
            return  mFragments;
        }

    private void initTablayout() {
        PagerAdapter adapter = new
                ViewPagerAdapter(requireActivity().getSupportFragmentManager(),initFragments());
        mViewPager.setOffscreenPageLimit(adapter.getCount());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(3);

        // Tablayout 关联 viewPager
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initView() {
        mTabLayout = view.findViewById(R.id.tab_layout);
        mViewPager = view.findViewById(R.id.view_page);
    }
}
