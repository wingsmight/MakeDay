package com.wingsmight.makeday;

public class Event
{
    private int year, day, hoursBefore, minutesBefore, hoursAfter, minutesAfter;

    private String eventText, month;

    public Event(int day, String month, int year, int hoursBefore, int minutesBefore, int hoursAfter, int minutesAfter, String eventText)
    {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hoursBefore = hoursBefore;
        this.minutesBefore = minutesBefore;
        this.hoursAfter = hoursAfter;
        this.minutesAfter = minutesAfter;
        this.eventText = eventText;
    }


    public int getYear()
    {
        return year;
    }

    public void setYear(int year)
    {
        this.year = year;
    }

    public int getDay()
    {
        return day;
    }

    public void setDay(int day)
    {
        this.day = day;
    }

    public int getHoursBefore()
    {
        return hoursBefore;
    }

    public void setHoursBefore(int hoursBefore)
    {
        this.hoursBefore = hoursBefore;
    }

    public int getMinutesBefore()
    {
        return minutesBefore;
    }

    public void setMinutesBefore(int minutesBefore)
    {
        this.minutesBefore = minutesBefore;
    }

    public int getHoursAfter()
    {
        return hoursAfter;
    }

    public void setHoursAfter(int hoursAfter)
    {
        this.hoursAfter = hoursAfter;
    }

    public int getMinutesAfter()
    {
        return minutesAfter;
    }

    public void setMinutesAfter(int minutesAfter)
    {
        this.minutesAfter = minutesAfter;
    }

    public String getEventText()
    {
        return eventText;
    }

    public void setEventText(String eventText)
    {
        this.eventText = eventText;
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
