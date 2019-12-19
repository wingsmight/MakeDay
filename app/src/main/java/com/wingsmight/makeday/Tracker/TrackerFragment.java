package com.wingsmight.makeday.Tracker;

import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wingsmight.makeday.R;

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
