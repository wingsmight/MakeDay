package com.wingsmight.makeday.Menu;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.wingsmight.makeday.GoalCheckingActivity;
import com.wingsmight.makeday.GoalNotificationReceiver;
import com.wingsmight.makeday.R;
import com.wingsmight.makeday.SavingSystem.SaveLoad;
import com.wingsmight.makeday.TimePickerFragment;

import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
        morningTime.setText(SaveLoad.loadString(SaveLoad.defaultSavingPath, "morningTime_HoursOfDay", "8")
                + ":" + SaveLoad.loadString(SaveLoad.defaultSavingPath, "morningTime_Minute", "00"));

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

                        SaveLoad.saveString(SaveLoad.defaultSavingPath, "morningTime_HoursOfDay", Integer.toString(hourOfDay));
                        SaveLoad.saveString(SaveLoad.defaultSavingPath, "morningTime_Minute", toHourFormat(minute));
                    }
                });
                timePickerDialog.show(getSupportFragmentManager(), "time picker");
            }
        });

        final Context context = this;
        final TextView eveningTime = findViewById(R.id.eveningTime);
        eveningTime.setText(SaveLoad.loadString(SaveLoad.defaultSavingPath, "eveningTime_HoursOfDay", "22")
                + ":" + SaveLoad.loadString(SaveLoad.defaultSavingPath, "eveningTime_Minute", "00"));

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

                        SaveLoad.saveString(SaveLoad.defaultSavingPath, "eveningTime_HoursOfDay", Integer.toString(hourOfDay));
                        SaveLoad.saveString(SaveLoad.defaultSavingPath, "eveningTime_Minute", toHourFormat(minute));

                        Calendar eveningDate = Calendar.getInstance();
                        eveningDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        eveningDate.set(Calendar.MINUTE, minute);
                        eveningDate.set(Calendar.SECOND, 0);

                        if(eveningDate.getTimeInMillis() < System.currentTimeMillis())
                        {
                            eveningDate.setTimeInMillis(eveningDate.getTimeInMillis() + 86400000);
                        }

                        GoalCheckingActivity.setEveningSkillNotify(context, eveningDate);
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
