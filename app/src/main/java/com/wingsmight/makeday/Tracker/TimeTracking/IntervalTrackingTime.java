package com.wingsmight.makeday.Tracker.TimeTracking;

import androidx.annotation.NonNull;

public class IntervalTrackingTime
{
    private int minute;

    public IntervalTrackingTime(int minute)
    {
        this.minute = minute;
    }

    public int getMinute()
    {
        return minute;
    }

    public String getTime()
    {
        String time;
        int minute = this.minute;
        if(minute >= 60)
        {
            int hour = minute / 60;
            minute %= 60;

            if(minute != 0)
            {
                time = Integer.toString(hour) + " час " +  Integer.toString(minute) + " минут";
            }
            else
            {
                time = Integer.toString(hour) + " час";
            }
        }
        else
        {
            time = Integer.toString(minute) + " минут";
        }

        return time;
    }

    @NonNull
    @Override
    public String toString()
    {
        return getTime();
    }
}
