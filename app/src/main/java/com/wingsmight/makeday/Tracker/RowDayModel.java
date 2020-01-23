package com.wingsmight.makeday.Tracker;

import com.wingsmight.makeday.MainActivity;
import com.wingsmight.makeday.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RowDayModel
{
    int day, dayOfWeek, month, year;
    String monthName;
    ArrayList<String> doneDeals = new ArrayList<>();
    ArrayList<String> undoneDeals = new ArrayList<>();
    ArrayList<String> almostDeals = new ArrayList<>();

    public RowDayModel(int day, int dayOfWeek, int month, int year, ArrayList<String> undoneDeals, ArrayList<String> doneDeals, ArrayList<String> almostDeals)
    {
        this.day = day;
        this.dayOfWeek = dayOfWeek;
        this.year = year;
        this.month = month;
        this.doneDeals = doneDeals;
        this.undoneDeals = undoneDeals;
        this.almostDeals = almostDeals;
        this.monthName = monthToNamingMonth(month);
    }

    private String monthToNamingMonth(int monthNumber)
    {
        Calendar cal=Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        cal.set(Calendar.MONTH, monthNumber);
        String monthName = month_date.format(cal.getTime());

        return monthName;
    }

    public int getDay()
    {
        return day;
    }

    public String getDayOfWeek()
    {
        switch (dayOfWeek + 1) {
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

    public int getMonth()
    {
        return month;
    }

    public void setMonth(int month)
    {
        this.month = month;
    }

    public String getMonthName()
    {
        return monthName;
    }

    public void setMonthName(String monthName)
    {
        this.monthName = monthName;
    }

    public String getDoneString()
    {
        if(doneDeals == null) return "";

        String doneString = MainActivity.GetContext().getResources().getString(R.string.Done) + ":";
        for (int i = 0; i < doneDeals.size() - 1; i++)
        {
            doneString += " " + doneDeals.get(i) + ",";
        }
        doneString += " " + doneDeals.get(doneDeals.size() - 1);

        return doneString;
    }

    public String getUndoneString()
    {
        if(undoneDeals == null) return "";

        String undoneString = MainActivity.GetContext().getResources().getString(R.string.Undone) + ":";
        for (int i = 0; i < undoneDeals.size() - 1; i++)
        {
            undoneString += " " + undoneDeals.get(i) + ",";
        }
        undoneString += " " + undoneDeals.get(undoneDeals.size() - 1);

        return undoneString;
    }

    public String getAlmostString()
    {
        if(almostDeals == null) return "";

        String almmostString = MainActivity.GetContext().getResources().getString(R.string.Almost) + ":";
        for (int i = 0; i < almostDeals.size() - 1; i++)
        {
            almmostString += " " + almostDeals.get(i) + ",";
        }
        almmostString += " " + almostDeals.get(almostDeals.size() - 1);

        return almmostString;
    }

    public ArrayList<String> getDoneDeals()
    {
        return doneDeals;
    }

    public void setDoneDeals(ArrayList<String> doneDeals)
    {
        this.doneDeals = doneDeals;
    }

    public ArrayList<String> getUndoneDeals()
    {
        return undoneDeals;
    }

    public void setUndoneDeals(ArrayList<String> undoneDeals)
    {
        this.undoneDeals = undoneDeals;
    }

    public ArrayList<String> getAlmostDeals()
    {
        return almostDeals;
    }

    public void setAlmostDeals(ArrayList<String> almostDeals)
    {
        this.almostDeals = almostDeals;
    }

    public void addUndoneDeals(String undoneDeal)
    {
        if(undoneDeals == null)
        {
            undoneDeals = new ArrayList<String>();
        }

        undoneDeals.add(undoneDeal);
    }

    public void addDoneDeals(String doneDeal)
    {
        if(doneDeals == null)
        {
            doneDeals = new ArrayList<String>();
        }

        doneDeals.add(doneDeal);
    }

    public void addAlmostDeals(String almostDeal)
    {
        if(almostDeals == null)
        {
            almostDeals = new ArrayList<String>();
        }

        almostDeals.add(almostDeal);
    }

    public boolean equalsToDate(Date date)
    {
        return ((date.getYear() + 1900) == year && date.getMonth() == month && date.getDate() == day);
    }
}
