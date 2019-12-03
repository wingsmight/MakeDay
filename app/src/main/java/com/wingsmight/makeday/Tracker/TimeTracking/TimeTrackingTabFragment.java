package com.wingsmight.makeday.Tracker.TimeTracking;

import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.wingsmight.makeday.MainActivity;
import com.wingsmight.makeday.SavingSystem.SaveLoad;
import com.wingsmight.makeday.TabName;
import com.wingsmight.makeday.R;
import com.wingsmight.makeday.TimePickerFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TimeTrackingTabFragment extends Fragment
{
    public static int requestCode = 1111;
    private TabName tabName = TabName.TIME_TRACKING;
    private RecyclerView recyclerView;
    private ArrayList<TimeTrackingDay> timeTrackingDays = new ArrayList<>();
    private TimeTrackingTabAdapter timeTrackingTabAdapter;

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

    private void displayUserData(IntervalTrackingTime interval)
    {
        //Toast.makeText(getContext(), interval, Toast.LENGTH_LONG).show();
    }

    private void launch()
    {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_date_range_black_24dp)
                        .setContentTitle("Чем занимались?")
                        .setContentText("Чем занимались с " + "??:??" + " до " + "??:??" + "?")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(getContext());
        notificationManager.notify(NOTIFY_ID, builder.build());
    }

    public void executePeriodicTask() {
        final android.os.Handler handler = new android.os.Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            try
                            {
                                //call the method that fetches your data here

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };


        timer.schedule(doAsynchronousTask, 0, 5000); //execute in every 50000 ms

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
