package com.wingsmight.makeday;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TrackerFragment extends Fragment
{
    private PagerAdapterInTracker pagerAdapterInTracker;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_tracker, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        SetupTabLayout();
    }

    private void SetupTabLayout()
    {
        pagerAdapterInTracker = new PagerAdapterInTracker(getChildFragmentManager());

        viewPager = getView().findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapterInTracker);

        TabLayout tabLayout = getView().findViewById(R.id.tracker_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}