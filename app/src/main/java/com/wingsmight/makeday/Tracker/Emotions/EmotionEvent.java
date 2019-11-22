package com.wingsmight.makeday.Tracker.Emotions;

import com.wingsmight.makeday.Tracker.TimeInterval;

public class EmotionEvent extends TimeInterval
{
    String emotion;

    public EmotionEvent(int day, int month, int year, int hoursBefore, int minutesBefore, int hoursAfter, int minutesAfter, String emotion)
    {
        super(day, month, year, hoursBefore, minutesBefore, hoursAfter, minutesAfter);

        this.emotion = emotion;
    }

    public String getEmotion()
    {
        return emotion;
    }

    public void setEmotion(String emotion)
    {
        this.emotion = emotion;
    }
}
