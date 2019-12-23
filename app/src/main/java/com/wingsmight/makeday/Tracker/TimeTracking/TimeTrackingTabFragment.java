package com.wingsmight.makeday.Tracker.TimeTracking;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.wingsmight.makeday.SavingSystem.SaveLoad;
import com.wingsmight.makeday.TabName;
import com.wingsmight.makeday.R;
import com.wingsmight.makeday.TimePickerFragment;
import com.wingsmight.makeday.Tracker.Event;
import com.wingsmight.makeday.Tracker.ReceiveNotifyAnswer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimeTrackingTabFragment extends Fragment
{
    private static final String ACTION_SNOOZE = "ACTION_SNOOZE";
    public static int requestCode = 1111;
    private TabName tabName = TabName.TIME_TRACKING;
    private RecyclerView recyclerView;
    private static ArrayList<TimeTrackingDay> timeTrackingDays = new ArrayList<>();
    private static TimeTrackingTabAdapter timeTrackingTabAdapter;

    private static final int NOTIFY_ID = 101;
    private static String CHANNEL_ID = "what did you do channel";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_time_tracking_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        recyclerView = view.findViewById(R.id.timeTrackerRecyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        timeTrackingDays = SaveLoad.load(tabName);
        timeTrackingTabAdapter = new TimeTrackingTabAdapter(timeTrackingDays);
        recyclerView.setAdapter(timeTrackingTabAdapter);

        //Button setLaunchSettings
        ExtendedFloatingActionButton launchButton = view.findViewById(R.id.launchButton);
        launchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                setLaunchSettings(view);
            }
        });
    }

    private TextView startTrackingTime;
    private TextView finishTrackingTime;
    private void setLaunchSettings(View view)
    {
        //Show popup
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.launch_tracking_dialog, null);
        builder.setView(dialogView);
        final AlertDialog popupDialog = builder.create();
        popupDialog.show();

        Button sendButton = dialogView.findViewById(R.id.dialog_like_bt);
        sendButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                launch();
                Toast.makeText(getContext(), "Уведомления настроены", Toast.LENGTH_SHORT).show();
                popupDialog.dismiss();
            }
        });

        startTrackingTime = dialogView.findViewById(R.id.startTrackingTime);
        startTrackingTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TimePickerFragment timePickerDialog = new TimePickerFragment();
                timePickerDialog.setTargetFragment(TimeTrackingTabFragment.this, requestCode);
                timePickerDialog.addOnTimeSetListener(new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {
                        startTrackingTime.setText(hourOfDay + ":" + toHourFormat(minute));
                    }
                });
                timePickerDialog.show(getFragmentManager(), "time picker");
            }
        });

        finishTrackingTime = dialogView.findViewById(R.id.finishTrackingTime);
        finishTrackingTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TimePickerFragment timePickerDialog = new TimePickerFragment();
                timePickerDialog.setTargetFragment(TimeTrackingTabFragment.this, requestCode);
                timePickerDialog.addOnTimeSetListener(new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {
                        finishTrackingTime.setText(hourOfDay + ":" + toHourFormat(minute));
                    }
                });
                timePickerDialog.show(getFragmentManager(), "time picker");
            }
        });

        Spinner spinner = dialogView.findViewById(R.id.intervalRequest);

        List<IntervalTrackingTime> userList = new ArrayList<>();
        userList.add(new IntervalTrackingTime(15));
        userList.add(new IntervalTrackingTime(30));
        userList.add(new IntervalTrackingTime(60));
        userList.add(new IntervalTrackingTime(90));

        ArrayAdapter<IntervalTrackingTime> adapter = new ArrayAdapter<>(getContext(), R.layout.interval_request_spinner_item, userList);
        adapter.setDropDownViewResource(R.layout.interval_request_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                IntervalTrackingTime interval = (IntervalTrackingTime)parent.getSelectedItem();
                displayUserData(interval);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public static IntervalTrackingTime intervalTrackingTime;
    private void displayUserData(IntervalTrackingTime interval)
    {
        intervalTrackingTime = interval;
    }

    private void launch()
    {
        Context context = getContext();

        RemoteInput remoteInput = new RemoteInput.Builder("key_text_reply")
                .setLabel("Введите другой ответ...")
                .build();

        Intent replyIntent;
        PendingIntent replyPendingIntent = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            replyIntent = new Intent("onNotifyButton");
            replyPendingIntent = PendingIntent.getBroadcast(context,0, replyIntent, 0);
        }
        else
        {
            replyIntent = new Intent("onNotifyButton");
            replyPendingIntent = PendingIntent.getBroadcast(context,0, replyIntent, 0);
        }

        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
                R.drawable.ic_send_black_24dp,
                "Ответить",
                replyPendingIntent
        ).addRemoteInput(remoteInput).build();

        RemoteViews collapsedView = new RemoteViews(getContext().getPackageName(), R.layout.notification_collapsed);
        RemoteViews expandedView = new RemoteViews(getContext().getPackageName(), R.layout.notification_expanded);

        expandedView.setOnClickPendingIntent(R.id.notifyButton1, getPendingSelfIntent(context, ReceiveNotifyAnswer.MyOnClick1));
        expandedView.setOnClickPendingIntent(R.id.notifyButton2, getPendingSelfIntent(context, ReceiveNotifyAnswer.MyOnClick2));
        expandedView.setOnClickPendingIntent(R.id.notifyButton3, getPendingSelfIntent(context, ReceiveNotifyAnswer.MyOnClick3));
        expandedView.setOnClickPendingIntent(R.id.notifyButton4, getPendingSelfIntent(context, ReceiveNotifyAnswer.MyOnClick4));
        expandedView.setOnClickPendingIntent(R.id.notifyButton5, getPendingSelfIntent(context, ReceiveNotifyAnswer.MyOnClick5));
        expandedView.setOnClickPendingIntent(R.id.notifyButton6, getPendingSelfIntent(context, ReceiveNotifyAnswer.MyOnClick6));


        Date date = Calendar.getInstance().getTime();

        int year = date.getYear() + 1900;
        int month = date.getMonth();
        int day = date.getDate();
        int dayOfWeek = date.getDay();
        int hours = date.getHours();
        int minutes = date.getMinutes();

        int timeDiff = intervalTrackingTime.getMinute();
        long millisecondsDiff = timeDiff * 60000;
        long beforeDateTime = date.getTime() - millisecondsDiff;
        Date beforeDate = new Date(beforeDateTime);
        int beforeHours = beforeDate.getHours();
        int beforeMinutes = beforeDate.getMinutes();

        expandedView.setTextViewText(R.id.WhatWasYouDoing, "Чем занимались с " + beforeHours + ":" + minutesToFullFormat(Integer.toString(beforeMinutes))
                + " до " + hours + ":" + minutesToFullFormat(Integer.toString(minutes)) + "?");
        collapsedView.setTextViewText(R.id.WhatWasYouDoing, "Чем занимались с " + beforeHours + ":" + minutesToFullFormat(Integer.toString(beforeMinutes))
                + " до " + hours + ":" + minutesToFullFormat(Integer.toString(minutes)) + "?");


        Notification notification = new NotificationCompat.Builder(context, "ChannelID")
                .setSmallIcon(R.drawable.app_icon)
                .setCustomBigContentView(expandedView)
                .setCustomContentView(collapsedView)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .addAction(replyAction)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .build();

        notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, notification);
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

    private static NotificationManagerCompat notificationManager;
    public static void cancelNotification(int id)
    {
        if(notificationManager != null)
        {
            notificationManager.cancel(id);
        }
    }

    private PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, ReceiveNotifyAnswer.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    public static void addEvent(Event event)
    {
        int size = timeTrackingDays.size();
        if(size != 0)
        {
            if(event.getDay() >= timeTrackingDays.get(0).getDay())
            {
                ArrayList<Event> newEventsArray = new ArrayList();
                newEventsArray.add(event);
                timeTrackingDays.add(0, new TimeTrackingDay(event.getDay(), event.getDayOfWeek(), event.getMonth(), event.getYear(), newEventsArray));
            }
            else
            {
                timeTrackingDays.get(0).AddTimeInterval(event);
            }
        }
        else
        {
            ArrayList<Event> newEventsArray = new ArrayList();
            newEventsArray.add(event);
            timeTrackingDays.add(new TimeTrackingDay(event.getDay(), event.getDayOfWeek(), event.getMonth(), event.getYear(), newEventsArray));
        }

        timeTrackingTabAdapter.notifyDataSetChanged();
    }

    private void backgroundSave(){
        Thread backgroundThread = new Thread() {
            @Override
            public void run()
            {
                SaveLoad.save(tabName, timeTrackingDays);
            }
        };
        backgroundThread.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        backgroundSave();
    }

    private String toHourFormat(int time)
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
}
