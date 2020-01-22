package com.wingsmight.makeday.Menu;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.wingsmight.makeday.GoalNotification;
import com.wingsmight.makeday.Growth.Goals.Goal;
import com.wingsmight.makeday.Growth.Goals.GoalsTabFragment;
import com.wingsmight.makeday.Growth.GrowthFragment;
import com.wingsmight.makeday.Growth.Skills.SkillsTabFragment;
import com.wingsmight.makeday.MainActivity;
import com.wingsmight.makeday.R;
import com.wingsmight.makeday.SavingSystem.SaveLoad;
import com.wingsmight.makeday.TimePickerFragment;
import com.wingsmight.makeday.Tracker.TimeTracking.TimeTrackingTabFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class NotificationSettingsActivity extends AppCompatActivity
{
    public static int requestCode = 126;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_settings);

        ActionBar toolbar = getSupportActionBar();
        toolbar.setTitle(getResources().getString(R.string.NotificationSettingsText));

        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setDisplayShowHomeEnabled(true);

        final TextView morningTime = findViewById(R.id.morningTime);
        morningTime.setText(SaveLoad.loadString(SaveLoad.defaultSavingPath, "morningTime", "8:00"));
        morningTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TimePickerFragment timePickerDialog = new TimePickerFragment();
                timePickerDialog.addOnTimeSetListener(new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {
                        morningTime.setText(hourOfDay + ":" + toHourFormat(minute));

                        SaveLoad.saveString(SaveLoad.defaultSavingPath, "morningTime", morningTime.getText().toString());
                    }
                });
                timePickerDialog.show(getSupportFragmentManager(), "time picker");
            }
        });

        final TextView eveningTime = findViewById(R.id.eveningTime);
        eveningTime.setText(SaveLoad.loadString(SaveLoad.defaultSavingPath, "eveningTime", "22:00"));
        eveningTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TimePickerFragment timePickerDialog = new TimePickerFragment();
                timePickerDialog.addOnTimeSetListener(new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {
                        eveningTime.setText(hourOfDay + ":" + toHourFormat(minute));

                        SaveLoad.saveString(SaveLoad.defaultSavingPath, "eveningTime", eveningTime.getText().toString());

                        GoalNotification.setEveningSkillNotify(eveningTime.getContext());
                    }
                });
                timePickerDialog.show(getSupportFragmentManager(), "time picker");
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
