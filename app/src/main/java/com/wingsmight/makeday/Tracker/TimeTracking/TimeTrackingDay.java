package com.wingsmight.makeday.Tracker.TimeTracking;

import java.util.ArrayList;

public class TimeTrackingDay<TI>
{
    private int day, month, year;
    private ArrayList<TI> timeIntervals;

    public TimeTrackingDay(int day, int month, int year, ArrayList<TI> timeIntervals)
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
        switch (month)
        {
            case 1:
            {
                return "Январь";
            }
            case 2:
            {
                return "Февраль";
            }
            case 3:
            {
                return "Март";
            }
            case 4:
            {
                return "Апрель";
            }
            case 5:
            {
                return "Май";
            }
            case 6:
            {
                return "Июнь";
            }
            case 7:
            {
                return "Июль";
            }
            case 8:
            {
                return "Август";
            }
            case 9:
            {
                return "Сентябрь";
            }
            case 10:
            {
                return "Октябрь";
            }
            case 11:
            {
                return "Ноябрь";
            }
            case 12:
            {
                return "Декабрь";
            }
            default:
            {
                return "???";
            }
        }
    }

    public void setMonth(int month) throws Exception
    {
        if(month >= 1 && month <= 12)
        {
            this.month = month;
        }
        else
        {
            throw new Exception("IncorrectMonthNumber");
        }

    }
}
