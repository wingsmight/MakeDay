package com.wingsmight.makeday.Growth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.wingsmight.makeday.Growth.Goals.GoalsTabFragment;
import com.wingsmight.makeday.Growth.Skills.SkillsTabFragment;
import com.wingsmight.makeday.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class GrowthFragment extends Fragment
{
    private PagerAdapterInGrowth pagerAdapterInGrowth;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_growth, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        SetupTabLayout();
    }

    private void SetupTabLayout()
    {
        pagerAdapterInGrowth = new PagerAdapterInGrowth(getChildFragmentManager());

        viewPager = getView().findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapterInGrowth);

        TabLayout tabLayout = getView().findViewById(R.id.tracker_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
