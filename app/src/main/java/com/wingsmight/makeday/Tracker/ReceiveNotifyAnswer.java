package com.wingsmight.makeday.Tracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.wingsmight.makeday.Tracker.TimeTracking.TimeTrackingTabFragment;

import java.util.Calendar;
import java.util.Date;

import androidx.core.app.RemoteInput;

public class ReceiveNotifyAnswer extends BroadcastReceiver
{
    public static String MyOnClick1 = "MyOnClick1";
    public static String MyOnClick2 = "MyOnClick2";
    public static String MyOnClick3 = "MyOnClick3";
    public static String MyOnClick4 = "MyOnClick4";
    public static String MyOnClick5 = "MyOnClick5";
    public static String MyOnClick6 = "MyOnClick6";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String eventText = "";
        String intentName = intent.getAction();

        if (MyOnClick1.equals(intentName))
        {
            eventText = "Читал";
        }
        else if (MyOnClick2.equals(intentName))
        {
            eventText = "Работал";
        }
        else if (MyOnClick3.equals(intentName))
        {
            eventText = "Отдыхал";
        }
        else if (MyOnClick4.equals(intentName))
        {
            eventText = "Играл на пк";
        }
        else if (MyOnClick5.equals(intentName))
        {
            eventText = "Учился";
        }
        else if (MyOnClick6.equals(intentName))
        {
            eventText = "Спорт";
        }

        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);

        if (remoteInput != null)
        {
            CharSequence replyText = remoteInput.getCharSequence("key_text_reply");

            eventText = replyText.toString();
        }


        Date date = Calendar.getInstance().getTime();

        int year = date.getYear() + 1900;
        int month = date.getMonth();
        int day = date.getDate();
        int dayOfWeek = date.getDay();
        int hours = date.getHours();
        int minutes = date.getMinutes();

        int timeDiff = TimeTrackingTabFragment.intervalTrackingTime.getMinute();
        long millisecondsDiff = timeDiff * 60000;
        long beforeDateTime = date.getTime() - millisecondsDiff;
        Date beforeDate = new Date(beforeDateTime);
        int beforeHours = beforeDate.getHours();
        int beforeMinutes = beforeDate.getMinutes();


        TimeTrackingTabFragment.addEvent(new Event(day, dayOfWeek, month, year, beforeHours, beforeMinutes, hours, minutes, eventText));
        TimeTrackingTabFragment.cancelNotification(1);
    }
}
