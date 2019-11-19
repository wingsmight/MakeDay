package com.wingsmight.makeday;

import java.util.ArrayList;

public class TimeTrackingDay<TI>
{
    private int day, year;
    private String month;
    private ArrayList<TI> timeIntervals;

    public TimeTrackingDay(int day, String month, int year, ArrayList<TI> timeIntervals)
    {
        this.day = day;
        this.month = month;
        this.year = year;
        this.timeIntervals = timeIntervals;
    }

    public void AddTimeInterval(TI timeInterval)
    {
        timeIntervals.add(timeInterval);
    }

    public ArrayList<TI> getTimeIntervals()
    {
        return timeIntervals;
    }

    public void setTimeIntervals(ArrayList<TI> timeIntervals)
    {
        this.timeIntervals = timeIntervals;
    }

    public int getDay()
    {
        return day;
    }

    public void setDay(int day)
    {
        this.day = day;
    }

    public int getYear()
    {
        return year;
    }

    public void setYear(int year)
    {
        this.year = year;
    }

    public String getMonth()
    {
        return month;
    }

    public void setMonth(String month)
    {
        this.month = month;
    }
}
