package com.wingsmight.makeday.Tracker;

import com.wingsmight.makeday.MainActivity;
import com.wingsmight.makeday.R;

import java.util.Calendar;

public class RowDayModel
{
    int day, dayOfWeek, year;
    String month;
    String[] doneDeals, undoneDeals;

    public RowDayModel(int day, int dayOfWeek, int year, String[] doneDeals, String[] undoneDeals, String month)
    {
        this.day = day;
        this.dayOfWeek = dayOfWeek;
        this.year = year;
        this.month = month;
        this.doneDeals = doneDeals;
        this.undoneDeals = undoneDeals;
    }

    public int getDay()
    {
        return day;
    }

    public String getDayOfWeek()
    {
        switch (day) {
            case Calendar.MONDAY:
            {
                return "Пнд";
            }
            case Calendar.TUESDAY:
            {
                return "Втр";
            }
            case Calendar.WEDNESDAY:
            {
                return "Срд";
            }
            case Calendar.THURSDAY:
            {
                return "Чтв";
            }
            case Calendar.FRIDAY:
            {
                return "Птн";
            }
            case Calendar.SATURDAY:
            {
                return "Суб";
            }
            case Calendar.SUNDAY:
            {
                return "Вск";
            }
        }

        return "Пн";
    }

    public void setDayOfWeek(int dayOfWeek)
    {
        this.dayOfWeek = dayOfWeek;
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

    public String getDoneString()
    {
        String doneString = MainActivity.GetContext().getResources().getString(R.string.Done) + ":";
        for (int i = 0; i < doneDeals.length - 1; i++)
        {
            doneString += " " + doneDeals[i] + ",";
        }
        doneString += " " + doneDeals[doneDeals.length - 1];

        return doneString;
    }

    public String getUndoneString()
    {
        String undoneString = MainActivity.GetContext().getResources().getString(R.string.Undone) + ":";
        for (int i = 0; i < undoneDeals.length - 1; i++)
        {
            undoneString += " " + undoneDeals[i] + ",";
        }
        undoneString += " " + undoneDeals[undoneDeals.length - 1];

        return undoneString;
    }

    public String[] getDoneDeals()
    {
        return doneDeals;
    }

    public void setDoneDeals(String[] doneDeals)
    {
        this.doneDeals = doneDeals;
    }

    public String[] getUndoneDeals()
    {
        return undoneDeals;
    }

    public void setUndoneDeals(String[] undoneDeals)
    {
        this.undoneDeals = undoneDeals;
    }
}
