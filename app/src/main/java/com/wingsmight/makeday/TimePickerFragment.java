package com.wingsmight.makeday;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class TimePickerFragment extends DialogFragment
{
    TimePickerDialog.OnTimeSetListener onTimeSetListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        boolean is24HourView = android.text.format.DateFormat.is24HourFormat(getContext());
        if (onTimeSetListener == null)
        {
            onTimeSetListener = (TimePickerDialog.OnTimeSetListener)getTargetFragment();
        }

        return new TimePickerDialog(getActivity(), onTimeSetListener, hour, minute, is24HourView);
    }

    public void addOnTimeSetListener(TimePickerDialog.OnTimeSetListener onTimeSetListener)
    {
        this.onTimeSetListener = onTimeSetListener;
    }
}
