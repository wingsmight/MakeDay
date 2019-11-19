package com.wingsmight.makeday;

public class EmotionEvent extends Event
{
    public EmotionEvent(int day, String month, int year, int hoursBefore, int minutesBefore, int hoursAfter, int minutesAfter, String eventText)
    {
        super(day, month, year, hoursBefore, minutesBefore, hoursAfter, minutesAfter, eventText);
    }
}
