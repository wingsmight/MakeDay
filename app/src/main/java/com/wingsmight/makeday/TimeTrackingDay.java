package com.wingsmight.makeday;

import java.util.ArrayList;

public class TimeTrackingDay
{
    private int day, year;
    private String month;
    private ArrayList<Event> events;

    public TimeTrackingDay(int day, String month, int year, ArrayList<Event> events)
    {
        this.day = day;
        this.month = month;
        this.year = year;
        this.events = events;
    }

    public ArrayList<Event> getEvents()
    {
        return events;
    }

    public void setEvents(ArrayList<Event> events)
    {
        this.events = events;
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
