package com.wingsmight.makeday.Tracker;

import com.wingsmight.makeday.MainActivity;
import com.wingsmight.makeday.R;

public class RowDayModel
{
    int day, year;
    String month;
    String[] doneDeals, undoneDeals;

    public RowDayModel(int day, String month, int year, String[] doneDeals, String[] undoneDeals)
    {
        this.day = day;
        this.year = year;
        this.month = month;
        this.doneDeals = doneDeals;
        this.undoneDeals = undoneDeals;
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
