package com.wingsmight.makeday;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapterInGrowth extends FragmentPagerAdapter
{

    public PagerAdapterInGrowth(FragmentManager fm)
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
                fragment = new SkillsTabFragment();
                break;
            }
            case 1:
            {
                fragment = new GoalsTabFragment();
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
                pageTitle = MainActivity.GetContext().getResources().getString(R.string.Skills);
                break;
            }
            case 1:
            {
                pageTitle = MainActivity.GetContext().getResources().getString(R.string.Goals);
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
