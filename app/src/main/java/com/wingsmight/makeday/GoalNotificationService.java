package com.wingsmight.makeday;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wingsmight.makeday.Growth.Goals.Goal;
import com.wingsmight.makeday.Growth.Goals.GoalsTabAdapter;
import com.wingsmight.makeday.SavingSystem.SaveLoad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

public class GoalNotificationService extends Service
{
    private static String goalLabel;

    private Date day;

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

    private int goalIndex = 0;
    AlertDialog dialog;
    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId)
    {
        day = Calendar.getInstance().getTime();

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.AppTheme_MaterialDialogTheme);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View dialogView = inflater.inflate(R.layout.goal_notiffication, null);

        dialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialog)
            {
                goalIndex = 0;

                Intent intent = new Intent("GPSLocationUpdates");
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editorSave = sharedPreferences.edit();

                String json = new Gson().toJson(lastDoneDeals);
                intent.putExtra("lastDoneDeals", json);
                editorSave.putString("lastDoneDeals", json);

                json = new Gson().toJson(lastUndoneDeals);
                intent.putExtra("lastUndoneDeals", json);
                editorSave.putString("lastUndoneDeals", json);

                json = new Gson().toJson(lastAlmostDeals);
                intent.putExtra("lastAlmostDeals", json);
                editorSave.putString("lastAlmostDeals", json);

                sendBroadcast(intent);
                editorSave.apply();

                lastUndoneDeals.clear();
                lastDoneDeals.clear();
                lastAlmostDeals.clear();
            }
        });
        dialogBuilder.setView(dialogView);

        if(dialog != null)
        {
            dialog.dismiss();
        }
        dialog = dialogBuilder.create();
        final Window dialogWindow = dialog.getWindow();
        final WindowManager.LayoutParams dialogWindowAttributes = dialogWindow.getAttributes();

        /////////////////////////////////////////////////////////////////////////////////////////////
        GoalsTabAdapter.goals = preLoad();

        if(GoalsTabAdapter.goals == null)
        {
            return START_STICKY;
        }

        for(int i = 0; i < GoalsTabAdapter.goals.size(); i++)
        {
            if (!GoalsTabAdapter.goals.get(i).getIsChecked())
            {
                GoalsTabAdapter.goals.remove(i);
                i--;
            }
        }

        if(GoalsTabAdapter.goals == null)
        {
            return START_STICKY;
        }
        if(GoalsTabAdapter.goals.size() > goalIndex)
        {
            goalLabel = GoalsTabAdapter.goals.get(goalIndex).getName();
        }
        else
        {
            return START_STICKY;
        }

        final TextView goalText = dialogView.findViewById(R.id.goalText);
        goalText.setText(goalLabel);

        Button yesButton = dialogView.findViewById(R.id.yesAnswer);
        yesButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //saveDoneDealToDay(day, goalLabel);
                lastDoneDeals.add(new GoalNotificationStruct(day, goalLabel, DealType.DONE));



                goalIndex++;
                if(goalIndex < GoalsTabAdapter.goals.size())
                {
                    goalLabel = GoalsTabAdapter.goals.get(goalIndex).getName();

                    goalText.setText(goalLabel);
                }
                else
                {
                    dialog.dismiss();
                }
            }
        });

        Button noButton = dialogView.findViewById(R.id.noAnswer);
        noButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                lastUndoneDeals.add(new GoalNotificationStruct(day, goalLabel, DealType.UNDONE));

                goalIndex++;
                if(goalIndex < GoalsTabAdapter.goals.size())
                {
                    goalLabel = GoalsTabAdapter.goals.get(goalIndex).getName();

                    goalText.setText(goalLabel);
                }
                else
                {
                    dialog.dismiss();
                }
            }
        });

        Button almostButton = dialogView.findViewById(R.id.almostAnswer);
        almostButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                lastAlmostDeals.add(new GoalNotificationStruct(day, goalLabel, DealType.ALMOST));

                goalIndex++;
                if(goalIndex < GoalsTabAdapter.goals.size())
                {
                    goalLabel = GoalsTabAdapter.goals.get(goalIndex).getName();

                    goalText.setText(goalLabel);
                }
                else
                {
                    dialog.dismiss();
                }
            }
        });

        TextView skipButton = dialogView.findViewById(R.id.skipButton);
        skipButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                goalIndex++;
                if(goalIndex < GoalsTabAdapter.goals.size())
                {
                    goalLabel = GoalsTabAdapter.goals.get(goalIndex).getName();

                    goalText.setText(goalLabel);
                }
                else
                {
                    dialog.dismiss();
                }
            }
        });

        Button doneButton = dialogView.findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        /////////////////////////////////////////////////////////////////////////////////////////////

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
        //dialogWindowAttributes.windowAnimations = R.style.DialogAnimation;
        dialog.show();

        return START_STICKY;
    }

    private ArrayList<Goal> preLoad()
    {
        SharedPreferences sharedPreferences = this.getSharedPreferences("TabArray", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(TabName.GOALS.toString(), "");
        return new Gson().fromJson(json, new TypeToken<List<Goal>>(){}.getType());
    }

    @Override
    public void onTaskRemoved(Intent rootIntent)
    {
        int hourOfDay = Integer.valueOf(SaveLoad.loadString(SaveLoad.defaultSavingPath, "eveningTime_HoursOfDay", "22"));
        int minute =  Integer.valueOf(SaveLoad.loadString(SaveLoad.defaultSavingPath, "eveningTime_Minute", "00"));

        Calendar eveningDate = Calendar.getInstance();
        eveningDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
        eveningDate.set(Calendar.MINUTE, minute);
        eveningDate.set(Calendar.SECOND, 0);

        if(eveningDate.getTimeInMillis() < System.currentTimeMillis())
        {
            eveningDate.setTimeInMillis(eveningDate.getTimeInMillis() + 86400000);
        }

        Intent restartServiceTask = new Intent(getApplicationContext(),this.getClass());
        restartServiceTask.setPackage(getPackageName());
        PendingIntent restartPendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceTask, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager myAlarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        myAlarmService.setRepeating(
                AlarmManager.RTC_WAKEUP,
                eveningDate.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                restartPendingIntent);

        super.onTaskRemoved(rootIntent);
    }

    public static void setEveningSkillNotify(Context context, Calendar eveningDate)
    {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, GoalNotificationReceiver.class);

        int requestCode = 1011;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent,0);

        //Устанавливаем интервал срабатывания в 1 минуту
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, eveningDate.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }


    private ArrayList<GoalNotificationStruct> lastDoneDeals = new ArrayList<>();
    private ArrayList<GoalNotificationStruct> lastUndoneDeals = new ArrayList<>();
    private ArrayList<GoalNotificationStruct> lastAlmostDeals = new ArrayList<>();
    public class GoalNotificationStruct implements Serializable
    {
        public Date day;
        public String dealLabel;
        public DealType dealType;

        public GoalNotificationStruct(Date day, String dealLabel, DealType dealType)
        {
            this.day = day;
            this.dealLabel = dealLabel;
            this.dealType = dealType;
        }
    }

    enum DealType
    {
        UNDONE,
        DONE,
        ALMOST
    }
}
