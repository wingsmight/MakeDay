package com.wingsmight.makeday;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public abstract class TimeIntervalAdapter<TI> extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private ArrayList<TI> timeIntervals;
    private int resourceLayout;

    public TimeIntervalAdapter(ArrayList<TI> timeIntervals, int resourceLayout)
    {
        this.timeIntervals = timeIntervals;
        this.resourceLayout = resourceLayout;
    }


    @Override
    public int getItemCount() {
        return timeIntervals.size();
    }
}
