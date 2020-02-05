package com.wingsmight.makeday;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.PowerManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wingsmight.makeday.Growth.Goals.GoalsTabAdapter;
import com.wingsmight.makeday.Growth.Goals.GoalsTabFragment;
import com.wingsmight.makeday.Growth.GrowthFragment;
import com.wingsmight.makeday.MyDays.MyDaysAdapter;
import com.wingsmight.makeday.MyDays.MyDaysFragment;
import com.wingsmight.makeday.SavingSystem.SaveLoad;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class GoalNotification extends BroadcastReceiver
{
    private static AlertDialog alertDialog;
    private static Handler notifyHandler;
    private static String goalLabel;

    private static MyDaysFragment myDaysFragment;

    public static void setEveningSkillNotify(Context context)
    {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        MyDaysFragment myDaysFragmentTest = ((MyDaysFragment)(fragmentManager.findFragmentByTag("1")));
        if(myDaysFragmentTest != null)
        {
            myDaysFragment = myDaysFragmentTest;
        }

        //show notification at evening
        long currentMilliseconds = Calendar.getInstance().getTimeInMillis();

        Date eveningDate = Calendar.getInstance().getTime();
        String eveningTime = SaveLoad.loadString(SaveLoad.defaultSavingPath, "eveningTime", "22:00");
        int eveningHour = Integer.valueOf(eveningTime.substring(0, eveningTime.indexOf(':')));
        int eveningMinutes = Integer.valueOf(eveningTime.substring(eveningTime.indexOf(':') + 1));

        eveningDate.setHours(eveningHour);
        eveningDate.setMinutes(eveningMinutes);
        eveningDate.setSeconds(0);

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
        if(dialogView == null) return;

        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.getWindow().setGravity(Gravity.TOP);

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialog)
            {
                goalIndex = 0;
                setEveningSkillNotify(context);
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = alertDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        HandlerThread handlerThread = new HandlerThread("background-thread");
        handlerThread.start();
        notifyHandler = new Handler(handlerThread.getLooper());
        notifyHandler.removeCallbacksAndMessages(null);
        notifyHandler.postDelayed(new Runnable() {
            public void run() {
                alertDialog.show();
            }
        }, millisecondsToNotify);
    }

    public static void showEveryEvening(Context context)
    {
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, GoalNotification.class);
        //intent.putExtra(ONE_TIME, Boolean.FALSE);//Задаем параметр интента
        int requestCode = 1011;
        PendingIntent pi = PendingIntent.getBroadcast(context,requestCode, intent,0);
        //Устанавливаем интервал срабатывания в 1 минуту.
        manager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),60*1000,pi);
    }

    private static int goalIndex = 0;
    private static View createDialogView(final Context context)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View dialogView = inflater.inflate(R.layout.goal_notiffication, null);

        if(GoalsTabAdapter.goals == null)
        {
            GoalsTabAdapter.goals = SaveLoad.load(TabName.GOALS);
            for(int i = 0; i < GoalsTabAdapter.goals.size(); i++)
            {
                if (!GoalsTabAdapter.goals.get(i).getIsChecked())
                {
                    GoalsTabAdapter.goals.remove(i);
                    i--;
                }
            }

            if(GoalsTabAdapter.goals == null) return null;
        }
        if(GoalsTabAdapter.goals.size() > goalIndex)
        {
            goalLabel = GoalsTabAdapter.goals.get(goalIndex).getName();
        }
        else
        {
            return null;
        }

        final TextView goalText = dialogView.findViewById(R.id.goalText);
        goalText.setText(goalLabel);


        Button yesButton = dialogView.findViewById(R.id.yesAnswer);
        yesButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                myDaysFragment.addDoneDealToToday(goalLabel);
                goalIndex++;
                if(goalIndex < GoalsTabAdapter.goals.size())
                {
                    goalLabel = GoalsTabAdapter.goals.get(goalIndex).getName();

                    goalText.setText(goalLabel);
                }
                else
                {
                    alertDialog.dismiss();
                }
            }
        });

        Button noButton = dialogView.findViewById(R.id.noAnswer);
        noButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                myDaysFragment.addUndoneDealToToday(goalLabel);
                goalIndex++;
                if(goalIndex < GoalsTabAdapter.goals.size())
                {
                    goalLabel = GoalsTabAdapter.goals.get(goalIndex).getName();

                    goalText.setText(goalLabel);
                }
                else
                {
                    alertDialog.dismiss();
                }
            }
        });

        Button almostButton = dialogView.findViewById(R.id.almostAnswer);
        almostButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                myDaysFragment.addAlmostDealToToday(goalLabel);
                goalIndex++;
                if(goalIndex < GoalsTabAdapter.goals.size())
                {
                    goalLabel = GoalsTabAdapter.goals.get(goalIndex).getName();

                    goalText.setText(goalLabel);
                }
                else
                {
                    alertDialog.dismiss();
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
                    alertDialog.dismiss();
                }
            }
        });

        Button doneButton = dialogView.findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                alertDialog.dismiss();
            }
        });

        return dialogView;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        //PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        //PowerManager.WakeLock wl= powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"YOUR TAG1");

//Осуществляем блокировку
        //wl.acquire();

//Здесь можно делать обработку.
        Intent i = new Intent();
        i.setClassName(context.getPackageName(), GoalCheckingActivity.class.getName());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);





















//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        View dialogView = createDialogView(context);
//        if(dialogView == null) return;
//
//        builder.setView(dialogView);
//        alertDialog = builder.create();
//        alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        alertDialog.getWindow().setGravity(Gravity.TOP);
//
////        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
////        {
////            @Override
////            public void onDismiss(DialogInterface dialog)
////            {
////                goalIndex = 0;
////                setEveningSkillNotify(context);
////            }
////        });
//
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        Window window = alertDialog.getWindow();
//        lp.copyFrom(window.getAttributes());
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        window.setAttributes(lp);
//
//        notifyHandler = new Handler();
//        notifyHandler.removeCallbacksAndMessages(null);
//        notifyHandler.post(new Runnable() {
//            public void run() {
//                alertDialog.show();
//            }
//        });

//Разблокируем поток.
        //wl.release();
    }
}
