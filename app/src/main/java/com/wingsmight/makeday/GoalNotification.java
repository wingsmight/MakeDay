package com.wingsmight.makeday;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.wingsmight.makeday.SavingSystem.SaveLoad;

import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.AlertDialog;

public class GoalNotification
{
    private static AlertDialog alertDialog;
    private static Handler notifyHandler;

    public static void setEveningSkillNotify(Context context)
    {
        //show notification at evening
        long currentMilliseconds = Calendar.getInstance().getTimeInMillis();
        Date eveningDate = Calendar.getInstance().getTime();

        String eveningTime = SaveLoad.loadString(SaveLoad.defaultSavingPath, "eveningTime", "22:00");
        int eveningHour = Integer.valueOf(eveningTime.substring(0, eveningTime.indexOf(':')));
        int eveningMinutes = Integer.valueOf(eveningTime.substring(eveningTime.indexOf(':') + 1));

        eveningDate.setHours(eveningHour);
        eveningDate.setMinutes(eveningMinutes);

        long eveningMilliseconds = eveningDate.getTime();

        long millisecondsToNotify = eveningMilliseconds - currentMilliseconds;
        if(millisecondsToNotify < 0)
        {
            millisecondsToNotify += 24 * 60 * 60 * 1000;
        }

        GoalNotification.show(context, millisecondsToNotify);
    }

    public static void show(final Context context, final long millisecondsToNotify)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = createDialogView(context);

        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.getWindow().setGravity(Gravity.TOP);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = alertDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        notifyHandler = new Handler();
        notifyHandler.removeCallbacksAndMessages(null);
        notifyHandler.postDelayed(new Runnable() {
            public void run() {
                alertDialog.show();
                show(context, millisecondsToNotify + 24 * 60 * 60 * 1000);
            }
        }, millisecondsToNotify);
    }

    private static View createDialogView(Context context)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View dialogView = inflater.inflate(R.layout.goal_notiffication, null);

        //

        return dialogView;
    }
}
