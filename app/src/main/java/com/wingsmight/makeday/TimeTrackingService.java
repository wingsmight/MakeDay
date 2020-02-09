package com.wingsmight.makeday;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wingsmight.makeday.Tracker.Event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

public class TimeTrackingService extends Service
{

    public void onCreate()
    {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId)
    {
        showNotification();

        return START_STICKY;
    }

    AlertDialog dialog;
    public void showNotification()
    {
        long trackingInterval = loadTrackingInterval();

        Date beforeDate = new Date(Calendar.getInstance().getTimeInMillis() - trackingInterval);
        Date afterDate = Calendar.getInstance().getTime();

        int beforeHour = beforeDate.getHours();
        int beforeMinutes = beforeDate.getMinutes();;

        int afterHour = afterDate.getHours();
        int afterMinutes = afterDate.getMinutes();

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.AppTheme_MaterialDialogTheme);

        View dialogView = createDialogView(beforeHour, beforeMinutes, afterHour, afterMinutes);

        dialogBuilder.setView(dialogView);

        if(dialog != null)
        {
            dialog.dismiss();
        }
        dialog = dialogBuilder.create();
        final Window dialogWindow = dialog.getWindow();
        final WindowManager.LayoutParams dialogWindowAttributes = dialogWindow.getAttributes();

// Set fixed width (280dp) and WRAP_CONTENT height
        final WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogWindowAttributes);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.TOP);
        dialogWindow.requestFeature(Window.FEATURE_NO_TITLE);

// Set to TYPE_SYSTEM_ALERT so that the Service can display it
        dialogWindow.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }

    private long loadTrackingInterval()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getLong("trackingInterval", 1000 * 60 * 15);
    }

    private View createDialogView(int beforeHours, int beforeMinutes, int afterHours, int afterMinutes)
    {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View dialogView = inflater.inflate(R.layout.time_tracking_notification, null);

        ((TextView)(dialogView.findViewById(R.id.WhatWasYouDoing))).setText("Чем занимались с " + beforeHours + ":" + minutesToFullFormat(Integer.toString(beforeMinutes))
                + " до " + afterHours + ":" + minutesToFullFormat(Integer.toString(afterMinutes)) + "?");

        Button[] notifyButtons = new Button[6];
        notifyButtons[0] = dialogView.findViewById(R.id.notifyButton1);
        notifyButtons[1] = dialogView.findViewById(R.id.notifyButton2);
        notifyButtons[2] = dialogView.findViewById(R.id.notifyButton3);
        notifyButtons[3] = dialogView.findViewById(R.id.notifyButton4);
        notifyButtons[4] = dialogView.findViewById(R.id.notifyButton5);
        notifyButtons[5] = dialogView.findViewById(R.id.notifyButton6);

        for (Button notifyButton : notifyButtons)
        {
            notifyButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    clickOnEventButton((Button)v);
                }
            });
        }

        ImageButton sendButton = dialogView.findViewById(R.id.sendEventButton);
        final EditText sendEventEditText = dialogView.findViewById(R.id.sendEventEditText);
        sendButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                clickOnEventSendButton(sendEventEditText.getText().toString());
            }
        });

        return dialogView;
    }

    public void clickOnEventButton(Button buttonView)
    {
        String eventText = buttonView.getText().toString();

        clickOnEventSendButton(eventText);
    }

    public void clickOnEventSendButton(String eventText)
    {
        if(dialog != null && dialog.isShowing())
        {
            dialog.dismiss();
        }

        Date date = Calendar.getInstance().getTime();

        long trackingInterval = loadTrackingInterval();
        long beforeDateTime = date.getTime() - trackingInterval;
        Date beforeDate = new Date(beforeDateTime);
        int beforeHours = beforeDate.getHours();
        int beforeMinutes = beforeDate.getMinutes();

        addEvent(new Event(date, beforeHours, beforeMinutes, eventText));

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Date finishDate = new Date(Calendar.getInstance().getTimeInMillis());
        finishDate.setHours(22);
        finishDate.setMinutes(0);
        long finishMillisec = finishDate.getTime();
        finishMillisec = sharedPreferences.getLong("finishMillisec", finishMillisec);

        if(Calendar.getInstance().getTimeInMillis() + loadTrackingInterval() > finishMillisec)
        {
            stopNotification(this);

            showAlertAboutTomorrowLaunch();
        }
    }

    private static void showAlertAboutTomorrowLaunch()
    {

    }

    private String minutesToFullFormat(String shortMinutes)
    {
        if(shortMinutes.length() == 1)
        {
            return "0" + shortMinutes;
        }
        else
        {
            return shortMinutes;
        }
    }

    public static String INTENT_FILTER = "EventIntentFilter";
    public void addEvent(Event event)
    {
        Intent intent = new Intent(INTENT_FILTER);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editorSave = sharedPreferences.edit();

        lastEvents.add(event);
        String json = new Gson().toJson(lastEvents);
        intent.putExtra("lastEvents", json);
        editorSave.putString("lastEvents", json);

        sendBroadcast(intent);
        editorSave.apply();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent)
    {
        Context context = getApplicationContext();
        stopNotification(context);

        Intent intent = new Intent(context, this.getClass());
        intent.setPackage(getPackageName());
        int requestCode = 1012;
        PendingIntent pendingIntent = PendingIntent.getService(context, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);

        long diffBetweenNotify = loadTrackingInterval();

        pullNotification(context, diffBetweenNotify, Calendar.getInstance().getTimeInMillis(), pendingIntent);
        super.onTaskRemoved(rootIntent);
    }

    public static void pullNotification(Context context, long diffBetweenNotify, long firstNotifyTime_withoutDiff, PendingIntent pendingIntentOrNULL)
    {
        if(pendingIntentOrNULL == null)
        {
            Intent intent = new Intent(context, TimeTrackingReceiver.class);

            int requestCode = 1012;
            pendingIntentOrNULL = PendingIntent.getBroadcast(context, requestCode, intent,0);
        }

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Date defaultStartDate = new Date(Calendar.getInstance().getTimeInMillis());
        defaultStartDate.setHours(8);
        defaultStartDate.setMinutes(0);
        long startTrackingTime = sharedPreferences.getLong("startMillisec", defaultStartDate.getTime());

        Date defaultFinishDate = new Date(Calendar.getInstance().getTimeInMillis());
        defaultFinishDate.setHours(22);
        defaultFinishDate.setMinutes(0);
        long finishTrackingTime = sharedPreferences.getLong("finishMillisec", defaultFinishDate.getTime());

        if(firstNotifyTime_withoutDiff + diffBetweenNotify > finishTrackingTime)
        {
            //firstNotifyTime_withoutDiff = startTrackingTime + AlarmManager.INTERVAL_DAY;

            showAlertAboutTomorrowLaunch();
            return;
        }
        else if(firstNotifyTime_withoutDiff < startTrackingTime)
        {
            firstNotifyTime_withoutDiff = startTrackingTime;
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,  firstNotifyTime_withoutDiff + diffBetweenNotify, diffBetweenNotify, pendingIntentOrNULL);
    }

    private static void stopNotification(Context context)
    {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TimeTrackingReceiver.class);

        int requestCode = 1012;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent,0);
        PendingIntent pendingIntent2 = PendingIntent.getService(context, requestCode, intent,PendingIntent.FLAG_ONE_SHOT);

        alarmManager.cancel(pendingIntent);
        if(pendingIntent2 != null)
        {
            alarmManager.cancel(pendingIntent2);
        }
    }

    private ArrayList<Event> lastEvents = new ArrayList<>();
}
