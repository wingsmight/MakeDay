package com.wingsmight.makeday.Tracker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wingsmight.makeday.MainActivity;
import com.wingsmight.makeday.R;
import com.wingsmight.makeday.Tracker.Emotions.EmotionsTabFragment;
import com.wingsmight.makeday.Tracker.TimeTracking.TimeTrackingTabFragment;

public class PagerAdapterInTracker extends FragmentPagerAdapter
{

    public PagerAdapterInTracker(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int postion)
    {
        Fragment fragment = null;
        switch (postion)
        {
            case 0:
            {
                fragment = new TimeTrackingTabFragment();
                break;
            }
            case 1:
            {
                fragment = new EmotionsTabFragment();
                break;
            }
        }

        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        CharSequence pageTitle = null;
        switch (position)
        {
            case 0:
            {
                pageTitle = MainActivity.GetContext().getResources().getString(R.string.TimeTracking);
                break;
            }
            case 1:
            {
                pageTitle = MainActivity.GetContext().getResources().getString(R.string.Emotions);
                break;
            }
        }

        return  pageTitle;
    }

    @Override
    public int getCount()
    {
        return 2;
    }
}
