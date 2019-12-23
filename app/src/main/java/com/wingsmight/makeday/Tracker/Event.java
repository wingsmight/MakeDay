package com.wingsmight.makeday.Tracker;

public class Event extends TimeInterval
{
    private String eventText;

    public Event(int day, int dayOfWeek, int month, int year, int hoursBefore, int minutesBefore, int hoursAfter, int minutesAfter, String eventText)
    {
        super(day, dayOfWeek, month, year, hoursBefore, minutesBefore, hoursAfter, minutesAfter);

        this.eventText = eventText;
    }

    public String getEventText()
    {
        return eventText;
    }

    public void setEventText(String eventText)
    {
        this.eventText = eventText;
    }
}
