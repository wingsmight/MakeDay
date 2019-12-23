package com.wingsmight.makeday.Tracker;

public abstract class TimeInterval
{
    private int day, dayOfWeek, month, year, hoursBefore, minutesBefore, hoursAfter, minutesAfter;

    public TimeInterval(int day, int dayOfWeek, int month, int year, int hoursBefore, int minutesBefore, int hoursAfter, int minutesAfter)
    {
        this.day = day;
        this.dayOfWeek = dayOfWeek;
        this.month = month;
        this.year = year;
        this.hoursBefore = hoursBefore;
        this.minutesBefore = minutesBefore;
        this.hoursAfter = hoursAfter;
        this.minutesAfter = minutesAfter;
    }

    public String getTimeInterval24()
    {
        return ToHourFormat(hoursBefore) + ":" + ToHourFormat(minutesBefore) + " - " + ToHourFormat(hoursAfter) + ":" + ToHourFormat(minutesAfter);
    }

    private String ToHourFormat(int time)
    {
        if(time >= 10)
        {
            return String.valueOf(time);
        }
        else
        {
            return "0" + String.valueOf(time);
        }
    }

    public int getDayOfWeek()
    {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek)
    {
        this.dayOfWeek = dayOfWeek;
    }

    public int getDay()
    {
        return day;
    }

    public void setDay(int day)
    {
        this.day = day;
    }

    public int getMonth()
    {
        return month;
    }

    public void setMonth(int month)
    {
        this.month = month;
    }

    public int getYear()
    {
        return year;
    }

    public void setYear(int year)
    {
        this.year = year;
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
}
